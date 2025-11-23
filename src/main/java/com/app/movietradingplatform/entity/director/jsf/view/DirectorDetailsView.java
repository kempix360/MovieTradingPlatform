package com.app.movietradingplatform.entity.director.jsf.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.ejb.EJB;
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
public class DirectorDetailsView implements Serializable {
    private UUID id;
    private Director director;

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

    public void loadDirector() {
        if (id != null) {
            Optional<Director> d = directorService.find(id);
            director = d.orElse(null);
        }
    }

    public String deleteMovie(UUID movieId) {
        if (movieId == null) return null;
        movieService.deleteMovieForDirector(id, movieId);
        return "director_details?faces-redirect=true&amp;id=" + id;
    }

    public void redirectIfDirectorIsNull() {
        if (id == null || director == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/view/director/director_list.xhtml");
            } catch (IOException e) {
                System.out.println("IOException in redirectIfDirectorIsNull: " + e.getMessage());
            }
        }
    }
}