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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory rate limiter for login attempts.
 * Limits to 5 attempts per IP per 15-minute window.
 */
@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 900; // 15 minutes

    private final Map<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

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

        String clientIp = getClientIp(request);
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

        info.increment();
        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
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
