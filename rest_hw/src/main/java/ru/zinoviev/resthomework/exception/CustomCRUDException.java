package ru.zinoviev.resthomework.exception;

public class CustomCRUDException extends RuntimeException {

    public CustomCRUDException(String message) {
        super(message);
    }

    public CustomCRUDException(String message, Throwable cause) {
        super(message, cause);
    }

}
