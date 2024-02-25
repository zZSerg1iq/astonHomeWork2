package ru.zinoviev.resthomework.exception;

public enum CrudExceptionMessages {
    CREATION_ERROR("creation error"),
    GET_ERROR("get error"),
    UPDATE_ERROR("update error"),
    DELETE_ERROR("delete error"),
    CONNECTION_ERROR("db connection error"),
    QUERY_ERROR("query error")

    ;
    private String message;

    CrudExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
