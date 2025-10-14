package com.app.movietradingplatform.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Director implements Serializable {
    private UUID id;
    private String name;
    private String description;
    private List<Movie> movies;

    public Director() {
        this.id = UUID.randomUUID();
        this.name = "Unknown";
        this.description = "";
        this.movies = new ArrayList<>();
    }
    public Director(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.movies = new ArrayList<>();
    }
    public Director(String name, String description, List<Movie> movies) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.movies = movies;
    }
    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }
}