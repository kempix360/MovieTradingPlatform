package com.app.movietradingplatform.entity.movie.service;

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
import lombok.NoArgsConstructor;

import java.util.*;

@LocalBean
@Stateless
@NoArgsConstructor()
public class MovieService {
    @Inject
    private MovieRepository movieRepository;
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

    @RolesAllowed(UserRoles.ADMIN)
    public Movie create(Movie movie) {
        movieRepository.create(movie);
        return movie;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Movie update(Movie movie) {
        movieRepository.update(movie);
        return movie;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void delete(UUID id) {
        movieRepository.find(id).ifPresent(movieRepository::delete);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteAll() {
        movieRepository.deleteAll();
    }

    // just for data initialization
    public void createWithLinks(MovieRequest request) {
        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .title(request.getTitle())
                .releaseDate(request.getReleaseDate())
                .genres(request.getGenres())
                .build();

        if (request.getDirectorId() != null) {
            directorService.find(request.getDirectorId()).ifPresent(movie::setDirector);
        }

        if (request.getUserId() != null) {
            userService.find(request.getUserId()).ifPresent(movie::setUser);
        }

        movieRepository.create(movie);

        // link movie in Director object
        if (movie.getDirector() != null) {
            Director director = movie.getDirector();
            if (director.getMovies() == null) director.setMovies(new ArrayList<>());
            director.getMovies().add(movie);
            directorService.update(director);
        }

        // link movie in User object
        if (movie.getUser() != null) {
            User user = movie.getUser();
            if (user.getOwnedMovies() == null) user.setOwnedMovies(new ArrayList<>());
            user.getOwnedMovies().add(movie);
            userService.update(user);
        }

    }

    @RolesAllowed(UserRoles.ADMIN)
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

    @RolesAllowed(UserRoles.ADMIN)
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

    @RolesAllowed(UserRoles.ADMIN)
    public Movie updateMovieForDirector(UUID directorId, UUID movieId, Movie updatedMovie) {
        return directorService.find(directorId).flatMap(director -> {
            return movieRepository.find(movieId).map(existingMovie -> {
                if (!Objects.equals(existingMovie.getDirector().getId(), directorId)) {
                    throw new IllegalArgumentException("Movie does not belong to the specified director");
                }

                // Update movie details
                existingMovie.setTitle(updatedMovie.getTitle());
                existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
                existingMovie.setGenres(updatedMovie.getGenres());
                movieRepository.update(existingMovie);

                return existingMovie;
            });
        }).orElseThrow(() -> new NoSuchElementException("Director or Movie not found"));
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteMovieForDirector(UUID directorId, UUID movieId) {
        directorService.find(directorId).ifPresent(director -> {
            movieRepository.find(movieId).ifPresent(movie -> {
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
            });
        });
    }

    @RolesAllowed(UserRoles.ADMIN)
    public List<Movie> findMoviesByUser(UUID userId) {
        return userService.find(userId)
                .map(User::getOwnedMovies)
                .orElse(Collections.emptyList());
    }

    public Optional<Movie> findMovieByUser(UUID userId, UUID movieId) {
        return userService.find(userId)
                .flatMap(director -> director.getOwnedMovies().stream()
                        .filter(movie -> Objects.equals(movie.getId(), movieId))
                        .findFirst());
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Movie createMovieForUser(UUID userId, Movie movie) {
        return userService.find(userId).map(user -> {
            if (movie.getId() == null) movie.setId(UUID.randomUUID());
            movie.setUser(user);
            movieRepository.create(movie);

            // Link the movie in the director's movie list
            if (user.getOwnedMovies() == null) {
                user.setOwnedMovies(new ArrayList<>());
            }
            user.getOwnedMovies().add(movie);
            userService.update(user);

            return movie;
        }).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Movie updateMovieForUser(UUID userId, UUID movieId, Movie updatedMovie) {
        return userService.find(userId).flatMap(user -> {
            return movieRepository.find(movieId).map(existingMovie -> {
                if (!Objects.equals(existingMovie.getUser().getId(), userId)) {
                    throw new IllegalArgumentException("Movie does not belong to the specified user");
                }

                // Update movie details
                existingMovie.setTitle(updatedMovie.getTitle());
                existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
                existingMovie.setGenres(updatedMovie.getGenres());
                movieRepository.update(existingMovie);

                return existingMovie;
            });
        }).orElseThrow(() -> new NoSuchElementException("Director or Movie not found"));
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteMovieForUser(UUID userId, UUID movieId) {
        userService.find(userId).ifPresent(user -> {
            movieRepository.find(movieId).ifPresent(movie -> {
                if (!Objects.equals(movie.getDirector().getId(), userId)) {
                    throw new IllegalArgumentException("Movie does not belong to the specified user");
                }

                // Remove the movie from the director's movie list
                if (user.getOwnedMovies() != null) {
                    user.getOwnedMovies().removeIf(m -> Objects.equals(m.getId(), movieId));
                }
                userService.update(user);

                // Delete the movie
                movieRepository.delete(movie);
            });
        });
    }
}