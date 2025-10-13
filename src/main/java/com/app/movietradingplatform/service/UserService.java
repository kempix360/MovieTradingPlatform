package com.app.movietradingplatform.service;

import com.app.movietradingplatform.entities.User;

import java.time.LocalDate;
import java.util.*;

public class UserService {
    private static final List<User> users = new ArrayList<>();

    static {
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
}
