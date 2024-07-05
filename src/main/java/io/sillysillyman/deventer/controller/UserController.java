package io.sillysillyman.deventer.controller;

import io.sillysillyman.deventer.dto.comment.CommentResponseDto;
import io.sillysillyman.deventer.dto.post.PostResponseDto;
import io.sillysillyman.deventer.dto.user.ChangePasswordRequestDto;
import io.sillysillyman.deventer.dto.user.ProfileResponseDto;
import io.sillysillyman.deventer.dto.user.UpdateProfileRequestDto;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.security.UserDetailsImpl;
import io.sillysillyman.deventer.service.CommentLikeService;
import io.sillysillyman.deventer.service.PostLikeService;
import io.sillysillyman.deventer.service.ScrapService;
import io.sillysillyman.deventer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private static final int PAGE_SIZE = 10;
    private final UserService userService;
    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;
    private final ScrapService scrapService;

    /**
     * 사용자의 프로필을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @return 프로필 응답 DTO
     */
    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        ProfileResponseDto profileResponseDto = userService.getProfile(user);
        return ResponseEntity.ok(profileResponseDto);
    }

    /**
     * 사용자의 모든 게시물을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 게시물 응답 DTO
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = userService.getAllPosts(user, pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 사용자의 모든 댓글을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 댓글 응답 DTO
     */
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommentResponseDto> commentResponseDtoPage = userService.getAllComments(user,
            pageable);
        return ResponseEntity.ok(commentResponseDtoPage);
    }

    /**
     * 사용자가 좋아요 한 게시물 목록을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 게시물 응답 DTO
     */
    @GetMapping("/likes/posts")
    public ResponseEntity<Page<PostResponseDto>> getLikedPosts(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return ResponseEntity.ok(postLikeService.getLikedEntities(user, pageable));
    }

    /**
     * 사용자가 좋아요 한 댓글 목록을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 댓글 응답 DTO
     */
    @GetMapping("/likes/comments")
    public ResponseEntity<Page<CommentResponseDto>> getLikedComments(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return ResponseEntity.ok(commentLikeService.getLikedEntities(user, pageable));
    }

    /**
     * 사용자가 스크랩한 게시물들을 생성일자 기준으로 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 스크랩한 게시물 응답 DTO
     */
    @GetMapping("/scraps/created-at")
    public ResponseEntity<Page<PostResponseDto>> getScrappedPostsByCreatedAt(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = scrapService.getScrappedPostsByCreatedAt(
            userDetails.getUser(), pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 사용자가 스크랩한 게시물들을 작성자 닉네임 기준으로 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 스크랩한 게시물 응답 DTO
     */
    @GetMapping("/scrap/nickname")
    public ResponseEntity<Page<PostResponseDto>> getScrappedPostsByNickname(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = scrapService.getScrappedPostsByNickname(
            userDetails.getUser(), pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 사용자의 프로필을 수정합니다.
     *
     * @param updateProfileRequestDto 프로필 수정 요청 DTO
     * @param userDetails             현재 인증된 사용자 정보
     * @return 수정된 프로필 응답 DTO
     */
    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(
        @Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        ProfileResponseDto profileResponseDto = userService.updateProfile(updateProfileRequestDto,
            user);
        return ResponseEntity.ok(profileResponseDto);
    }

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param changePasswordRequestDto 비밀번호 변경 요청 DTO
     * @param userDetails              현재 인증된 사용자 정보
     * @return 비밀번호 변경 완료 메시지
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
        @Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        userService.changePassword(changePasswordRequestDto, user);
        return ResponseEntity.ok().body("비밀번호를 변경했습니다.");
    }
}