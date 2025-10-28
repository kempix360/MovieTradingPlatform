package com.app.movietradingplatform.entity.director.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class DirectorListView implements Serializable {
    @Inject
    private DirectorService directorService;

    public List<Director> getDirectors() {
        return directorService.getAll();
    }

    public String deleteDirector(UUID id) {
        directorService.delete(id);
        return "director_list?faces-redirect=true";
    }
}
