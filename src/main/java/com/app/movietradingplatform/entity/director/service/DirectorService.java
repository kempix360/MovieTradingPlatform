package com.app.movietradingplatform.entity.director.service;

import com.app.movietradingplatform.entity.director.Director;
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
        directors.add(Director.builder()
                .id(UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4e5b6b"))
                .name("Sean Baker")
                .description("Known for his independent films with a focus on marginalized communities.")
                .movies(List.of(
                        new Movie("The Florida Project", LocalDate.of(2017, 10, 6)),
                        new Movie("Tangerine", LocalDate.of(2015, 7, 10))
                ))
                .build());

        directors.add(Director.builder()
                .id(UUID.fromString("2b3c4d5e-6f7a-8b9c-0d1e-2f3e4d5c6a7c"))
                .name("Greta Gerwig")
                .description("Acclaimed filmmaker and actress known for her work in coming-of-age films.")
                .movies(List.of(
                        new Movie("Lady Bird", LocalDate.of(2017, 11, 3)),
                        new Movie("Little Women", LocalDate.of(2019, 12, 25)),
                        new Movie("Barbie", LocalDate.of(2023, 7, 21))
                ))
                .build());

        directors.add(Director.builder()
                .id(UUID.fromString("3c4d5e6f-7a8b-9c0d-1e2f-3f4a5b6c7d8e"))
                .name("Denis Villeneuve")
                .description("Renowned for his visually stunning and thought-provoking films.")
                .movies(List.of(
                        new Movie("Arrival", LocalDate.of(2016, 9, 1)),
                        new Movie("Blade Runner 2049", LocalDate.of(2017, 10, 6)),
                        new Movie("Dune", LocalDate.of(2021, 10, 22))
                ))
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

    public Director update(Director director) {
        if (director.getId() == null) {
            director.setId(UUID.randomUUID());
        }

        // Remove existing director first
        boolean directorRemoved = directors.removeIf(d -> d.getId().equals(director.getId()));
        if (!directorRemoved) {
            throw new NoSuchElementException("Director not found with ID: " + director.getId());
        }

        directors.add(director);
        return director;
    }

    public boolean delete(UUID id) {
        return directors.removeIf(director -> director.getId().equals(id));
    }
}