package com.app.movietradingplatform.entity.user.view;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
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
public class UserDetailsView implements Serializable {
    @Inject
    private UserService userService;

    private UUID id;
    private User user;

    public void loadUser() {
        if (id != null) {
            user = userService.getById(id);
        }
    }

    public String deleteMovie(UUID movieId) {
        userService.deleteMovie(id, movieId);
        return "user_details?faces-redirect=true&amp;id=" + id;
    }
}