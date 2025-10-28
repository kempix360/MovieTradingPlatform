package com.app.movietradingplatform.entity.movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.app.movietradingplatform.entity.enums.Genre;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Movie implements Serializable {
    private UUID id;
    private String title;
    private LocalDate releaseDate;
    private List<Genre> genres;

    public Movie(String title){
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = LocalDate.now();
        this.genres = Collections.emptyList();
    }
    public Movie(String title, LocalDate releaseDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = Collections.emptyList();
    }
}