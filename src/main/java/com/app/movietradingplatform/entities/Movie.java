package com.app.movietradingplatform.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie {
    private UUID id;
    private String title;
    private LocalDate releaseDate;
    private List<Genre> genres;
    private Director director;
    private User user;

    public Movie() {
        this.id = UUID.randomUUID();
        this.title = "Unknown";
        this.releaseDate = LocalDate.now();
        this.genres = new ArrayList<>();
        this.director = new Director();
        this.user = new User();
    }
    public Movie(String title, LocalDate releaseDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = Collections.emptyList();
        this.director = new Director();
        this.user = new User();
    }
    public Movie(String title, LocalDate releaseDate, List<Genre> genres, Director director, User user) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.director = director;
        this.user = user;
    }
}