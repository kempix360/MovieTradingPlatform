package com.app.movietradingplatform.entity.director.service;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.repository.DirectorRepository;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.*;

@LocalBean
@Stateless
@NoArgsConstructor()
public class DirectorService {
    private DirectorRepository directorRepository;
    private UserService userService;

    @Inject
    public DirectorService(DirectorRepository directorRepository, UserService userService) {
        this.directorRepository = directorRepository;
        this.userService = userService;
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public Optional<Director> find(UUID id) {
        return directorRepository.find(id);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Director create(Director director) {
        directorRepository.create(director);
        return director;
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public Director update(Director director) {
        directorRepository.update(director);
        return director;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void delete(UUID id) {
        directorRepository.find(id).ifPresent(director -> {
            // Remove movies from users' ownedMovies lists
            if (director.getMovies() != null) {
                for (Movie movie : director.getMovies()) {
                    User user = movie.getUser();
                    if (user != null && user.getOwnedMovies() != null) {
                        user.getOwnedMovies().remove(movie);
                        userService.update(user); // Persist the updated user
                    }
                }
            }
            // Delete the director
            directorRepository.delete(director);
        });
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteAll() {
        directorRepository.deleteAll();
    }
}