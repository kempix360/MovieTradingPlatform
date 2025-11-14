package com.app.movietradingplatform.entity.user.controller;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.dto.UserRequest;
import com.app.movietradingplatform.entity.user.dto.UserResponse;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {

    @Inject
    UserService userService;

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .registrationDate(user.getRegistrationDate())
                .build();
    }

    @GET
    public Response list() {
        List<UserResponse> response = userService.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @POST
    public Response create(UserRequest request, @Context UriInfo uriInfo) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRegistrationDate(LocalDate.now());

        User created = userService.create(user);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(toResponse(created)).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        Optional<User> user = userService.find(UUID.fromString(id));
        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "User not found")).build();
        }
        return Response.ok(toResponse(user.get())).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") String id, UserRequest request) {
        try {
            User user = new User();
            user.setId(UUID.fromString(id));
            user.setUsername(request.getUsername());

            User updated = userService.update(user);
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
            userService.delete(UUID.fromString(id));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    public Response deleteAll() {
        List<User> all = userService.findAll();
        if (all.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "No users found")).build();
        }
        userService.deleteAll();
        return Response.noContent().build();
    }
}
