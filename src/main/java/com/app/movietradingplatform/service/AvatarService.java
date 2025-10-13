package com.app.movietradingplatform.service;

import java.io.*;
import java.nio.file.*;

public class AvatarService {
    private final Path baseDir;

    public AvatarService(String dir) throws IOException {
        this.baseDir = Paths.get(dir);
        if (!Files.exists(baseDir)) Files.createDirectories(baseDir);
    }

    public Path getAvatarPath(String userId) {
        return baseDir.resolve(userId + ".png");
    }

    public boolean exists(String userId) {
        return Files.exists(getAvatarPath(userId));
    }

    public void save(String userId, InputStream input) throws IOException {
        Files.copy(input, getAvatarPath(userId), StandardCopyOption.REPLACE_EXISTING);
    }

    public InputStream load(String userId) throws IOException {
        return Files.newInputStream(getAvatarPath(userId));
    }

    public boolean delete(String userId) throws IOException {
        return Files.deleteIfExists(getAvatarPath(userId));
    }
}
