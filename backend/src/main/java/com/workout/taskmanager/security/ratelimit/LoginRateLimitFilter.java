package com.workout.taskmanager.security.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory rate limiter for login attempts.
 * Limits to 5 failed attempts per IP per 15-minute window.
 * Only increments count on failed login attempts (non-2xx response).
 */
@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 900; // 15 minutes
    private static final long EVICTION_INTERVAL_SECONDS = 300; // Clean up every 5 minutes

    private final Map<String, AttemptInfo> attempts = new ConcurrentHashMap<>();
    private volatile Instant lastEviction = Instant.now();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (!"/auth/login".equals(path) || !"POST".equals(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        evictExpiredEntries();

        String clientIp = request.getRemoteAddr();
        AttemptInfo info = attempts.compute(clientIp, (key, existing) -> {
            if (existing == null || existing.isExpired()) {
                return new AttemptInfo();
            }
            return existing;
        });

        if (info.getCount() >= MAX_ATTEMPTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            long retryAfter = info.getSecondsUntilReset();
            response.setHeader("Retry-After", String.valueOf(retryAfter));
            response.getWriter().write(
                    "{\"status\":\"TOO_MANY_REQUESTS\",\"message\":\"Too many login attempts. Try again in " + retryAfter + " seconds.\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);

        // Only count failed attempts (non-2xx responses)
        if (response.getStatus() >= 400) {
            info.increment();
        }
    }

    /**
     * Periodically remove expired entries to prevent unbounded memory growth.
     */
    private void evictExpiredEntries() {
        Instant now = Instant.now();
        if (now.isAfter(lastEviction.plusSeconds(EVICTION_INTERVAL_SECONDS))) {
            lastEviction = now;
            Iterator<Map.Entry<String, AttemptInfo>> it = attempts.entrySet().iterator();
            while (it.hasNext()) {
                if (it.next().getValue().isExpired()) {
                    it.remove();
                }
            }
        }
    }

    private static class AttemptInfo {
        private int count = 0;
        private final Instant windowStart = Instant.now();

        public synchronized int getCount() {
            return count;
        }

        public synchronized void increment() {
            count++;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(windowStart.plusSeconds(WINDOW_SECONDS));
        }

        public long getSecondsUntilReset() {
            long remaining = WINDOW_SECONDS - (Instant.now().getEpochSecond() - windowStart.getEpochSecond());
            return Math.max(0, remaining);
        }
    }
}
