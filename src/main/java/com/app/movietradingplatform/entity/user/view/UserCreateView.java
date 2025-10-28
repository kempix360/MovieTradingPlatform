package com.app.movietradingplatform.entity.user.view;

import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Named
@ViewScoped
public class UserCreateView implements Serializable {
    private UUID id;
    private User user;
    private Movie newMovie = new Movie();

    @Inject
    private UserService userService;
    @Inject
    private MovieService movieService;

    @PostConstruct
    public void init() {
        if (id != null) {
            user = userService.getById(id);
        } else {
            user = new User();
            user.setRegistrationDate(LocalDate.now());
        }
    }

    public void addMovie() {
        if (newMovie.getTitle() != null && !newMovie.getTitle().trim().isEmpty()) {
            if (user.getOwnedMovies() == null) {
                user.setOwnedMovies(new ArrayList<>());
            }
            user.getOwnedMovies().add(newMovie);
            newMovie = new Movie(); // Reset form
        }
    }

    public void removeMovie(Movie movie) {
        if (user.getOwnedMovies() != null) {
            user.getOwnedMovies().remove(movie);
        }
    }

    public String save() {
        userService.save(user);
        return "/view/user/user_list.xhtml?faces-redirect=true";
    }

    public List<Movie> getOwnedMovies() { return user.getOwnedMovies(); }
}
