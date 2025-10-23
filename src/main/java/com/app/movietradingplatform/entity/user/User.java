package com.app.movietradingplatform.entity.user;

import com.app.movietradingplatform.entity.movie.Movie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Serializable {
    private UUID id;
    private String username;
    private LocalDate registrationDate;
    private List<Movie> ownedMovies;

    public User(){
        this.id = UUID.randomUUID();
        this.username = "Unknown";
        this.registrationDate = LocalDate.now();
        this.ownedMovies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        if (!this.ownedMovies.contains(movie)) {
            this.ownedMovies.add(movie);
        }
    }
}