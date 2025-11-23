package com.app.movietradingplatform.entity.user.jsf.view;

import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class UserListView implements Serializable {
    private UserService userService;

    @EJB
    public void setUserService(UserService service) {
        this.userService = service;
    }

    public List<User> getUsers() {
        return userService.findAll();
    }

    public String deleteUser(UUID id) {
        userService.delete(id);
        return "user_list?faces-redirect=true";
    }
}
