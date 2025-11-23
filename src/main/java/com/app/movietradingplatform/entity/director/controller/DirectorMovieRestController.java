package com.app.movietradingplatform.entity.director.controller;

import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.dto.MovieResponse;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/directors/{directorId}/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DirectorMovieRestController {

    DirectorService directorService;
    MovieService movieService;
    @EJB
    public void setDirectorService(DirectorService service) {
        this.directorService = service;
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
    public Response listAllMoviesForDirector(@PathParam("directorId") String directorId) {
        UUID directorUuid = UUID.fromString(directorId);

        if (directorService.find(directorUuid).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Director not found: " + directorId))
                    .build();
        }

        try {
            List<MovieResponse> response = movieService.findMoviesByDirector(directorUuid)
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
    public Response listMovieForDirector(@PathParam("directorId") String directorId, @PathParam("movieId") String movieId) {
        try {
            Movie movie = movieService.findMovieByDirector(UUID.fromString(directorId), UUID.fromString(movieId))
                    .orElseThrow(() -> new NoSuchElementException("Movie not found"));
            return Response.ok(toResponse(movie)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    public Response createMovieForDirector(@PathParam("directorId") String directorId, MovieRequest request, @Context UriInfo uriInfo) {
        try {
            Movie movie = new Movie();
            movie.setTitle(request.getTitle());
            movie.setGenres(request.getGenres());
            movie.setReleaseDate(request.getReleaseDate());

            Movie created = movieService.createMovieForDirector(UUID.fromString(directorId), movie);
            URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
            return Response.created(uri).entity(toResponse(created)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("{movieId}")
    public Response updateMovieForDirector(@PathParam("directorId") String directorId,
                           @PathParam("movieId") String movieId,
                           MovieRequest request) {
        try {
            Movie movie = new Movie();
            movie.setTitle(request.getTitle());
            movie.setGenres(request.getGenres());
            movie.setReleaseDate(request.getReleaseDate());

            Movie updated = movieService.updateMovieForDirector(UUID.fromString(directorId), UUID.fromString(movieId), movie);
            return Response.ok(toResponse(updated)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{movieId}")
    public Response deleteMovieForDirector(@PathParam("directorId") String directorId,
                           @PathParam("movieId") String movieId) {
        try {
            movieService.deleteMovieForDirector(UUID.fromString(directorId), UUID.fromString(movieId));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
