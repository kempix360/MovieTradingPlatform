package com.app.movietradingplatform.entity.movie.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class MovieEditView implements Serializable {
    private UUID directorId;
    private UUID movieId;
    private Movie movie;
    private Director director;
    private final List<Genre> availableGenres = new ArrayList<>(Arrays.asList(Genre.values()));

    @Inject
    private MovieService movieService;

    @Inject
    private DirectorService directorService;

    public void loadMovie() {
        if (directorId != null) {
            director = directorService.getById(directorId);
            if (director != null && movieId != null) {
                movie = director.getMovies().stream()
                        .filter(m -> m.getId().equals(movieId))
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    public String updateMovie() {
        if (movie == null || director == null) {
            throw new IllegalStateException("Director and movie must be loaded before updating.");
        }
        director.getMovies().removeIf(m -> m.getId().equals(movie.getId()));
        director.getMovies().add(movie);

        directorService.update(directorId, director);
        return "/view/director/director_details.xhtml?faces-redirect=true&id=" + director.getId();
    }

    public void redirectIfDirectorIsNull() {
        if (directorId == null || director == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/view/director/director_list.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void redirectIfMovieIsNull() {
        if (movieId == null || movie == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/view/director/director_list.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
