package com.app.movietradingplatform.entity.user.view;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class UserDetailsView implements Serializable {
    @Inject
    private UserService userService;
    @Inject
    private MovieService avatarService;

    private UUID id;
    private User user;
    private Part avatarFile;
    @Inject
    private MovieService movieService;

    @PostConstruct
    public void init() {
        if (id != null) {
            Optional<User> u = userService.find(id);
            user = u.orElse(null);
        }
    }

    public String deleteMovie(UUID movieId) {
        if (movieId == null) return null;
        movieService.deleteWithLinks(movieId);
        return "user_details?faces-redirect=true&amp;id=" + id;
    }
}