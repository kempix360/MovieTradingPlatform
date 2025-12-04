package com.app.movietradingplatform.entity.movie.controller;

import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.dto.MovieResponse;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.dto.UserResponse;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieRestController {

    private MovieService movieService;
    private UserService userService;
    @EJB
    public void setMovieService(MovieService service) {
        this.movieService = service;
    }
    @EJB
    public void setUserService(UserService service) {
        this.userService = service;
    }

    private MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .genres(movie.getGenres().toString())
                .user(UserResponse.builder()
                        .id(movie.getUser().getId())
                        .username(movie.getUser().getUsername())
                        .registrationDate(movie.getUser().getRegistrationDate())
                        .build())
                .build();
    }

    @GET
    @PermitAll
    public Response listAllMoviesForAUser() {
        if (!userService.verifyCallerPrincipal()) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "User is not authorized to do this operation."))
                    .build();
        }
        try {
            List<MovieResponse> response = movieService.findAllMoviesByCaller()
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
    @PermitAll
    public Response getMovieForAUser(@PathParam("movieId") String movieId) {
        try {
            if (!userService.verifyCallerPrincipal()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("error", "User is not authorized to do this operation."))
                        .build();
            }
            Movie movie = movieService.findMovieByCaller(UUID.fromString(movieId))
                    .orElseThrow(() -> new NoSuchElementException("Movie not found"));
            return Response.ok(toResponse(movie)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @PermitAll
    public Response createMovieForUser(MovieRequest request, @Context UriInfo uriInfo) {
        try {
            Movie movie = new Movie();
            movie.setTitle(request.getTitle());
            movie.setGenres(request.getGenres());
            movie.setReleaseDate(request.getReleaseDate());

            Movie created = movieService.createMovieForCaller(movie);
            URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
            return Response.created(uri).entity(toResponse(created)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}