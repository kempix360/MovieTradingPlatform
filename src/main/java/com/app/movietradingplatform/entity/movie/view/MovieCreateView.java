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
import java.util.*;

@Getter
@Setter
@Named
@ViewScoped
public class MovieCreateView implements Serializable {
    private Movie movie = new Movie();
    private Director director = new Director();
    private UUID directorId;
    private final List<Genre> availableGenres = new ArrayList<>(Arrays.asList(Genre.values()));

    @Inject
    private MovieService movieService;
    @Inject
    private DirectorService directorService;

    public void loadDirector() {
        if (directorId != null) {
            Optional<Director> d = directorService.find(directorId);
            director = d.orElse(null);
        }
    }

    public String createMovie() {
        movieService.createMovieForDirector(directorId, movie);
        return "/view/director/director_details.xhtml?faces-redirect=true&id=" + director.getId();
    }

    public void redirectIfDirectorIsNull() {
        if (directorId == null || director == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/view/director/director_list.xhtml");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}