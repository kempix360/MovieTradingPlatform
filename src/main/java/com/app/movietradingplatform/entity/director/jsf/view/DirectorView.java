package com.app.movietradingplatform.entity.director.jsf.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.UserRoles;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class DirectorView implements Serializable {
    private UUID id;
    private Director director;
    private List<Movie> movies;
    private boolean notFound = false;
    private UUID movieToDeleteId;

    @EJB
    private DirectorService directorService;
    @EJB
    private MovieService movieService;

    public void init() {
        if (id != null) {
            Optional<Director> directorOpt = directorService.find(id);
            if (directorOpt.isPresent()) {
                director = directorOpt.get();
                notFound = false;
                movies = movieService.findMoviesByDirector(director.getId());
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

    public String deleteDirector(UUID id) {
        if (director == null) return null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc != null && !fc.getExternalContext().isUserInRole(UserRoles.ADMIN)) {
                try {
                    fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/errors/403.xhtml");
                } catch (Exception ignored) {}
                return null;
            }
//            movieService.delete(id);
            directorService.delete(id);
        } catch (IllegalArgumentException ignored) {}
        return "/view/director/list.xhtml?faces-redirect=true";
    }

    public void deleteSelectedMovie() {
        try {
            if (director != null && movieToDeleteId != null) {
//                movieService.deleteMovieForDirector(director.getId(), movieToDeleteId);
                movieService.deleteMovieForCaller(movieToDeleteId);
                movieToDeleteId = null;
                FacesContext.getCurrentInstance().addMessage(null,
                        new jakarta.faces.application.FacesMessage(
                                jakarta.faces.application.FacesMessage.SEVERITY_INFO, "Movie deleted", null));
            }
        }
        catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new jakarta.faces.application.FacesMessage(
                            jakarta.faces.application.FacesMessage.SEVERITY_ERROR, "Error while deleting movie: ", e.getMessage()));
        }
    }
}