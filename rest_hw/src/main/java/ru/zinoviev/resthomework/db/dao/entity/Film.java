package ru.zinoviev.resthomework.db.dao.entity;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Film {

    private long id;
    private String name;
    private String description;
    private Date releaseDate;
    private int duration;

    private List<UserLike> userLikes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate) && Objects.equals(userLikes, film.userLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration, userLikes);
    }
}
