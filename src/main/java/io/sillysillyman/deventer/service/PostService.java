package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.dto.comment.CommentResponseDto;
import io.sillysillyman.deventer.dto.post.CreatePostRequestDto;
import io.sillysillyman.deventer.dto.post.PostResponseDto;
import io.sillysillyman.deventer.dto.post.PostWithCommentsResponseDto;
import io.sillysillyman.deventer.dto.post.UpdatePostRequestDto;
import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.NotFoundEntity;
import io.sillysillyman.deventer.enums.UserStatus;
import io.sillysillyman.deventer.exception.EntityNotFoundException;
import io.sillysillyman.deventer.repository.CategoryRepository;
import io.sillysillyman.deventer.repository.CommentRepository;
import io.sillysillyman.deventer.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시물을 생성합니다.
     *
     * @param createPostRequestDto 게시물 생성 요청 DTO
     * @param user                 현재 인증된 사용자 정보
     * @return 생성된 게시물 응답 DTO
     */
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User user) {
        validateUserNotBlocked(user);

        String title = createPostRequestDto.getTitle();
        String content = createPostRequestDto.getContent();
        Category category = getCategoryByTopicOrThrow(createPostRequestDto.getCategoryTopic());
        Post post = new Post(title, content, user, category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    /**
     * 게시물 상세 정보를 가져옵니다.
     *
     * @param postId 게시물 ID
     * @return 게시물 상세 정보와 댓글 목록을 포함한 DTO
     */
    @Transactional(readOnly = true)
    public PostWithCommentsResponseDto getPostDetail(Long postId) {
        Post post = getPostByIdOrThrow(postId);
        PostResponseDto postResponseDto = new PostResponseDto(post);

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtos = comments.stream()
            .map(CommentResponseDto::new).toList();

        return new PostWithCommentsResponseDto(postResponseDto, commentResponseDtos);
    }

    /**
     * 모든 게시물을 페이지네이션하여 가져옵니다.
     *
     * @param pageable 페이지 요청 정보
     * @return 게시물 응답 DTO 목록
     */
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable).map(PostResponseDto::new);
    }

    /**
     * 특정 카테고리 내의 모든 게시물을 페이지네이션하여 가져옵니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable   페이지 요청 정보
     * @return 카테고리 내의 게시물 응답 DTO 목록
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = getCategoryByIdOrThrow(categoryId);

        return postRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable)
            .map(PostResponseDto::new);
    }

    /**
     * 게시물을 수정합니다.
     *
     * @param postId               수정할 게시물 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @param user                 현재 인증된 사용자 정보
     * @return 수정된 게시물 응답 DTO
     */
    @Transactional
    public PostResponseDto updatePost(
        Long postId,
        UpdatePostRequestDto updatePostRequestDto,
        User user) {

        validateUserNotBlocked(user);
        Post post = getPostByIdOrThrow(postId);
        validatePostOwnership(post, user);

        String title = updatePostRequestDto.getTitle();
        String content = updatePostRequestDto.getContent();
        post.update(title, content);

        return new PostResponseDto(post);
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param postId 삭제할 게시물 ID
     * @param user   현재 인증된 사용자 정보
     */
    public void deletePost(Long postId, User user) {
        validateUserNotBlocked(user);
        Post post = getPostByIdOrThrow(postId);
        validatePostOwnership(post, user);
        postRepository.delete(post);
    }

    /**
     * 사용자 상태가 BLOCKED인지 확인합니다.
     *
     * @param user 확인할 사용자 객체
     */
    private void validateUserNotBlocked(User user) {
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND);
        }
    }

    /**
     * 주어진 게시물의 소유자가 주어진 사용자와 일치하는지 확인합니다.
     *
     * @param post 확인할 게시물
     * @param user 확인할 사용자
     */
    private void validatePostOwnership(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND);
        }
    }

    /**
     * 주어진 주제로 카테고리를 조회하고, 없으면 예외를 던집니다.
     *
     * @param topic 카테고리 주제
     * @return 조회된 카테고리
     */
    private Category getCategoryByTopicOrThrow(String topic) {
        return categoryRepository.findByTopic(topic)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.CATEGORY_NOT_FOUND));
    }

    /**
     * 주어진 ID로 카테고리를 조회하고, 없으면 예외를 던집니다.
     *
     * @param categoryId 카테고리 ID
     * @return 조회된 카테고리
     */
    private Category getCategoryByIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.CATEGORY_NOT_FOUND));
    }

    /**
     * 주어진 ID로 게시물을 조회하고, 없으면 예외를 던집니다.
     *
     * @param postId 게시물 ID
     * @return 조회된 게시물
     */
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
    }
}