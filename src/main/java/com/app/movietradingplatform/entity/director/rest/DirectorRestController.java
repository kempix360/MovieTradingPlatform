package com.app.movietradingplatform.entity.director.rest;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;

@Path("/directors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DirectorRestController {

    @Inject
    DirectorService directorService;

    @GET
    public Response list() {
        return Response.ok(directorService.getAll()).build();
    }

    @POST
    public Response create(Director director, @Context UriInfo uriInfo) {
        Director created = directorService.save(director);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        Director d = directorService.getById(UUID.fromString(id));
        if (d == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(d).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") String id, Director director) {
        try {
            Director updated = directorService.update(UUID.fromString(id), director);
            return Response.ok(updated).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        directorService.delete(UUID.fromString(id));
        return Response.noContent().build();
    }
}
