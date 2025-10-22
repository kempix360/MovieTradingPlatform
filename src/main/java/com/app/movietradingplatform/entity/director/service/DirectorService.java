package com.app.movietradingplatform.entity.director.service;

import com.app.movietradingplatform.entity.director.Director;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class DirectorService {
    private final List<Director> directors = new ArrayList<>();

    @Inject
    public DirectorService() {
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

    public Director load(UUID id) {
        return getById(id);
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