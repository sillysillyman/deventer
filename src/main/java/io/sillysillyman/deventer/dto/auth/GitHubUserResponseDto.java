package io.sillysillyman.deventer.dto.auth;

import lombok.Getter;

@Getter
public class GitHubUserResponseDto {

    private String login;
    private Long id;
    private String avatar_url;
    private String url;
    private String email;
    private String username;
}