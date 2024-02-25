package ru.zinoviev.resthomework.web.validator;

import ru.zinoviev.resthomework.db.dto.FilmDto;

import java.io.IOException;

public class FilmValidator {

    public static void validate(FilmDto filmDto) throws IOException {
        if (filmDto.getName() == null || filmDto.getName().equals("")
                || filmDto.getDuration() <= 0
                || filmDto.getReleaseDate() == null) {
            throw new IOException("Object validation exception: name:<notNull,notBlank>, duration: > 0, ReleaseDate: <notNull>");
        }
    }

}
