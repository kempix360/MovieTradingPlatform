package com.app.movietradingplatform.entities;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

//@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@NoArgsConstructor
public class User implements Serializable {
    private UUID id;
    private String username;
    private LocalDate registrationDate;
    @JsonManagedReference
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
    public User(UUID id, String username) {
        this.id = id;
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

    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void addMovie(Movie movie) {
        this.ownedMovies.add(movie);
    }
}