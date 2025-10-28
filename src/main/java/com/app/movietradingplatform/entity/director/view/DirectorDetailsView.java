package com.app.movietradingplatform.entity.director.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class DirectorDetailsView implements Serializable {
    @Inject
    private DirectorService directorService;

    private UUID id;
    private Director director;

    public void loadDirector() {
        if (id != null) {
            director = directorService.getById(id);
        }
    }

    public String deleteMovie(UUID movieId) {
        directorService.deleteMovie(id, movieId);
        return "director_details?faces-redirect=true&amp;id=" + id;
    }
}