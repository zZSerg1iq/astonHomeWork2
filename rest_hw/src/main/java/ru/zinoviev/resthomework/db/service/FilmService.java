package ru.zinoviev.resthomework.db.service;

import ru.zinoviev.resthomework.db.dto.FilmDto;
import ru.zinoviev.resthomework.db.dto.UserLikeDto;

import java.util.List;

public interface FilmService {

    void createFilm(FilmDto film);

    FilmDto getFilmById(long filmId);

    List<FilmDto> findFilmByName(String filmName);

    List<UserLikeDto> getFilmUserLikes(long filmId, int limit, int offset);

    void addUserLike(long filmId, long userId, String comment);

    List<FilmDto> getAllFilms();

    void updateFilm(FilmDto film);

    void deleteFilmById(long filmId);
}
