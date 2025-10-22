package com.app.movietradingplatform.entity.movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.enums.Genre;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Movie implements Serializable {
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