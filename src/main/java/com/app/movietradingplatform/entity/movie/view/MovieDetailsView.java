package com.app.movietradingplatform.entity.movie.view;

import com.app.movietradingplatform.entity.movie.Movie;
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
public class MovieDetailsView implements Serializable {

    @Inject
    private UserService userService;

    private UUID userId;
    private UUID movieId;
    private Movie movie;

    public void loadMovie() {
        if (userId != null && movieId != null) {
            User user = userService.getById(userId);
            if (user != null) {
                movie = user.getOwnedMovies().stream()
                        .filter(m -> m.getId().equals(movieId))
                        .findFirst()
                        .orElse(null);
            }
        }
    }

}
