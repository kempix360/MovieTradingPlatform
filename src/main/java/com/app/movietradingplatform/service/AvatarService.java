package com.app.movietradingplatform.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@ApplicationScoped
public class AvatarService {
    private Path baseDir;

    @Inject
    private ServletContext context;

    @Inject
    public AvatarService() {
    }

    @PostConstruct
    public void initialize() {
        try {
            String dir = context.getInitParameter("avatarDir");
            this.baseDir = Paths.get(dir);
            if (!Files.exists(baseDir)) {
                Files.createDirectories(baseDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create avatar directory", e);
        }
    }

    public Path getAvatarPath(UUID userId) {
        return baseDir.resolve(userId + ".png");
    }

    public boolean exists(UUID userId) {
        return Files.exists(getAvatarPath(userId));
    }

    public InputStream load(UUID userId) throws IOException {
        return Files.newInputStream(getAvatarPath(userId));
    }

    public void save(UUID userId, InputStream input) throws IOException {
        Files.copy(input, getAvatarPath(userId));
    }

    public void update(UUID userId, InputStream input) throws IOException {
        Files.copy(input, getAvatarPath(userId), StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean delete(UUID userId) throws IOException {
        return Files.deleteIfExists(getAvatarPath(userId));
    }
}
