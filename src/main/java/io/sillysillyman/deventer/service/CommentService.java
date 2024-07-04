package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.dto.comment.CommentResponseDto;
import io.sillysillyman.deventer.dto.comment.CreateCommentRequestDto;
import io.sillysillyman.deventer.dto.comment.UpdateCommentRequestDto;
import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.NotFoundEntity;
import io.sillysillyman.deventer.enums.UserActionError;
import io.sillysillyman.deventer.enums.UserStatus;
import io.sillysillyman.deventer.exception.EntityNotFoundException;
import io.sillysillyman.deventer.exception.UserActionNotAllowedException;
import io.sillysillyman.deventer.repository.CommentRepository;
import io.sillysillyman.deventer.repository.PostRepository;
import io.sillysillyman.deventer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글을 생성합니다.
     *
     * @param createCommentRequestDto 댓글 생성 요청 DTO
     * @param user                    현재 인증된 사용자 정보
     * @return 생성된 댓글 응답 DTO
     */
    public CommentResponseDto createComment(CreateCommentRequestDto createCommentRequestDto,
        User user) {
        validateUserStatus(user.getId());

        Post post = getPostByIdOrThrow(createCommentRequestDto.getPostId());
        Comment comment = new Comment(createCommentRequestDto.getContent(), user, post);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentId               수정할 댓글 ID
     * @param updateCommentRequestDto 댓글 수정 요청 DTO
     * @param user                    현재 인증된 사용자 정보
     * @return 수정된 댓글 응답 DTO
     */
    @Transactional
    public CommentResponseDto updateComment(
        Long commentId,
        UpdateCommentRequestDto updateCommentRequestDto,
        User user) {

        validateUserStatus(user.getId());

        Comment comment = getCommentByIdOrThrow(commentId);
        validateUserOwnership(comment, user.getId());
        comment.update(updateCommentRequestDto.getContent());

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @param user      현재 인증된 사용자 정보
     */
    public void deleteComment(Long commentId, User user) {
        validateUserStatus(user.getId());
        Comment comment = getCommentByIdOrThrow(commentId);
        validateUserOwnership(comment, user.getId());
        commentRepository.delete(comment);
    }

    /**
     * 사용자와 댓글 작성자가 일치하는지 확인합니다.
     *
     * @param comment 댓글 객체
     * @param userId  사용자 ID
     */
    private void validateUserOwnership(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new UserActionNotAllowedException(UserActionError.NOT_AUTHORIZED);
        }
    }

    /**
     * 사용자 상태를 확인합니다. BLOCKED 상태인 경우 예외를 던집니다.
     *
     * @param userId 사용자 ID
     */
    private void validateUserStatus(Long userId) {
        User user = userRepository.findById((userId)).orElseThrow(
            () -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));
        if (user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new UserActionNotAllowedException(UserActionError.BLOCKED_USER);
        }
    }

    /**
     * 댓글을 ID로 조회합니다. 존재하지 않으면 예외를 던집니다.
     *
     * @param commentId 조회할 댓글 ID
     * @return 조회된 댓글
     */
    private Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
    }

    /**
     * 게시물을 ID로 조회합니다. 존재하지 않으면 예외를 던집니다.
     *
     * @param postId 조회할 게시물 ID
     * @return 조회된 게시물
     */
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
    }
}