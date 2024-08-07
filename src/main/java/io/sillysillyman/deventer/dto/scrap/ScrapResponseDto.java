package io.sillysillyman.deventer.dto.scrap;

import io.sillysillyman.deventer.entity.Post;
import lombok.Getter;

@Getter
public class ScrapResponseDto {

    private final Long postId;
    private final String title;

    public ScrapResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
    }
}