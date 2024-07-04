package io.sillysillyman.deventer.entity.like;

import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_likes")
public class PostLike extends Like {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostLike(User user, Post post) {
        super(user);
        this.post = post;
    }
}