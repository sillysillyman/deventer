package io.sillysillyman.deventer.dto.post;

import io.sillysillyman.deventer.dto.comment.CommentResponseDto;
import io.sillysillyman.deventer.dto.post.PostResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostWithCommentsResponseDto {

    private final PostResponseDto postResponseDto;
    private final List<CommentResponseDto> commentResponseDtoList;
}