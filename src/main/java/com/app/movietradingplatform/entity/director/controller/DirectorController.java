package com.app.movietradingplatform.entity.director.controller;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@ApplicationScoped
public class DirectorController {
    @Inject
    private DirectorService directorService;

    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public DirectorController() {
    }

    public void getAllDirectors(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        jsonb.toJson(directorService.getAll(), resp.getWriter());
    }

    public void getDirectorById(UUID id, HttpServletResponse resp) throws IOException {
        Director director = directorService.getById(id);
        if (director == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Director not found");
        } else {
            resp.setContentType("application/json");
            jsonb.toJson(director, resp.getWriter());
        }
    }

    public void createDirector(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Director director = jsonb.fromJson(req.getReader(), Director.class);
            Director createdDirector = directorService.save(director);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            jsonb.toJson(createdDirector, resp.getWriter());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void updateDirector(UUID id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Director director = jsonb.fromJson(req.getReader(), Director.class);
            Director updatedDirector = directorService.update(id, director);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(updatedDirector, resp.getWriter());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void deleteDirector(UUID id, HttpServletResponse resp) throws IOException {
        if (directorService.delete(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Director not found");
        }
    }
}