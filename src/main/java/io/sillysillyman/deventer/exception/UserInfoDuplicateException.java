package io.sillysillyman.deventer.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class UserInfoDuplicateException extends RuntimeException {

    private final List<String> messages;

    public UserInfoDuplicateException(List<String> messages) {
        this.messages = messages;
    }
}
