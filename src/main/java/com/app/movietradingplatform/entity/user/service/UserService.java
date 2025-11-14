package com.app.movietradingplatform.entity.user.service;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@ApplicationScoped
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> find(UUID id) {
        return userRepository.find(id);
    }

    @Transactional
    public User create(User user) {
        userRepository.create(user);
        return user;
    }

    @Transactional
    public User update(User user) {
        userRepository.update(user);
        return user;
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.delete(userRepository.find(id).orElseThrow());
    }

    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void updateAvatar(UUID id, InputStream is) {
        userRepository.find(id).ifPresent(user -> {
            try {
                user.setAvatar(is.readAllBytes());
                userRepository.update(user);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
}
