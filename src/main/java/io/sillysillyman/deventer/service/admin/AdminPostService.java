package io.sillysillyman.deventer.service.admin;

import io.sillysillyman.deventer.dto.post.ChangePostCategoryRequestDto;
import io.sillysillyman.deventer.dto.post.CreatePostRequestDto;
import io.sillysillyman.deventer.dto.post.PostResponseDto;
import io.sillysillyman.deventer.dto.post.UpdatePostRequestDto;
import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.NotFoundEntity;
import io.sillysillyman.deventer.exception.EntityNotFoundException;
import io.sillysillyman.deventer.repository.CategoryRepository;
import io.sillysillyman.deventer.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 공지글을 생성합니다.
     *
     * @param createPostRequestDto 게시물 생성 요청 DTO
     * @param admin                현재 인증된 관리자 정보
     * @return 생성된 게시물 응답 DTO
     */
    public PostResponseDto createNotice(CreatePostRequestDto createPostRequestDto, User admin) {
        Category category = getCategoryByTopicOrThrow(createPostRequestDto.getCategoryTopic());

        String title = createPostRequestDto.getTitle();
        String content = createPostRequestDto.getContent();
        Post post = new Post(title, content, admin, category, true);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    /**
     * 공지글을 수정합니다.
     *
     * @param postId               수정할 공지글 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @return 수정된 게시물 응답 DTO
     */
    @Transactional
    public PostResponseDto updateNotice(Long postId, UpdatePostRequestDto updatePostRequestDto) {
        Post post = getPostByIdOrThrow(postId);

        if (!post.isNotice()) {
            throw new IllegalArgumentException("해당 게시물은 공지글이 아닙니다.");
        }

        String title = updatePostRequestDto.getTitle();
        String content = updatePostRequestDto.getContent();
        post.update(title, content);
        return new PostResponseDto(post);
    }

    /**
     * 관리자 권한으로 게시물을 수정합니다.
     *
     * @param postId               수정할 게시물 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @return 수정된 게시물 응답 DTO
     */
    @Transactional
    public PostResponseDto updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto) {
        Post post = getPostByIdOrThrow(postId);

        String title = updatePostRequestDto.getTitle();
        String content = updatePostRequestDto.getContent();
        post.update(title, content);
        return new PostResponseDto(post);
    }

    /**
     * 관리자 권한으로 게시물을 삭제합니다.
     *
     * @param postId 삭제할 게시물 ID
     */
    public void deletePost(Long postId) {
        Post post = getPostByIdOrThrow(postId);
        postRepository.delete(post);
    }

    /**
     * 게시물의 카테고리를 변경합니다.
     *
     * @param postId                       이동할 게시물 ID
     * @param changePostCategoryRequestDto 카테고리 이동 요청 DTO
     * @return 이동된 게시물 응답 DTO
     */
    @Transactional
    public PostResponseDto changePostCategory(Long postId,
        ChangePostCategoryRequestDto changePostCategoryRequestDto) {

        Post post = getPostByIdOrThrow(postId);
        Category category = getCategoryByTopicOrThrow(
            changePostCategoryRequestDto.getCategoryTopic());

        post.setCategory(category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    /**
     * 게시물 ID로 게시물을 찾고, 찾을 수 없으면 예외를 던집니다.
     *
     * @param postId 게시물 ID
     * @return 게시물 엔티티
     * @throws EntityNotFoundException 게시물을 찾을 수 없는 경우
     */
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
    }

    /**
     * 카테고리 주제로 카테고리를 찾고, 찾을 수 없으면 예외를 던집니다.
     *
     * @param topic 카테고리 주제
     * @return 카테고리 엔티티
     * @throws EntityNotFoundException 카테고리를 찾을 수 없는 경우
     */
    private Category getCategoryByTopicOrThrow(String topic) {
        return categoryRepository.findByTopic(topic)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.CATEGORY_NOT_FOUND));
    }
}