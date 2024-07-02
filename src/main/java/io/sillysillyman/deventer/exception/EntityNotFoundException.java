package io.sillysillyman.deventer.exception;

import io.sillysillyman.deventer.enums.NotFoundEntity;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(NotFoundEntity notFoundEntity) {
        super(notFoundEntity.getMessage());
    }
}