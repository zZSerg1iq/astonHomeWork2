package ru.zinoviev.resthomework.web.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.zinoviev.resthomework.H2TestStarter;
import ru.zinoviev.resthomework.db.dto.FilmDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmServletTest extends H2TestStarter {

    private final String rootPath = "/films";
    private final String childPath = "/films/film";

    @Test
    public void getFilmTest() throws IOException {
        int id = 1;
        FilmDto basicFilm = filmService.getFilmById(id);

        FilmServlet filmServlet = new FilmServlet(filmService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);


        when(request.getMethod()).thenReturn("GET");
        when(request.getServletPath()).thenReturn(childPath);
        when(request.getRequestURI()).thenReturn(childPath);

        //получаем нормальный объект
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        StringWriter correctWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(correctWriter));
        filmServlet.doGet(request, response);

        //получаем что то из нала
        when(request.getParameter("id")).thenReturn(null);
        StringWriter nullWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(nullWriter));
        filmServlet.doGet(request, response);

        //получаем что то из фигни
        when(request.getParameter("id")).thenReturn("sdf23rwrew");
        StringWriter wrongWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(wrongWriter));
        filmServlet.doGet(request, response);

        FilmDto filmDto = new ObjectMapper().readValue(correctWriter.toString(), FilmDto.class);
        assertAll(
                () -> assertThat(basicFilm).isEqualTo(filmDto),
                () -> assertEquals("invalid path variable: null : null", nullWriter.toString()),
                () -> assertEquals("invalid path variable: null : null", nullWriter.toString())
        );
    }

    @Test
    public void getFilmListTest() throws IOException {
        int id = 1;
        List<FilmDto> basicList = filmService.getAllFilms();

        FilmServlet filmServlet = new FilmServlet(filmService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //получаем list
        when(request.getMethod()).thenReturn("GET");
        when(request.getServletPath()).thenReturn(rootPath);
        when(request.getRequestURI()).thenReturn(rootPath);
        StringWriter correctWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(correctWriter));
        filmServlet.doGet(request, response);
        List<FilmDto> filmDto = new ObjectMapper().readValue(correctWriter.toString(),
                new TypeReference<List<FilmDto>>() {});


        //получаем list по имени
        List<FilmDto> basicFilmByNameList = filmService.findFilmByName("По");
        when(request.getParameter("name")).thenReturn("По");
        when(request.getServletPath()).thenReturn(childPath);
        when(request.getRequestURI()).thenReturn(childPath);
        StringWriter byNameWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(byNameWriter));
        filmServlet.doGet(request, response);
        List<FilmDto> filmByNameDtoList = new ObjectMapper().readValue(byNameWriter.toString(),
                new TypeReference<List<FilmDto>>() {});

        assertAll(
                () -> assertThat(basicList).isEqualTo(filmDto),
                () -> assertThat(filmByNameDtoList).isEqualTo(basicFilmByNameList)
        );
    }

    @Test
    public void deleteFilmTest() throws IOException {
        int id = 1;
        FilmDto basicFilm = filmService.getFilmById(id);
        List<FilmDto> filmDtoList = filmService.getAllFilms();
        //удаляемый объект действительно есть
        assertAll(
                () -> assertNotNull(basicFilm),
                () -> assertNotNull(filmDtoList)
        );


        FilmServlet filmServlet = new FilmServlet(filmService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("DELETE");
        when(request.getServletPath()).thenReturn(childPath);
        when(request.getRequestURI()).thenReturn(childPath);

        //удаляем нормальный объект
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        filmServlet.doDelete(request, response);
        //забираем новый лист
        List<FilmDto> filmDtoList2 = filmService.getAllFilms();


        //удаляем null
        when(request.getParameter("id")).thenReturn(null);
        StringWriter nullWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(nullWriter));
        filmServlet.doDelete(request, response);


        //кошка прошла по клавиатуре
        when(request.getParameter("id")).thenReturn("89034dfvg27892fuwd");
        StringWriter wrongWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(wrongWriter));
        filmServlet.doDelete(request, response);


        assertAll(
                () -> assertTrue(filmDtoList2.size() < filmDtoList.size()),
                () -> assertFalse(filmDtoList2.contains(basicFilm)),
                () -> assertTrue(filmDtoList.contains(basicFilm)),
                () -> assertEquals("\"success\"", writer.toString()),
                () -> assertEquals("invalid path variable: null", nullWriter.toString()),
                () -> assertEquals("invalid path variable: 89034dfvg27892fuwd", wrongWriter.toString())
        );
    }


    @Test
    public void updateFilmTest() throws IOException {
        FilmServlet filmServlet = new FilmServlet(filmService);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        //получаем, изменяем, записываем
        FilmDto filmDtoUpdated = filmService.getFilmById(2);
        filmDtoUpdated.setName(filmDtoUpdated.getName() + " updated");
        filmDtoUpdated.setDescription(filmDtoUpdated.getDescription() + " updated");
        String filmDtoJson = new ObjectMapper().writeValueAsString(filmDtoUpdated);

        StringWriter writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new StringReader(filmDtoJson));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getReader()).thenReturn(reader);
        filmServlet.doPut(request, response);


        //пытаемся записать невалидный объект
        FilmDto nullableFilm = new FilmDto();
        String nullableFilmDtoJson = new ObjectMapper().writeValueAsString(nullableFilm);
        StringWriter nullableWriter = new StringWriter();
        BufferedReader nullableReader = new BufferedReader(new StringReader(nullableFilmDtoJson));

        when(response.getWriter()).thenReturn(new PrintWriter(nullableWriter));
        when(request.getReader()).thenReturn(nullableReader);
        filmServlet.doPut(request, response);


        //пытаемся записать объект с несуществующим id
        FilmDto wrongFilm = filmService.getFilmById(5);
        wrongFilm.setId(123123);
        String wrongFilmDtoJson = new ObjectMapper().writeValueAsString(wrongFilm);

        StringWriter wrongWriter = new StringWriter();
        BufferedReader wrongReader = new BufferedReader(new StringReader(wrongFilmDtoJson));

        when(response.getWriter()).thenReturn(new PrintWriter(wrongWriter));
        when(request.getReader()).thenReturn(wrongReader);
        filmServlet.doPut(request, response);

        assertAll(
                () -> assertEquals("success", writer.toString()),
                () -> assertEquals(filmDtoUpdated, filmService.getFilmById(2)),
                () -> assertEquals("Object validation exception: name:<notNull,notBlank>, duration: > 0, ReleaseDate: <notNull>", nullableWriter.toString()),
                () -> assertEquals("update error", wrongWriter.toString())
        );
    }

    @Test
    public void createFilmTest() throws IOException {
        FilmServlet filmServlet = new FilmServlet(filmService);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        //создаем, записываем
        FilmDto filmDto = new FilmDto();
        filmDto.setDuration(10);
        filmDto.setDescription("lalalend");
        filmDto.setReleaseDate("2000-01-10");
        filmDto.setName("lalalalalalala");
        String filmDtoJson = new ObjectMapper().writeValueAsString(filmDto);

        StringWriter writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new StringReader(filmDtoJson));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getReader()).thenReturn(reader);
        filmServlet.doPost(request, response);


        //пытаемся записать невалидный объект
        FilmDto nullableFilm = new FilmDto();
        String nullableFilmDtoJson = new ObjectMapper().writeValueAsString(nullableFilm);
        StringWriter nullableWriter = new StringWriter();
        BufferedReader nullableReader = new BufferedReader(new StringReader(nullableFilmDtoJson));

        when(response.getWriter()).thenReturn(new PrintWriter(nullableWriter));
        when(request.getReader()).thenReturn(nullableReader);
        filmServlet.doPost(request, response);

        assertAll(
                () -> assertEquals("success", writer.toString()),
                () -> assertEquals("Object validation exception: name:<notNull,notBlank>, duration: > 0, ReleaseDate: <notNull>", nullableWriter.toString())
        );
    }

}
