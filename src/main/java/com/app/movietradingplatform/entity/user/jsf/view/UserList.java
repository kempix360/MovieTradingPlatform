package com.app.movietradingplatform.entity.user.jsf.view;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class UserList implements Serializable {
    @Getter
    List<User> users;
    @EJB
    private UserService userService;

    @PostConstruct
    public void init() {
        users = userService.findAll();
    }

    public void checkAccess() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
        if (req.getUserPrincipal() == null) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/view/auth/login.xhtml");
        }
    }

    public String deleteUser(UUID id) {
        userService.delete(id);
        init();
        return null;
    }

}
