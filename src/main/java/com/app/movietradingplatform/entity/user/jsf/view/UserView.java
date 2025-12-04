package com.app.movietradingplatform.entity.user.jsf.view;

import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class UserView implements Serializable {
    private UUID id;
    private User user;

    @EJB
    private UserService userService;

    public void init() {
        if (id != null) {
            Optional<User> u = userService.find(id);
            user = u.orElse(null);
        }
    }

    public String delete(UUID userId) {
        if (userId == null) return null;
        userService.delete(userId);
        return null;
    }
}