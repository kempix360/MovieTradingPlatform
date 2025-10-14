package com.app.movietradingplatform.controller.servlet;

import com.app.movietradingplatform.entity.User;
import com.app.movietradingplatform.service.AvatarService;
import com.app.movietradingplatform.service.UserService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ApiServlet", urlPatterns = "/api/*")
@MultipartConfig
public class ApiServlet extends HttpServlet {
    private final Jsonb jsonb = JsonbBuilder.create();
    private UserService userService;
    private AvatarService avatarService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        String dir = getServletContext().getInitParameter("avatarDir");
        try {
            avatarService = new AvatarService(dir);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    public static final class Patterns {
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F\\-]{36}");
        public static final Pattern USERS = Pattern.compile("/users/?");
        public static final Pattern USER = Pattern.compile("/users/(" + UUID.pattern() + ")/?");
        public static final Pattern USER_AVATAR = Pattern.compile("/users/(" + UUID.pattern() + ")/avatar/?");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        // GET /api/users
        if (path.matches(Patterns.USERS.pattern())) {
            resp.setContentType("application/json");
            jsonb.toJson(userService.getAll(), resp.getWriter());
            return;
        }

        // GET /api/users/{id}
        if (path.matches(Patterns.USER.pattern())) {
            UUID id = extractUuid(Patterns.USER, path);
            var user = userService.load(id);
            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                resp.setContentType("application/json");
                jsonb.toJson(user, resp.getWriter());
            }
            return;
        }

        // GET /api/users/{id}/avatar
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            if (!avatarService.exists(id.toString())) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.setContentType("image/png");
            try (InputStream in = avatarService.load(id.toString());
                 OutputStream out = resp.getOutputStream()) {
                in.transferTo(out);
            }
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown GET path: " + path);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        // POST /api/users - Create new user
        if (path.matches(Patterns.USERS.pattern())) {
            try {
                var user = jsonb.fromJson(req.getReader(), User.class);
                var createdUser = userService.save(user);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.setContentType("application/json");
                resp.setHeader("Location", req.getRequestURL() + "/" + createdUser.getId());
                jsonb.toJson(createdUser, resp.getWriter());
                return;
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                return;
            }
        }
        // POST /api/users/{id}/avatar - Create user's avatar
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            Part filePart = req.getPart("file");
            if (filePart == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file");
                return;
            }
            if (userService.getById(id) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
            avatarService.save(id.toString(), filePart.getInputStream());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        // PUT /api/users{id} - Update user
        if (path.matches(Patterns.USERS.pattern())) {
            try {
                var user = jsonb.fromJson(req.getReader(), User.class);
                var createdUser = userService.update(user);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setHeader("Location", req.getRequestURL() + "/" + createdUser.getId());
                jsonb.toJson(createdUser, resp.getWriter());
                return;
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }
        // PUT /api/users/{id}/avatar - Update user's avatar
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            Part filePart = req.getPart("file");
            if (filePart == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file");
                return;
            }
            if (userService.getById(id) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
            avatarService.update(id.toString(), filePart.getInputStream());
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        // DELETE /api/users/{id}
        if (path.matches(Patterns.USER.pattern())) {
            UUID id = extractUuid(Patterns.USER, path);
            if (userService.delete(id)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
            return;
        }

        // DELETE /api/users/{id}/avatar
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            if (avatarService.delete(id.toString())) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    // utils
    private static UUID extractUuid(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new IllegalArgumentException("Invalid path UUID");
    }
}
