package com.app.movietradingplatform.entity.movie.service;

import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class MovieService {
    private final List<Movie> movies = new ArrayList<>();

    @Inject
    public MovieService() {
        // Load test movies
        movies.add(Movie.builder()
                .id(UUID.fromString("1a2b3c4d-5e6f-7d8c-9c0d-1e6f3a4e5b6b"))
                .title("Short Term 12")
                .build());

        movies.add(Movie.builder()
                .id(UUID.fromString("1c4c5e62-7a8b-9c0d-1e2f-3f5a5b6c7d8e"))
                .title("Pulp Fiction")
                .build());
    }

    public List<Movie> getAll() {
        return new ArrayList<>(movies);
    }

    public Movie getById(UUID id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Movie save(Movie movie) {
        if (movie.getId() == null) {
            movie.setId(UUID.randomUUID());
        }
        movies.add(movie);
        return movie;
    }

    public Movie update(Movie movie) {
        if (movie.getId() == null) {
            movie.setId(UUID.randomUUID());
        }

        // Remove existing movie first
        boolean movieRemoved = movies.removeIf(d -> d.getId().equals(movie.getId()));
        if (!movieRemoved) {
            throw new NoSuchElementException("Movie not found with ID: " + movie.getId());
        }

        movies.add(movie);
        return movie;
    }

    public boolean delete(UUID id) {
        return movies.removeIf(movie -> movie.getId().equals(id));
    }
}