package ru.zinoviev.resthomework.db.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class FilmDto {

    private long id;

    private String name;
    private String description;
    private String releaseDate;
    private int duration;

    private Set<UserLikeDto> userLikes;

    public FilmDto(long id, String name, String description, String releaseDate, int duration, Set<UserLikeDto> userLikes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.userLikes = userLikes;

        if (userLikes == null) {
            this.userLikes = new HashSet<>();
        }
    }

    public FilmDto() {
        this.userLikes = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDto filmDto = (FilmDto) o;
        return id == filmDto.id && duration == filmDto.duration && Objects.equals(name, filmDto.name) && Objects.equals(description, filmDto.description) && Objects.equals(releaseDate, filmDto.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }
}
