package com.app.movietradingplatform.entity.user.controller;

import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.dto.MovieResponse;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/users/{userId}/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserMovieRestController {

    UserService userService;
    MovieService movieService;
    @EJB
    public void setUserService(UserService service) {
        this.userService = service;
    }
    @EJB
    public void setMovieService(MovieService service) {
        this.movieService = service;
    }

    private MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .genres(movie.getGenres().toString())
                .build();
    }

    @GET
    @PermitAll
    public Response listAllMoviesForUser(@PathParam("userId") String userUuid) {
        UUID userId = UUID.fromString(userUuid);
        if (userService.find(userId).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "User not found: " + userId))
                    .build();
        }
        if (!userService.verifyCallerPrincipal(userId)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "User is not authorized to do this operation."))
                    .build();
        }

        try {
            List<MovieResponse> response = movieService.findMoviesByUser(userId)
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
    public Response listMovieForUser(@PathParam("userId") String userId, @PathParam("movieId") String movieId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            if (!userService.verifyCallerPrincipal(userUuid)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("error", "User is not authorized to do this operation."))
                        .build();
            }
            Movie movie = movieService.findMovieByUser(userUuid, UUID.fromString(movieId))
                    .orElseThrow(() -> new NoSuchElementException("Movie not found"));
            return Response.ok(toResponse(movie)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @PermitAll
    public Response createMovieForUser(@PathParam("userId") String userId, MovieRequest request, @Context UriInfo uriInfo) {
        UUID userUuid = UUID.fromString(userId);
        if (!userService.verifyCallerPrincipal(userUuid)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "User is not authorized to do this operation."))
                    .build();
        }
        try {
            Movie movie = new Movie();
            movie.setTitle(request.getTitle());
            movie.setGenres(request.getGenres());
            movie.setReleaseDate(request.getReleaseDate());

            Movie created = movieService.createMovieForUser(userUuid, movie);
            URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
            return Response.created(uri).entity(toResponse(created)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("{movieId}")
    @PermitAll
    public Response updateMovieForUser(@PathParam("userId") String userId,
                                           @PathParam("movieId") String movieId,
                                           MovieRequest request) {
        UUID userUuid = UUID.fromString(userId);
        if (!userService.verifyCallerPrincipal(userUuid)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "User is not authorized to do this operation."))
                    .build();
        }
        try {
            Movie movie = new Movie();
            movie.setTitle(request.getTitle());
            movie.setGenres(request.getGenres());
            movie.setReleaseDate(request.getReleaseDate());

            Movie updated = movieService.updateMovieForUser(userUuid, UUID.fromString(movieId), movie);
            return Response.ok(toResponse(updated)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{movieId}")
    @PermitAll
    public Response deleteMovieForUser(@PathParam("userId") String userId,
                                           @PathParam("movieId") String movieId) {
        UUID userUuid = UUID.fromString(userId);
        if (!userService.verifyCallerPrincipal(userUuid)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "User is not authorized to do this operation."))
                    .build();
        }
        try {
            movieService.deleteMovieForUser(userUuid, UUID.fromString(movieId));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
