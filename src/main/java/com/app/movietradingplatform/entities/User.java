package com.app.movietradingplatform.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
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
    public User(String username) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.registrationDate = LocalDate.now();
        this.ownedMovies = new ArrayList<>();
    }
    public User(String username, LocalDate registrationDate, List<Movie> ownedMovies) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.registrationDate = registrationDate;
        this.ownedMovies = ownedMovies;
    }

    public void addMovie(Movie movie) {
        this.ownedMovies.add(movie);
    }
}