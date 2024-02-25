package ru.zinoviev.resthomework.db.dao.repository;

import ru.zinoviev.resthomework.db.dao.entity.Film;
import ru.zinoviev.resthomework.db.dao.entity.UserLike;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    void createFilm(Film film);

    Optional<Film> getFilmById(long filmId);

    List<Film> findFilmByName(String filmName);

    List<UserLike> getFilmUserLikes(long filmId, int limit, int offset);

    void addUserLike(long filmId, long userId, String comment);

    List<Film> getAllFilms();

    void updateFilm(Film film);

    void deleteFilmById(long filmId);
}
