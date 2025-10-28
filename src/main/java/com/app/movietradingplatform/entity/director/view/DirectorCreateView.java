package com.app.movietradingplatform.entity.director.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Named
@ViewScoped
public class DirectorCreateView implements Serializable {
    @Inject
    private DirectorService directorService;

    private UUID id;
    private Director director;

    public void loadDirector() {
        if (id != null) {
            director = directorService.getById(id);
        }
    }

    public void removeMovie(Movie movie) {
        if (director.getMovies() != null) {
            director.getMovies().remove(movie);
        }
    }

    public String update() {
        directorService.update(director);
        return "/view/director/director_list.xhtml?faces-redirect=true";
    }
}
