package com.app.movietradingplatform.entity.movie.dto;

import com.app.movietradingplatform.entity.enums.Genre;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MovieRequest {
    private String title;
    private LocalDate releaseDate;
    private List<Genre> genres;
    private UUID directorId;
    private UUID userId;
}