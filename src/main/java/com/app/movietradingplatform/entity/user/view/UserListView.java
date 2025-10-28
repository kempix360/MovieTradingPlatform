package com.app.movietradingplatform.entity.user.view;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class UserListView implements Serializable {
    @Inject
    private UserService userService;

    public List<User> getUsers() {
        return userService.getAll();
    }

    public String deleteUser(UUID id) {
        userService.delete(id);
        return "user_list?faces-redirect=true";
    }
}
