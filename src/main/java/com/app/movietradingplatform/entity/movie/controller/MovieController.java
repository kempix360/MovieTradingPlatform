package com.app.movietradingplatform.entity.movie.controller;

import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@ApplicationScoped
public class MovieController {
    @Inject
    private MovieService movieService;

    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public MovieController() {
    }

    public void getAllMovies(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        jsonb.toJson(movieService.getAll(), resp.getWriter());
    }

    public void getMovieById(UUID id, HttpServletResponse resp) throws IOException {
        Movie movie = movieService.getById(id);
        if (movie == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "movie not found");
        } else {
            resp.setContentType("application/json");
            jsonb.toJson(movie, resp.getWriter());
        }
    }

    public void createMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Movie movie = jsonb.fromJson(req.getReader(), Movie.class);
            Movie createdMovie = movieService.save(movie);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setHeader("Location", req.getRequestURL() + "/" + createdMovie.getId());
            jsonb.toJson(createdMovie, resp.getWriter());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void updateMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Movie movie = jsonb.fromJson(req.getReader(), Movie.class);
            Movie updatedmovie = movieService.update(movie);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(updatedmovie, resp.getWriter());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void deleteMovie(UUID id, HttpServletResponse resp) throws IOException {
        if (movieService.delete(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "movie not found");
        }
    }
}