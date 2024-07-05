package io.sillysillyman.deventer.controller;

import io.sillysillyman.deventer.dto.post.CreatePostRequestDto;
import io.sillysillyman.deventer.dto.post.PostResponseDto;
import io.sillysillyman.deventer.dto.post.PostWithCommentsResponseDto;
import io.sillysillyman.deventer.dto.post.UpdatePostRequestDto;
import io.sillysillyman.deventer.security.UserDetailsImpl;
import io.sillysillyman.deventer.service.PostLikeService;
import io.sillysillyman.deventer.service.PostService;
import io.sillysillyman.deventer.service.ScrapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private static final int PAGE_SIZE = 10;
    private final PostService postService;
    private final PostLikeService postLikeService;
    private final ScrapService scrapService;

    /**
     * 게시물을 생성합니다.
     *
     * @param createPostRequestDto 게시물 생성 요청 DTO
     * @param userDetails          현재 인증된 사용자 정보
     * @return 생성된 게시물 응답 DTO
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
        @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.createPost(createPostRequestDto,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDto);
    }

    /**
     * 게시물에 좋아요를 토글합니다. 이미 좋아요된 게시물은 좋아요가 취소되고, 좋아요되지 않은 게시물은 좋아요가 추가됩니다.
     *
     * @param postId      좋아요를 토글할 게시물 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 좋아요 처리 결과 메시지와 좋아요 개수
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> togglePostLike(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok()
            .body(postLikeService.toggleLike(postId, userDetails.getUser()));
    }

    /**
     * 게시물에 스크랩을 토글합니다. 이미 스크랩된 게시물은 스크랩이 취소되고, 스크랩되지 않은 게시물은 스크랩이 추가됩니다.
     *
     * @param postId      스크랩할 게시물의 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 스크랩 처리 결과 메시지
     */
    @PostMapping("/{postId}/scrap")
    public ResponseEntity<String> toggleScrap(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok()
            .body(scrapService.toggleScrap(postId, userDetails.getUser()));
    }

    /**
     * 게시물 상세 정보를 조회합니다.
     *
     * @param postId 조회할 게시물 ID
     * @return 게시물 상세 정보 응답 DTO
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostWithCommentsResponseDto> getPostDetail(@PathVariable Long postId) {
        PostWithCommentsResponseDto postWithCommentsResponseDto = postService.getPostDetail(postId);
        return ResponseEntity.ok().body(postWithCommentsResponseDto);
    }

    /**
     * 전체 게시물을 조회합니다.
     *
     * @param page 페이지 번호
     * @return 페이지네이션된 게시물 목록
     */
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = postService.getAllPosts(pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 카테고리별 게시물을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param page       페이지 번호
     * @return 페이지네이션된 카테고리별 게시물 목록
     */
    @GetMapping(params = "category")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(
        @RequestParam Long categoryId,
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = postService.getPostsByCategory(categoryId,
            pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 게시물을 수정합니다.
     *
     * @param postId               수정할 게시물 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @param userDetails          현재 인증된 사용자 정보
     * @return 수정된 게시물 응답 DTO
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable Long postId,
        @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.updatePost(postId, updatePostRequestDto,
            userDetails.getUser());
        return ResponseEntity.ok(postResponseDto);
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param postId      삭제할 게시물 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok().body("게시물을 삭제했습니다.");
    }
}