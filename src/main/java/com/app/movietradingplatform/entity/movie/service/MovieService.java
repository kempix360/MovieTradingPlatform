package com.app.movietradingplatform.entity.movie.service;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.repository.MovieRepository;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.*;

@ApplicationScoped
@NoArgsConstructor()
public class MovieService {
    private MovieRepository movieRepository;
    private DirectorService directorService;
    private UserService userService;

    @Inject
    public MovieService(MovieRepository movieRepository, DirectorService directorService,
                        UserService userService) {
        this.movieRepository = movieRepository;
        this.directorService = directorService;
        this.userService = userService;
    }

    // just for data initialization
    @Transactional
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
            var director = movie.getDirector();
            if (director.getMovies() == null) director.setMovies(new ArrayList<>());
            director.getMovies().add(movie);
            directorService.update(director);
        }

        // link movie in User object
        if (movie.getUser() != null) {
            var user = movie.getUser();
            if (user.getOwnedMovies() == null) user.setOwnedMovies(new ArrayList<>());
            user.getOwnedMovies().add(movie);
            userService.update(user);
        }

    }

//    public boolean updatePartialWithLinks(PatchSongRequest request, UUID uuid) {
//        return songRepository.find(uuid).map(song -> {
//            if (request.getTitle() != null) song.setTitle(request.getTitle());
//            if (request.getGenre() != null) song.setGenre(request.getGenre());
//            if (request.getReleaseYear() != null) song.setReleaseYear(request.getReleaseYear());
//            if (request.getDuration() != null) song.setDuration(request.getDuration());
//            // re-link artist
//            if (request.getArtistId() != null) {
//                var oldArtist = song.getArtist();
//                if (oldArtist != null) {
//                    oldArtist.getSongs().removeIf(s -> Objects.equals(s.getId(), song.getId()));
//                    artistService.update(oldArtist);
//                }
//                artistService.find(request.getArtistId()).ifPresent(newArtist -> {
//                    if (newArtist.getSongs() == null) newArtist.setSongs(new ArrayList<>());
//                    newArtist.getSongs().add(song);
//                    artistService.update(newArtist);
//                    song.setArtist(newArtist);
//                });
//            }
//
//            // re-link user
//            if (request.getUserId() != null) {
//                var oldUser = song.getUser();
//                if (oldUser != null) {
//                    if (oldUser.getSongs() != null) oldUser.getSongs().removeIf(s -> Objects.equals(s.getId(), song.getId()));
//                    userService.update(oldUser);
//                }
//                userService.find(request.getUserId()).ifPresent(newUser -> {
//                    if (newUser.getSongs() == null) newUser.setSongs(new ArrayList<>());
//                    newUser.getSongs().add(song);
//                    userService.update(newUser);
//                    song.setUser(newUser);
//                });
//            }
//
//            songRepository.update(song);
//            return true;
//        }).orElse(false);
//    }
//
    public void deleteWithLinks(UUID movieId) {
        movieRepository.find(movieId).ifPresent(movie -> {
            Director director = movie.getDirector();
            User user = movie.getUser();
            if (director != null) {
                if (director.getMovies() != null) director.getMovies().removeIf(
                        s -> Objects.equals(s.getId(), movie.getId()));
                directorService.update(director);
            }
            if (user != null) {
                if (user.getOwnedMovies() != null) user.getOwnedMovies().removeIf(
                        s -> Objects.equals(s.getId(), movie.getId()));
                userService.update(user);
            }
            movieRepository.delete(movie);
        });
    }

    public List<Movie> findMoviesByDirector(UUID directorId) {
        return directorService.find(directorId)
                .map(Director::getMovies)
                .orElse(Collections.emptyList());
    }

    public Optional<Movie> findMovieByDirector(UUID directorId, UUID movieId) {
        return directorService.find(directorId)
                .flatMap(director -> director.getMovies().stream()
                        .filter(movie -> Objects.equals(movie.getId(), movieId))
                        .findFirst());
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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
}