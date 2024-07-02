package io.sillysillyman.deventer.exception;

import io.sillysillyman.deventer.enums.UserActionError;

public class MismatchStatusException extends RuntimeException {

    public MismatchStatusException(UserActionError userActionError) {
        super(userActionError.getMessage());
    }
}
