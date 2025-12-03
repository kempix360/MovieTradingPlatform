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
public class DirectorList implements Serializable {
    private DirectorService directorService;

    @EJB
    public void setDirectorService(DirectorService service) {
        this.directorService = service;
    }

    public List<Director> getDirectors() {
        return directorService.findAll();
    }

    public String deleteDirector(UUID id) {
        directorService.delete(id);
        return "director_list?faces-redirect=true";
    }
}