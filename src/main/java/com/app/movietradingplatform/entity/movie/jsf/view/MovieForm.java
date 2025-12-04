package com.app.movietradingplatform.entity.movie.jsf.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

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
            // Load the existing movie
            Optional<Movie> movieOpt = movieService.findMovieByCaller(movieId);
            movieOpt.ifPresent(value -> movie = value);
        } else {
            // Initialize a new movie
            movie = new Movie();
        }

        if (directorId != null) {
            Optional<Director> directorOpt = directorService.find(directorId);
            directorOpt.ifPresent(value -> director = value);
        }
        availableDirectors = directorService.findAll();
    }

    public String save() {
        try{
            if (movieId == null) {
                // Adding a new movie
                movie.setDirector(director);
                movieService.createMovieForCaller(movie);
//                movieService.createMovieForDirector(directorId, movie);
                return "/view/director/view.xhtml?faces-redirect=true&id=" + director.getId().toString();
            } else {
                // Updating an existing movie
                movieService.updateMovieForCaller(movieId, movie);
                return "/view/movie/view.xhtml?faces-redirect=true&id=" + movieId.toString();
            }
        }
        catch(Exception e){
            return null;
        }
    }
}