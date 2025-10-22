package com.app.movietradingplatform.entity.user.controller;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import com.app.movietradingplatform.service.AvatarService;
import com.app.movietradingplatform.utils.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;

import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@ApplicationScoped
public class UserController {
    @Inject
    private UserService userService;
    @Inject
    private AvatarService avatarService;

    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public UserController() {
    }

    public void getAllUsers(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        jsonb.toJson(userService.getAll(), resp.getWriter());
    }

    public void getUserById(UUID id, HttpServletResponse resp) throws IOException {
        User user = userService.getById(id);
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } else {
            resp.setContentType("application/json");
            jsonb.toJson(user, resp.getWriter());
        }
    }

    public void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = jsonb.fromJson(req.getReader(), User.class);
            User createdUser = userService.save(user);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setHeader("Location", req.getRequestURL() + "/" + createdUser.getId());
            jsonb.toJson(createdUser, resp.getWriter());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = jsonb.fromJson(req.getReader(), User.class);
            User updatedUser = userService.update(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(updatedUser, resp.getWriter());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void deleteUser(UUID id, HttpServletResponse resp) throws IOException {
        if (userService.delete(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    public void getAvatar(UUID id, HttpServletResponse resp) throws IOException {
        if (!avatarService.exists(id)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Avatar not found");
            return;
        }
        resp.setContentType("image/png");
        try (InputStream in = avatarService.load(id);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void getMovies(UUID id, HttpServletResponse resp) throws IOException {
        resp.setContentType("image/png");
        try (InputStream in = avatarService.load(id);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void saveAvatar(UUID id, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file");
            return;
        }
        if (userService.getById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        avatarService.save(id, filePart.getInputStream());
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    public void updateAvatar(UUID id, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file");
            return;
        }
        if (userService.getById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        avatarService.update(id, filePart.getInputStream());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    public void deleteAvatar(UUID id, HttpServletResponse resp) throws IOException {
        if (!avatarService.delete(id)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Avatar not found");
        } else {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}