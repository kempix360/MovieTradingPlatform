package com.app.movietradingplatform.entity.movie.dto;

import com.app.movietradingplatform.entity.director.dto.DirectorResponse;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MovieResponse {
    private UUID id;
    private String title;
    private LocalDate releaseDate;
    private String genres;
    private DirectorResponse director;
}
