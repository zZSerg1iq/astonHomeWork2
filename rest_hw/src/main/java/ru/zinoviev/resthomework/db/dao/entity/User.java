package ru.zinoviev.resthomework.db.dao.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private long id;

    private String name;

    private String login;

    private UserData userData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(login, user.login) && Objects.equals(userData, user.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, userData);
    }
}
