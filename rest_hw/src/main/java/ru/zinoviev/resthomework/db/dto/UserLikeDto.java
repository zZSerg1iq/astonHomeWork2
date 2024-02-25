package ru.zinoviev.resthomework.db.dto;

import lombok.Data;

@Data
public class UserLikeDto {

    private long id;
    private long userId = 0;
    private String userLogin;
    private String comment;

}
