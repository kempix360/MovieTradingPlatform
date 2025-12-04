package com.app.movietradingplatform.entity.movie.jsf.view;

import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class MovieList implements Serializable {
    @EJB
    MovieService movieService;

    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movieService.findAllMoviesByCaller();
    }

    public String delete(UUID movieId) {
        if (movieId != null) {
            try {
                movieService.deleteMovieForCaller(movieId);
                return "/view/movie/list.xhtml?faces-redirect=true";
            } catch (Exception ignored) {
                // if deletion was forbidden or failed, stay on current page
                return null;
            }
        }
        return "/view/movie/list.xhtml?faces-redirect=true";
    }
}
