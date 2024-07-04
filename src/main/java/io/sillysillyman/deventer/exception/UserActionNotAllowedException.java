package io.sillysillyman.deventer.exception;

import io.sillysillyman.deventer.enums.UserActionError;

public class UserActionNotAllowedException extends RuntimeException {

    public UserActionNotAllowedException(UserActionError userActionError) {
        super(userActionError.getMessage());
    }
}