package io.sillysillyman.deventer.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {

    @NotBlank(message = "제목을 입력해야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해야 합니다.")
    private String content;

    @NotBlank(message = "카테고리 주제를 입력해야 합니다.")
    private String categoryTopic;
}