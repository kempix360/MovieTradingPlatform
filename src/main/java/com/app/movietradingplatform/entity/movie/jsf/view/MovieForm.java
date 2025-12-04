package com.app.movietradingplatform.entity.movie.jsf.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
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
public class MovieForm implements Serializable {
    private UUID movieId;
    private Movie movie;
    private UUID directorId;
    private Director director;
    private List<Director> availableDirectors;
    private final List<Genre> availableGenres = new ArrayList<>(Arrays.asList(Genre.values()));

    @EJB
    private DirectorService directorService;
    @EJB
    private MovieService movieService;

    public void init() {
        if (movieId != null) {
            Optional<Movie> movieOpt = movieService.findMovieByCaller(movieId);
            movieOpt.ifPresent(value -> movie = value);
        }

        if (directorId != null) {
            Optional<Director> directorOpt = directorService.find(directorId);
            directorOpt.ifPresent(value -> director = value);
        }
        availableDirectors = directorService.findAll();
    }

    public String save() {
        try{
            movieService.updateMovieForCaller(movieId, movie);
            return "/view/movie/view.xhtml?faces-redirect=true&id=" + movieId.toString();
        }
        catch(Exception e){
            return null;
        }
    }
}