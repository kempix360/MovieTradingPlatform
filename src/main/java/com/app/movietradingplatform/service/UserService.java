package com.app.movietradingplatform.service;

import com.app.movietradingplatform.entities.User;
import java.util.*;

public class UserService {
    private static final Map<UUID, User> users = new LinkedHashMap<>();

    static {
        // Load test users
        users.put(UUID.randomUUID(), new User("Michael B. Jordan"));
        users.put(UUID.randomUUID(), new User("Jeremy Strong"));
        users.put(UUID.randomUUID(), new User("Mikey Madison"));
        users.put(UUID.randomUUID(), new User("Ayo Edebiri"));
    }

    public List<User> getAll() { return new ArrayList<>(users.values()); }
    public User getById(UUID id) { return users.get(id); }
}
