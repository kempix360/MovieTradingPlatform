package com.app.movietradingplatform.controller.servlet;

import com.app.movietradingplatform.entity.director.controller.DirectorController;
import com.app.movietradingplatform.entity.user.controller.UserController;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ApiServlet", urlPatterns = "/api/*")
@MultipartConfig
public class ApiServlet extends HttpServlet {
    @Inject
    private UserController userController;
    @Inject
    private DirectorController directorController;

    public static final class Patterns {
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F\\-]{36}");

        public static final Pattern USERS = Pattern.compile("/users/?");
        public static final Pattern USER = Pattern.compile("/users/(" + UUID.pattern() + ")/?");
        public static final Pattern USER_AVATAR = Pattern.compile("/users/(" + UUID.pattern() + ")/avatar/?");
        public static final Pattern USER_MOVIES = Pattern.compile("/users/(" + UUID.pattern() + ")/movies/?");
        public static final Pattern USER_SINGLE_MOVIE = Pattern.compile("/users/(" + UUID.pattern() + ")" +
                "/movies/(" + UUID.pattern() + ")/?");

        public static final Pattern DIRECTORS = Pattern.compile("/users/?");
        public static final Pattern DIRECTOR = Pattern.compile("/users/(" + UUID.pattern() + ")/?");
    }

    public static UUID extractUuid(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new IllegalArgumentException("Invalid path UUID");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        if (path.matches(Patterns.USERS.pattern())) {
            userController.getAllUsers(resp);
        }
        if (path.matches(Patterns.USER.pattern())) {
            userController.getUserById(extractUuid(Patterns.USER, path), resp);
        }
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            userController.getAvatar(extractUuid(Patterns.USER_AVATAR, path), resp);
        }

        if (path.matches(Patterns.DIRECTORS.pattern())) {
            directorController.getAllDirectors(resp);
            return;
        }
        if (path.matches(Patterns.DIRECTOR.pattern())) {
            directorController.getDirectorById(path.substring(1), resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown GET path: " + path);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        if (path.matches(Patterns.USERS.pattern())) {
            userController.createUser(req, resp);
        }
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            userController.saveAvatar(extractUuid(Patterns.USER_AVATAR, path), req, resp);
            return;
        }

        if (path.matches(Patterns.DIRECTORS.pattern())) {
            directorController.createDirector(req.getReader().readLine(), resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        if (path.matches(Patterns.USERS.pattern())) {
            userController.updateUser(req, resp);
        }
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            userController.updateAvatar(extractUuid(Patterns.USER_AVATAR, path), req, resp);
            return;
        }

        if (path.matches(Patterns.DIRECTORS.pattern())) {
            directorController.updateDirector(req.getReader().readLine(), resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        if (path.matches(Patterns.USER.pattern())) {
            userController.deleteUser(extractUuid(Patterns.USER, path), resp);
        }
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            userController.deleteAvatar(extractUuid(Patterns.USER_AVATAR, path), resp);
            return;
        }

        if (path.matches(Patterns.DIRECTOR.pattern())) {
            directorController.deleteDirector(path.substring(1), resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}