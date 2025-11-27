package com.app.movietradingplatform.entity.director.controller;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.dto.DirectorRequest;
import com.app.movietradingplatform.entity.director.dto.DirectorResponse;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.user.UserRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/directors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DirectorRestController {

    DirectorService directorService;
    @EJB
    public void setService(DirectorService service) {
        this.directorService = service;
    }

    private DirectorResponse toResponse(Director director) {
        return DirectorResponse.builder()
                .id(director.getId())
                .name(director.getName())
                .description(director.getDescription())
                .build();
    }

    @GET
    @RolesAllowed(UserRoles.USER)
    public Response listAllDirectors() {
        List<DirectorResponse> response = directorService.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @GET
    @Path("{directorId}")
    @PermitAll
    public Response get(@PathParam("directorId") String directorId) {
        Optional<Director> director = directorService.find(UUID.fromString(directorId));
        if (director.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Director not found")).build();
        }
        return Response.ok(toResponse(director.get())).build();
    }

    @POST
    @RolesAllowed(UserRoles.ADMIN)
    public Response create(DirectorRequest request, @Context UriInfo uriInfo) {
        Director director = new Director();
        director.setName(request.getName());
        director.setDescription(request.getDescription());

        Director created = directorService.create(director);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(toResponse(created)).build();
    }

    @PUT
    @Path("{directorId}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response update(@PathParam("directorId") String directorId, DirectorRequest request) {
        Optional<Director> director = directorService.find(UUID.fromString(directorId));
        if (director.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Director not found")).build();
        }
        try {
            Director newDirector = new Director();
            newDirector.setId(UUID.fromString(directorId));
            newDirector.setName(request.getName());
            newDirector.setDescription(request.getDescription());

            Director updated = directorService.update(newDirector);
            return Response.ok(toResponse(updated)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{directorId}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response delete(@PathParam("directorId") String directorId) {
        Optional<Director> director = directorService.find(UUID.fromString(directorId));
        if (director.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Director not found")).build();
        }
        try {
            directorService.delete(UUID.fromString(directorId));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @RolesAllowed(UserRoles.ADMIN)
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
