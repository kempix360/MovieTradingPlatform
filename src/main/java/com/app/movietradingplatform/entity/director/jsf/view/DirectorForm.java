package com.app.movietradingplatform.entity.director.jsf.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class DirectorForm implements Serializable {
    UUID id;
    Director director;

    @EJB
    private DirectorService directorService;

    public void init(){

    }

    public List<Director> getDirectors() {
        return directorService.findAll();
    }

    public String deleteDirector(UUID id) {
        try {
            directorService.delete(id);
        } catch (IllegalArgumentException ignored) {}
        return "/view/director/list?faces-redirect=true";
    }
}
