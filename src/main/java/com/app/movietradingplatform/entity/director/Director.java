package com.app.movietradingplatform.entity.director;

import com.app.movietradingplatform.entity.movie.Movie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    public void addMovie(Movie movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
        }
    }
}