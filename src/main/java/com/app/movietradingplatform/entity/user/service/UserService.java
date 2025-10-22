package com.app.movietradingplatform.entity.user.service;

import com.app.movietradingplatform.entity.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;

@ApplicationScoped
public class UserService {
    private final List<User> users = new ArrayList<>();

    @Inject
    public UserService() {
        // Load test users
        users.add(new User(UUID.fromString("4e8d7e31-1b9e-4882-a917-5d765c08493f"), "Michael B. Jordan"));
        users.add(new User(UUID.fromString("4128c40e-1a95-45db-84da-fb14ceb1c870"), "Jeremy Strong"));
        users.add(new User(UUID.fromString("791368b3-31ca-43ff-a327-26fc0f1adea3"), "Mikey Madison"));
        users.add(new User(UUID.fromString("8100b921-4b6b-4362-b9d4-53c3fd64896f"), "Ayo Edebiri"));
    }

    public List<User> getAll() { return new ArrayList<>(users); }

    public User getById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        users.add(user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        // Remove existing user first
        boolean userRemoved = users.removeIf(u -> u.getId().equals(user.getId()));
        if (!userRemoved) {
            throw new NoSuchElementException("User not found with ID: " + user.getId());
        }

        users.add(user);
        return user;
    }

    public boolean delete(UUID id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
}
