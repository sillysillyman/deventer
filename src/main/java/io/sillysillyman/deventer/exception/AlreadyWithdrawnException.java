package io.sillysillyman.deventer.exception;

public class AlreadyWithdrawnException extends RuntimeException {

    public AlreadyWithdrawnException(String message) {
        super(message);
    }
}