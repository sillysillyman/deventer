package io.sillysillyman.deventer.dto.auth;

import lombok.Getter;

@Getter
public class GitHubTokenResponseDto {

    private String access_token;
    private String scope;
    private String token_type;
}