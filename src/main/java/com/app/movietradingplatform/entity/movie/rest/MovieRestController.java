package com.app.movietradingplatform.entity.movie.rest;


import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;

@Path("/directors/{directorId}/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieRestController {

    @Inject
    DirectorService directorService;

    @GET
    public Response list(@PathParam("directorId") String directorId) {
        try {
            return Response.ok(directorService.getMovies(UUID.fromString(directorId))).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    public Response create(@PathParam("directorId") String directorId, Movie movie, @Context UriInfo uriInfo) {
        try {
            Movie created = directorService.createMovie(UUID.fromString(directorId), movie);
            URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
            return Response.created(uri).entity(created).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("{movieId}")
    public Response get(@PathParam("directorId") String directorId, @PathParam("movieId") String movieId) {
        try {
            return Response.ok(directorService.getMovie(UUID.fromString(directorId), UUID.fromString(movieId))).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("{movieId}")
    public Response update(@PathParam("directorId") String directorId,
                           @PathParam("movieId") String movieId,
                           Movie movie) {
        try {
            return Response.ok(directorService.updateMovie(UUID.fromString(directorId), UUID.fromString(movieId), movie)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{movieId}")
    public Response delete(@PathParam("directorId") String directorId, @PathParam("movieId") String movieId) {
        try {
            directorService.deleteMovie(UUID.fromString(directorId), UUID.fromString(movieId));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
