package ru.zinoviev.resthomework.db.dao.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserData {

    private long id;
    private String address;
    private String email;
    private String telegramName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(address, userData.address) && Objects.equals(email, userData.email) && Objects.equals(telegramName, userData.telegramName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, email, telegramName);
    }
}
