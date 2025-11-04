package com.app.movietradingplatform.entity.director;

import com.app.movietradingplatform.entity.movie.Movie;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Director implements Serializable {
    private UUID id;
    private String name;
    private String description;
    @Builder.Default
    private List<Movie> movies = new ArrayList<>();

    public Director() {
        this.id = UUID.randomUUID();
        this.name = "Unknown";
        this.description = "";
    }
    public void addMovie(Movie movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
        }
    }
}