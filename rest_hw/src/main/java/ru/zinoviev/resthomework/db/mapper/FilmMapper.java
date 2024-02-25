package ru.zinoviev.resthomework.db.mapper;

import ru.zinoviev.resthomework.db.dao.entity.Film;
import ru.zinoviev.resthomework.db.dao.entity.UserLike;
import ru.zinoviev.resthomework.db.dto.FilmDto;
import ru.zinoviev.resthomework.db.dto.UserLikeDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FilmMapper {

    public List<FilmDto> listEntityToListDto(List<Film> filmList) {
        List<FilmDto> filmDtoList = new ArrayList<>();

        for (Film film : filmList) {
            filmDtoList.add(entityToDto(film));
        }

        return filmDtoList;
    }

    public Film dtoToEntity(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setDuration(filmDto.getDuration());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date releaseDate = format.parse(filmDto.getReleaseDate());
            film.setReleaseDate(releaseDate);
        } catch (ParseException e) {
            // Обработка ошибки
            e.printStackTrace();
        }

        return film;
    }

    public FilmDto entityToDto(Film film) {
        FilmDto filmDto = new FilmDto();
        if (film.getId() < 1) {
            return filmDto;
        }

        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setDuration(film.getDuration());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        filmDto.setReleaseDate(sdf.format(film.getReleaseDate()));

        if (film.getUserLikes() != null && film.getUserLikes().size() > 0) {
            filmDto.setUserLikes(userLikesToUserLikesDto(film.getUserLikes()));
        }

        return filmDto;
    }

    public Set<UserLikeDto> userLikesToUserLikesDto(List<UserLike> userLikes) {
        Set<UserLikeDto> userLikesDto = new HashSet<>();

        for (UserLike like : userLikes) {
            UserLikeDto likeDto = new UserLikeDto();
            likeDto.setId(like.getId());
            likeDto.setComment(like.getComment());

            if (like.getUser() != null) {
                likeDto.setUserId(like.getUser().getId());
                likeDto.setUserLogin(like.getUser().getLogin());
            }

            userLikesDto.add(likeDto);
        }

        return userLikesDto;
    }
}
