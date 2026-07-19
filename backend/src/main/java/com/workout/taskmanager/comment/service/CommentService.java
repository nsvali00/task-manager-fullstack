package com.workout.taskmanager.comment.service;

import com.workout.taskmanager.comment.dto.CommentCreateRequest;
import com.workout.taskmanager.comment.dto.CommentResponse;
import com.workout.taskmanager.comment.entity.Comment;
import com.workout.taskmanager.comment.repository.CommentRepository;
import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.IssueNotFoundException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.issue.entity.Issue;
import com.workout.taskmanager.issue.repository.IssueRepository;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final ProjectMemberRepository memberRepository;

    @Transactional
    public CommentResponse addComment(Long issueId, CommentCreateRequest request, User user) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(issueId));
        checkMembership(issue.getProject(), user);

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setIssue(issue);
        comment.setUser(user);

        comment = commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByIssue(Long issueId, Pageable pageable, User user) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(issueId));
        checkMembership(issue.getProject(), user);

        return commentRepository.findByIssueId(issueId, pageable)
                .map(CommentResponse::from);
    }

    @Transactional
    public void deleteComment(Long commentId, Long issueId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));

        if (!comment.getIssue().getId().equals(issueId)) {
            throw new ResourceNotFoundException("Comment not found with id " + commentId + " for issue " + issueId);
        }

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    private void checkMembership(Project project, User user) {
        if (!memberRepository.existsByProjectAndUser(project, user)) {
            throw new AccessDeniedException("Not a project member");
        }
    }
}