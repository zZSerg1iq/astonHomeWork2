package ru.zinoviev.resthomework.db.dao.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLike {

    private long id;
    private User user;
    private Film film;
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLike userLike = (UserLike) o;
        return Objects.equals(user, userLike.user) && Objects.equals(film, userLike.film) && Objects.equals(comment, userLike.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, film, comment);
    }
}
