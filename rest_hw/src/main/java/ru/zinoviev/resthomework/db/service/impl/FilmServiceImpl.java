package ru.zinoviev.resthomework.db.service.impl;

import ru.zinoviev.resthomework.db.dao.entity.Film;
import ru.zinoviev.resthomework.db.dao.repository.FilmRepository;
import ru.zinoviev.resthomework.db.dto.FilmDto;
import ru.zinoviev.resthomework.db.dto.UserLikeDto;
import ru.zinoviev.resthomework.db.mapper.FilmMapper;
import ru.zinoviev.resthomework.db.service.FilmService;

import java.util.ArrayList;
import java.util.List;

public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public void createFilm(FilmDto film) {
        filmRepository.createFilm(new FilmMapper().dtoToEntity(film));
    }

    @Override
    public FilmDto getFilmById(long filmId) {
        return new FilmMapper().entityToDto(filmRepository.getFilmById(filmId).orElseGet(Film::new));
    }

    @Override
    public List<FilmDto> findFilmByName(String filmName) {
        if (filmName == null) {
            return new ArrayList<>();
        }
        return new FilmMapper().listEntityToListDto(filmRepository.findFilmByName(filmName));
    }

    @Override
    public List<UserLikeDto> getFilmUserLikes(long filmId, int limit, int offset) {
        return new ArrayList<>(new FilmMapper().userLikesToUserLikesDto(
                filmRepository.getFilmUserLikes(filmId, limit, offset)
        ));
    }

    @Override
    public void addUserLike(long filmId, long userId, String comment) {
        filmRepository.addUserLike(filmId, userId, comment);
    }

    @Override
    public List<FilmDto> getAllFilms() {
        return new FilmMapper().listEntityToListDto(filmRepository.getAllFilms());
    }

    @Override
    public void updateFilm(FilmDto film) {
        filmRepository.updateFilm(new FilmMapper().dtoToEntity(film));
    }

    @Override
    public void deleteFilmById(long filmId) {
        filmRepository.deleteFilmById(filmId);
    }
}
