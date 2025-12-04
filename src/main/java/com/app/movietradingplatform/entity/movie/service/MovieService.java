package com.app.movietradingplatform.entity.movie.service;

import com.app.movietradingplatform.config.interceptor.binding.LogAccess;
import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.repository.MovieRepository;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import lombok.NoArgsConstructor;

import java.util.*;

@LocalBean
@Stateless
@NoArgsConstructor()
public class MovieService {
    private MovieRepository movieRepository;
    private SecurityContext securityContext;

    @Inject
    public MovieService(MovieRepository movieRepository,
                        SecurityContext securityContext) {
        this.movieRepository = movieRepository;
        this.securityContext = securityContext;
    }

    private DirectorService directorService;
    private UserService userService;
    @EJB
    public void setDirectorService(DirectorService directorService) {
        this.directorService = directorService;
    }
    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @PermitAll
    public Optional<Movie> find(UUID id) {
        return movieRepository.find(id);
    }

    @LogAccess("CREATE MOVIE")
    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public Movie create(Movie movie) {
        movieRepository.create(movie);
        return movie;
    }

    @RolesAllowed(UserRoles.ADMIN)
    @LogAccess("UPDATE MOVIE")
    public Movie update(Movie movie) {
        movieRepository.update(movie);
        return movie;
    }

    @RolesAllowed(UserRoles.ADMIN)
    @LogAccess("DELETE MOVIE")
    public void delete(UUID id) {
        movieRepository.find(id).ifPresent(movieRepository::delete);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteAll() {
        movieRepository.deleteAll();
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public List<Movie> findMoviesByDirector(UUID directorId) {
        return directorService.find(directorId)
                .map(Director::getMovies)
                .orElse(Collections.emptyList());
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<Movie> findMovieByDirector(UUID directorId, UUID movieId) {
        return directorService.find(directorId)
                .flatMap(director -> director.getMovies().stream()
                        .filter(movie -> Objects.equals(movie.getId(), movieId))
                        .findFirst());
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @LogAccess("CREATE MOVIE FOR DIRECTOR")
    public Movie createMovieForDirector(UUID directorId, Movie movie) {
        return directorService.find(directorId).map(director -> {
            if (movie.getId() == null) movie.setId(UUID.randomUUID());
            movie.setDirector(director);
            movieRepository.create(movie);

            // Link the movie in the director's movie list
            if (director.getMovies() == null) {
                director.setMovies(new ArrayList<>());
            }
            director.getMovies().add(movie);
            directorService.update(director);

            return movie;
        }).orElseThrow(() -> new NoSuchElementException("Director not found"));
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @LogAccess("UPDATE MOVIE FOR DIRECTOR")
    public Movie updateMovieForDirector(UUID directorId, UUID movieId, Movie updatedMovie) {
        return directorService.find(directorId).flatMap(director -> movieRepository.find(movieId).map(existingMovie -> {
            if (!Objects.equals(existingMovie.getDirector().getId(), directorId)) {
                throw new IllegalArgumentException("Movie does not belong to the specified director");
            }

            // Update movie details
            existingMovie.setTitle(updatedMovie.getTitle());
            existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
            existingMovie.setGenres(updatedMovie.getGenres());
            movieRepository.update(existingMovie);

            return existingMovie;
        })).orElseThrow(() -> new NoSuchElementException("Director or Movie not found"));
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @LogAccess("DELETE MOVIE FOR DIRECTOR")
    public void deleteMovieForDirector(UUID directorId, UUID movieId) {
        directorService.find(directorId).ifPresent(director -> movieRepository.find(movieId).ifPresent(movie -> {
            if (!Objects.equals(movie.getDirector().getId(), directorId)) {
                throw new IllegalArgumentException("Movie does not belong to the specified director");
            }

            // Remove the movie from the director's movie list
            if (director.getMovies() != null) {
                director.getMovies().removeIf(m -> Objects.equals(m.getId(), movieId));
            }
            directorService.update(director);

            // Delete the movie
            movieRepository.delete(movie);
        }));
    }

    @PermitAll
    public List<Movie> findAllMoviesByCaller() {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return movieRepository.findAll();
        }
        String callerName = securityContext.getCallerPrincipal().getName();
        return userService.findByUsername(callerName)
                .map(User::getOwnedMovies)
                .orElse(Collections.emptyList());
    }

    @PermitAll
    public List<Movie> findAllMoviesByCallerAndDirector(UUID directorId) {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return movieRepository.findAll();
        }
        String callerName = securityContext.getCallerPrincipal().getName();
        return userService.findByUsername(callerName)
                .map(user -> user.getOwnedMovies().stream()
                        .filter(movie -> movie.getDirector() != null && movie.getDirector().getId().equals(directorId))
                        .toList())
                .orElse(Collections.emptyList());
    }

    @PermitAll
    public Optional<Movie> findMovieByCaller(UUID movieId) {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return movieRepository.find(movieId);
        }
        String callerName = securityContext.getCallerPrincipal().getName();
        return userService.findByUsername(callerName)
                .flatMap(user -> user.getOwnedMovies().stream()
                        .filter(movie -> Objects.equals(movie.getId(), movieId))
                        .findFirst());
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @LogAccess("CREATE MOVIE FOR CALLER")
    public Movie createMovieForCaller(Movie movie) {
        String callerName = securityContext.getCallerPrincipal().getName();
        Optional<User> userOptional = userService.findByUsername(callerName);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found for caller name: " + callerName);
        }
        User user = userOptional.get();
        if (movie.getId() == null) {
            movie.setId(UUID.randomUUID());
        }
        movie.setUser(user);
        movieRepository.create(movie);

        // Link the movie in the user's movie list
        if (user.getOwnedMovies() == null) {
            user.setOwnedMovies(new ArrayList<>());
        }
        user.getOwnedMovies().add(movie);
        userService.update(user);
        Director director = movie.getDirector();
        director.getMovies().add(movie);
        directorService.update(director);

        return movie;
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    @LogAccess("UPDATE MOVIE FOR CALLER")
    public Movie updateMovieForCaller(UUID movieId, Movie updatedMovie) {
        String callerName = securityContext.getCallerPrincipal().getName();
        return userService.findByUsername(callerName).flatMap(user -> movieRepository.find(movieId).map(existingMovie -> {
            if (!securityContext.isCallerInRole(UserRoles.ADMIN) && !Objects.equals(existingMovie.getUser().getUsername(), callerName)) {
                throw new IllegalArgumentException("Movie does not belong to the specified user");
            }

            // Update movie details
            existingMovie.setTitle(updatedMovie.getTitle());
            existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
            existingMovie.setGenres(updatedMovie.getGenres());
            movieRepository.update(existingMovie);

            return existingMovie;
        })).orElseThrow(() -> new NoSuchElementException("Director or Movie not found"));
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    @LogAccess("DELETE MOVIE FOR CALLER")
    public void deleteMovieForCaller(UUID movieId) {
        String callerName = securityContext.getCallerPrincipal().getName();
        userService.findByUsername(callerName).ifPresent(user -> movieRepository.find(movieId).ifPresent(movie -> {
            if (!securityContext.isCallerInRole(UserRoles.ADMIN) && !Objects.equals(movie.getUser().getUsername(), callerName)) {
                throw new IllegalArgumentException("Movie does not belong to the specified user");
            }
            if (user.getOwnedMovies() != null) {
                user.getOwnedMovies().removeIf(m -> Objects.equals(m.getId(), movieId));
            }
            userService.update(user);
            // Delete the movie
            movieRepository.delete(movie);
        }));
    }
}