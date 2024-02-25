package ru.zinoviev.resthomework.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.zinoviev.resthomework.db.DBServiceManager;
import ru.zinoviev.resthomework.db.dto.FilmDto;
import ru.zinoviev.resthomework.db.service.FilmService;
import ru.zinoviev.resthomework.exception.CustomCRUDException;
import ru.zinoviev.resthomework.web.validator.FilmValidator;
import ru.zinoviev.resthomework.web.validator.UrlParamValidator;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "FilmServlet", urlPatterns = {"/films"})
public class FilmServlet extends HttpServlet {

    private final String INVALID_PATH_VARIABLE = "invalid path variable: ";
    private final String NOT_FOUND = "not found";
    private final String SUCCESS = "success";

    private final FilmService filmService;

    public FilmServlet(FilmService filmService) {
        this.filmService = filmService;
    }

    public FilmServlet() {
        filmService = DBServiceManager.getDefaultFilmService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contextPath = req.getServletPath();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String name = req.getParameter("name");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            if (contextPath.equals("/films")) {
                List<FilmDto> films = filmService.getAllFilms();
                json = objectMapper.writeValueAsString(films);
                resp.setStatus(HttpServletResponse.SC_OK);

            } else if (contextPath.equals("/films/film")) {
                if (UrlParamValidator.validateIdValueQuery(id)) {
                    FilmDto filmDto = filmService.getFilmById(Long.parseLong(id));
                    if (filmDto.getId() == 0) {
                        json = NOT_FOUND;
                    } else {
                        json = objectMapper.writeValueAsString(filmDto);
                    }
                    resp.setStatus(HttpServletResponse.SC_OK);

                } else if (UrlParamValidator.validateNameValueQuery(name)) {
                    List<FilmDto> films = filmService.findFilmByName(name);
                    if (films.size() == 0) {
                        json = NOT_FOUND;
                    } else {
                        json = objectMapper.writeValueAsString(films);
                    }
                    resp.setStatus(HttpServletResponse.SC_OK);

                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    json = INVALID_PATH_VARIABLE + id + " : " + name;
                }
            }
        } catch (CustomCRUDException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json = e.getMessage() + ":  " + id + " : " + name;
        }

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = null;

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        FilmDto filmDto = objectMapper.readValue(sb.toString(), FilmDto.class);

        try {
            FilmValidator.validate(filmDto);

            try {
                filmService.createFilm(filmDto);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                json = SUCCESS;
            } catch (CustomCRUDException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json = e.getMessage();
            }

        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json = e.getMessage();
        }

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = null;

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        FilmDto filmDto = objectMapper.readValue(sb.toString(), FilmDto.class);

        try {
            FilmValidator.validate(filmDto);

            try {
                filmService.updateFilm(filmDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                json = SUCCESS;
            } catch (CustomCRUDException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json = e.getMessage();
            }

        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json = e.getMessage();
        }

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contextPath = req.getServletPath();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            if (contextPath.equals("/films/film")) {
                if (UrlParamValidator.validateIdValueQuery(id)) {
                    filmService.deleteFilmById(Long.parseLong(id));
                    json = objectMapper.writeValueAsString("success");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    json = INVALID_PATH_VARIABLE + id;
                }
            }
        } catch (CustomCRUDException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json = e.getMessage();
        }

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

}
