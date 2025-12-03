package com.app.movietradingplatform.entity.movie.jsf.view;

import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
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
public class MovieView implements Serializable {
    private UUID id;
    private UUID directorId;
    private Movie movie;

    private DirectorService directorService;
    private MovieService movieService;

    @EJB
    public void setDirectorService(DirectorService service) {
        this.directorService = service;
    }
    @EJB
    public void setMovieService(MovieService service) {
        this.movieService = service;
    }

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