package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.dto.post.PostResponseDto;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.Scrap;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.NotFoundEntity;
import io.sillysillyman.deventer.enums.UserActionError;
import io.sillysillyman.deventer.exception.EntityNotFoundException;
import io.sillysillyman.deventer.exception.UserActionNotAllowedException;
import io.sillysillyman.deventer.repository.PostRepository;
import io.sillysillyman.deventer.repository.ScrapRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;

    /**
     * 게시물에 스크랩을 토글합니다. 이미 스크랩된 게시물은 스크랩이 취소되고, 스크랩되지 않은 게시물은 스크랩이 추가됩니다.
     *
     * @param postId 스크랩할 게시물의 ID
     * @param user   현재 인증된 사용자
     * @return 스크랩이 완료되었는지 여부 메시지
     */
    @Transactional
    public String toggleScrap(Long postId, User user) {
        Post post = getPostByIdOrThrow(postId);

        validateOwnership(post, user);

        Optional<Scrap> optionalScrap = scrapRepository.findByUserAndPost(user, post);
        if (optionalScrap.isEmpty()) {
            Scrap scrap = new Scrap(user, post);
            scrapRepository.save(scrap);
            return "게시물(Id: " + postId + ")의 스크랩이 완료되었습니다.";
        } else {
            scrapRepository.delete(optionalScrap.get());
            return "게시물(Id: " + postId + ")의 스크랩이 취소되었습니다.";
        }
    }

    /**
     * 사용자가 스크랩한 게시물들을 생성일자 기준으로 조회합니다.
     *
     * @param user     현재 인증된 사용자
     * @param pageable 페이지 정보
     * @return 스크랩한 게시물 목록 (생성일자 순)
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getScrappedPostsByCreatedAt(User user, Pageable pageable) {
        Page<Scrap> scraps = scrapRepository.findAllByUserOrderByPostCreatedAtDesc(user, pageable);
        List<PostResponseDto> scrappedPosts = scraps.stream()
            .map(scrap -> new PostResponseDto(scrap.getPost()))
            .toList();
        return new PageImpl<>(scrappedPosts, pageable, scraps.getTotalElements());
    }

    /**
     * 사용자가 스크랩한 게시물들을 작성자 닉네임 기준으로 조회합니다.
     *
     * @param user     현재 인증된 사용자
     * @param pageable 페이지 정보
     * @return 스크랩한 게시물 목록 (작성자 닉네임 순)
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getScrappedPostsByNickname(User user, Pageable pageable) {
        Page<Scrap> scraps = scrapRepository.findAllByUserOrderByPostUserNicknameAsc(user,
            pageable);
        List<PostResponseDto> scrappedPosts = scraps.stream()
            .map(scrap -> new PostResponseDto(scrap.getPost()))
            .toList();
        return new PageImpl<>(scrappedPosts, pageable, scraps.getTotalElements());
    }


    /**
     * 게시물 소유자가 현재 사용자와 일치하는지 확인합니다. 사용자가 자신의 게시물을 스크랩하지 못하도록 방지합니다.
     *
     * @param post 게시물 객체
     * @param user 현재 인증된 사용자
     */
    private void validateOwnership(Post post, User user) {
        if (post.getUser().getId().equals(user.getId())) {
            throw new UserActionNotAllowedException(UserActionError.SELF_ACTION_NOT_ALLOWED);
        }
    }

    /**
     * ID를 기준으로 게시물을 조회하고, 없을 경우 예외를 던집니다.
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 게시물
     */
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
    }
}