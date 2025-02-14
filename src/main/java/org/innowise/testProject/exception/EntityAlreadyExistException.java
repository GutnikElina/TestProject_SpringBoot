package org.innowise.testProject.exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
