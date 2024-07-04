package io.sillysillyman.deventer.service.admin;

import io.sillysillyman.deventer.dto.comment.CommentResponseDto;
import io.sillysillyman.deventer.dto.comment.UpdateCommentRequestDto;
import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.enums.NotFoundEntity;
import io.sillysillyman.deventer.exception.EntityNotFoundException;
import io.sillysillyman.deventer.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글을 업데이트합니다.
     *
     * @param commentId               댓글 ID
     * @param updateCommentRequestDto 댓글 요청 DTO
     * @return 업데이트된 댓글의 응답 DTO
     */
    @Transactional
    public CommentResponseDto updateComment(
        Long commentId,
        UpdateCommentRequestDto updateCommentRequestDto) {
        
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.update(updateCommentRequestDto.getContent());
        return new CommentResponseDto(comment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 댓글 ID
     */
    public void deleteComment(Long commentId) {
        Comment comment = getCommentByIdOrThrow(commentId);
        commentRepository.delete(comment);
    }

    /**
     * 주어진 ID로 댓글을 찾거나, 존재하지 않으면 예외를 던집니다.
     *
     * @param commentId 댓글 ID
     * @return Comment 객체
     */
    public Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
    }
}