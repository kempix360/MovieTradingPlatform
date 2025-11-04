package com.app.movietradingplatform.entity.director.jsf;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.UUID;

@FacesConverter(value = "directorConverter", managed = true)
public class DirectorConverter implements Converter<Director> {

    @Inject
    private DirectorService directorService;

    @Override
    public Director getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            UUID id = UUID.fromString(s);
            return directorService.getById(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Director director) {
        if (director == null) return "";
        return director.getId() == null ? "" : director.getId().toString();
    }

}