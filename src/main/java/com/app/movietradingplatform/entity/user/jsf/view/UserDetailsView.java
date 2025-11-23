package com.app.movietradingplatform.entity.user.jsf.view;

import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
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
    private UUID id;
    private User user;
    private UserService userService;
    private MovieService movieService;

    @EJB
    public void setUserService(UserService service) {
        this.userService = service;
    }
    @EJB
    public void setMovieService(MovieService service) {
        this.movieService = service;
    }

    @PostConstruct
    public void init() {
        if (id != null) {
            Optional<User> u = userService.find(id);
            user = u.orElse(null);
        }
    }

    public String deleteMovie(UUID movieId) {
        if (movieId == null) return null;
        movieService.delete(movieId);
        return "user_details?faces-redirect=true&amp;id=" + id;
    }
}