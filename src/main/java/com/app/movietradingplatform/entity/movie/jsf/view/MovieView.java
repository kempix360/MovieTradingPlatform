package com.app.movietradingplatform.entity.movie.jsf.view;

import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.annotation.PostConstruct;
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
    private Movie movie;
    private boolean notFound;

    @EJB
    private DirectorService directorService;
    @EJB
    private MovieService movieService;

    public void init() {
        if (id != null) {
            Optional<Movie> movieOpt = movieService.findMovieByCaller(id);
            if (movieOpt.isPresent()) {
                movie = movieOpt.get();
                notFound = false;
            }
            else notFound = true;
        } else {
            notFound = true;
        }

        if (notFound) {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc != null) {
                try {
                    fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/errors/404.xhtml");
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String delete() {
        if (id != null) {
            movieService.delete(id);
        }
        return "/view/movie/list.xhtml?faces-redirect=true";
    }
}