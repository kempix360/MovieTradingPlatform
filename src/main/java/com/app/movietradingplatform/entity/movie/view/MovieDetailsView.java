package com.app.movietradingplatform.entity.movie.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class MovieDetailsView implements Serializable {
    private UUID id;
    private UUID directorId;
    private Movie movie;

    @Inject
    private MovieService movieService;

    @Inject
    private DirectorService directorService;

    public void loadMovie() {
        if (id != null) {
            Optional<Movie> m = movieService.findMovieByDirector(directorId, id);
            movie = m.orElse(null);
        }
    }

    public void redirectIfMovieIsNull() {
        if (id == null || movie == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/view/director/director_list.xhtml");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}