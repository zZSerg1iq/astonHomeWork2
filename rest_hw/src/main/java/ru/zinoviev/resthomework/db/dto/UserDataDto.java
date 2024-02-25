package ru.zinoviev.resthomework.db.dto;

import lombok.Data;

@Data
public class UserDataDto {

    private long id;
    private String address;
    private String email;
    private String telegramName;

}
