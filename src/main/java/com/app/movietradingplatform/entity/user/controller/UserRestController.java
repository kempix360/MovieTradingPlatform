package com.app.movietradingplatform.entity.user.controller;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.dto.UserRequest;
import com.app.movietradingplatform.entity.user.dto.UserResponse;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
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

    UserService userService;
    @EJB
    public void setService(UserService service) {
        this.userService = service;
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .registrationDate(user.getRegistrationDate())
                .build();
    }

    @GET
    @RolesAllowed(UserRoles.ADMIN)
    public Response list() {
        List<UserResponse> response = userService.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Response.ok(response).build();
    }

    @GET
    @Path("{userId}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response get(@PathParam("userId") String id) {
        Optional<User> user = userService.find(UUID.fromString(id));
        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "User not found")).build();
        }
        return Response.ok(toResponse(user.get())).build();
    }

    @POST
    public Response create(UserRequest request, @Context UriInfo uriInfo) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRegistrationDate(LocalDate.now());

        User created = userService.create(user);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(toResponse(created)).build();
    }

    @PUT
    @Path("{userId}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response update(@PathParam("userId") String userId, UserRequest request) {
        try {
            User user = new User();
            user.setId(UUID.fromString(userId));
            user.setUsername(request.getUsername());

            User updated = userService.update(user);
            return Response.ok(toResponse(updated)).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("{userId}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response delete(@PathParam("userId") String userId) {
        try {
            userService.delete(UUID.fromString(userId));
            return Response.noContent().build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @RolesAllowed(UserRoles.ADMIN)
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
