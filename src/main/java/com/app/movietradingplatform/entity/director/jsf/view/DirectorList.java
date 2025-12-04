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
    private List<Director> directors;

    @EJB
    private DirectorService directorService;

    public List<Director> getDirectors() {
        return directorService.findAll();
    }

    public void deleteDirector(UUID id) {
        if (id != null) {
            directorService.delete(id);
        }
    }
}