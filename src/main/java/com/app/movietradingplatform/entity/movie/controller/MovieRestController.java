package com.app.movietradingplatform.entity.movie.controller;

import com.app.movietradingplatform.entity.director.dto.DirectorResponse;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieResponse;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.dto.UserResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.stream.Collectors;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieRestController {

    private MovieService movieService;
    @EJB
    public void setService(MovieService service) {
        this.movieService = service;
    }

    private MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .genres(movie.getGenres().toString())
                .director(DirectorResponse.builder()
                        .id(movie.getDirector().getId())
                        .name(movie.getDirector().getName())
                        .description(movie.getDirector().getDescription())
                        .build())
                .user(UserResponse.builder()
                        .id(movie.getUser().getId())
                        .username(movie.getUser().getUsername())
                        .registrationDate(movie.getUser().getRegistrationDate())
                        .build())
                .build();
    }

    @GET
    @RolesAllowed(UserRoles.ADMIN)
    public Response listAllMovies() {
        try {
            List<MovieResponse> response = movieService.findAll()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return Response.ok(response).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("{movieId}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response list(@PathParam("movieId") String movieId) {
        try {
            Movie movie = movieService.find(UUID.fromString(movieId))
                    .orElseThrow(() -> new NoSuchElementException("Movie not found"));
            return Response.ok(toResponse(movie)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }


}