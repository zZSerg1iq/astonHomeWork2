package ru.zinoviev.resthomework.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.zinoviev.resthomework.db.DBServiceManager;
import ru.zinoviev.resthomework.db.dto.UserDto;
import ru.zinoviev.resthomework.db.service.UserService;
import ru.zinoviev.resthomework.exception.CustomCRUDException;
import ru.zinoviev.resthomework.web.validator.UrlParamValidator;
import ru.zinoviev.resthomework.web.validator.UserValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {

    private final String INVALID_PATH_VARIABLE = "invalid path variable: ";
    private final String NOT_FOUND = "not found";
    private final String SUCCESS = "success";

    private final String ROOT_ENDPOINT = "/users";
    private final String CHILD_ENDPOINT = "/users/user";

    private final UserService userService;

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    public UserServlet() {
        userService = DBServiceManager.getDefaultUserService();
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
            if (contextPath.equals(ROOT_ENDPOINT)) {
                List<UserDto> users = userService.getAllUsers();
                json = objectMapper.writeValueAsString(users);
                resp.setStatus(HttpServletResponse.SC_OK);

            } else if (contextPath.equals(CHILD_ENDPOINT)) {
                if (UrlParamValidator.validateIdValueQuery(id)) {
                    UserDto userDto = userService.getUserById(Long.parseLong(id));
                    if (userDto.getId() == 0) {
                        json = NOT_FOUND;
                    } else {
                        json = objectMapper.writeValueAsString(userDto);
                    }
                    resp.setStatus(HttpServletResponse.SC_OK);

                } else if (UrlParamValidator.validateNameValueQuery(name)) {
                    List<UserDto> users = userService.findUserByName(name);
                    if (users.size() == 0) {
                        json = NOT_FOUND;
                    } else {
                        json = objectMapper.writeValueAsString(users);
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
        UserDto userDto = objectMapper.readValue(sb.toString(), UserDto.class);

        try {
            UserValidator.validate(userDto);
            try {
                userService.createUser(userDto);
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
        UserDto userDto = objectMapper.readValue(sb.toString(), UserDto.class);

        try {
            UserValidator.validate(userDto);

            try {
                userService.updateUser(userDto);
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
            if (contextPath.equals(CHILD_ENDPOINT)) {
                if (UrlParamValidator.validateIdValueQuery(id)) {
                    userService.deleteUserById(Long.parseLong(id));
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
