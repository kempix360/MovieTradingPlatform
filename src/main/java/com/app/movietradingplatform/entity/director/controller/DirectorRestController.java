package com.app.movietradingplatform.entity.director.controller;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.dto.DirectorRequest;
import com.app.movietradingplatform.entity.director.dto.DirectorResponse;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/directors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DirectorRestController {

    @Inject
    DirectorService directorService;

    private DirectorResponse toResponse(Director director) {
        return DirectorResponse.builder()
                .id(director.getId())
                .name(director.getName())
                .description(director.getDescription())
                .build();
    }

    @GET
    public Response list() {
        List<DirectorResponse> response = directorService.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @POST
    public Response create(DirectorRequest request, @Context UriInfo uriInfo) {
        Director director = new Director();
        director.setName(request.getName());
        director.setDescription(request.getDescription());

        Director created = directorService.create(director);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(toResponse(created)).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        Optional<Director> director = directorService.find(UUID.fromString(id));
        if (director.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Director not found")).build();
        }
        return Response.ok(toResponse(director.get())).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") String id, DirectorRequest request) {
        try {
            Director director = new Director();
            director.setId(UUID.fromString(id));
            director.setName(request.getName());
            director.setDescription(request.getDescription());

            Director updated = directorService.update(director);
            return Response.ok(toResponse(updated)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            directorService.delete(UUID.fromString(id));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    public Response deleteAll() {
        List<Director> all = directorService.findAll();
        if (all.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "No directors found")).build();
        }
        directorService.deleteAll();
        return Response.noContent().build();
    }
}
