package com.app.movietradingplatform.entity.movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.app.movietradingplatform.entity.enums.Genre;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Movie implements Serializable {
    private UUID id;
    private String title;
    private LocalDate releaseDate;
    @Builder.Default
    private List<Genre> genres = new ArrayList<>();

    public Movie(String title){
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = LocalDate.now();
    }
    public Movie(String title, LocalDate releaseDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = releaseDate;
    }
    public Movie(String title, LocalDate releaseDate, List<Genre> genres) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }
}