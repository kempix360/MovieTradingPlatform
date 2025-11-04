package com.app.movietradingplatform.entity.director.service;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class DirectorService {
    private final List<Director> directors = new ArrayList<>();

    @Inject
    public DirectorService() {
        // Load test directors
        List<Movie> movies = new ArrayList<>();
        movies.add(
            new Movie(
                "The Florida Project",
                LocalDate.of(2017, 10, 6),
                List.of(Genre.DRAMA, Genre.COMEDY)
            ));
        movies.add(
            new Movie(
                "Tangerine",
                LocalDate.of(2015, 7, 10),
                List.of(Genre.DRAMA, Genre.COMEDY, Genre.ADVENTURE)
            ));
        directors.add(Director.builder()
                .id(UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4e5b6b"))
                .name("Sean Baker")
                .description("Known for his independent films with a focus on marginalized communities.")
                .movies(movies)
                .build());

        directors.add(Director.builder()
                .id(UUID.fromString("2b3c4d5e-6f7a-8b9c-0d1e-2f3e4d5c6a7c"))
                .name("Greta Gerwig")
                .description("Acclaimed filmmaker and actress known for her work in coming-of-age films.")
                .movies(movies)
                .build());

        directors.add(Director.builder()
                .id(UUID.fromString("3c4d5e6f-7a8b-9c0d-1e2f-3f4a5b6c7d8e"))
                .name("Denis Villeneuve")
                .description("Renowned for his visually stunning and thought-provoking films.")
                .movies(movies)
                .build());
    }

    public List<Director> getAll() {
        return new ArrayList<>(directors);
    }

    public Director getById(UUID id) {
        return directors.stream()
                .filter(director -> director.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Director save(Director director) {
        if (director.getId() == null) {
            director.setId(UUID.randomUUID());
        }
        directors.add(director);
        return director;
    }

    public Director update(UUID id, Director director) {
        if (director.getId() == null) {
            director.setId(id);
        }

        Director existing = getById(id);

        if (existing == null) {
            // create new if not found
            director.setId(id);
            directors.add(director);
            return director;
        }

        existing.setName(director.getName());
        existing.setDescription(director.getDescription());
        existing.setMovies(director.getMovies());
        return existing;
    }

    public boolean delete(UUID id) {
        return directors.removeIf(director -> director.getId().equals(id));
    }

    public void deleteAll() {
        directors.clear();
    }

    public List<Movie> getMovies(UUID directorId) {
        Director d = getById(directorId);
        if (d == null) throw new NoSuchElementException("Director not found: " + directorId);
        return d.getMovies();
    }

    public Movie getMovie(UUID directorId, UUID movieId) {
        return getMovies(directorId).stream()
                .filter(m -> m.getId().equals(movieId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + movieId));
    }

    public Movie createMovie(UUID directorId, Movie movie) {
        Director d = getById(directorId);
        if (d == null) throw new NoSuchElementException("Director not found: " + directorId);
        movie.setId(UUID.randomUUID());
        d.getMovies().add(movie);
        return movie;
    }

    public Movie updateMovie(UUID directorId, UUID movieId, Movie updated) {
        Movie movie = getMovie(directorId, movieId);
        movie.setTitle(updated.getTitle());
        movie.setReleaseDate(updated.getReleaseDate());
        return movie;
    }

    public void deleteMovie(UUID directorId, UUID movieId) {
        Director d = getById(directorId);
        if (d == null) throw new NoSuchElementException("Director not found: " + directorId);
        d.getMovies().removeIf(m -> m.getId().equals(movieId));
    }
}