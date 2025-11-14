package com.app.movietradingplatform.config.db;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.movie.dto.MovieRequest;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DataInitializer {
    private final UserService userService;
    private final DirectorService directorService;
    private final MovieService movieService;
    private final RequestContextController requestContextController;
    private final ServletContext servletContext;
    private final Path avatarDirPath;

    @Inject
    public DataInitializer(UserService userService,
                           DirectorService directorService,
                           MovieService movieService,
                           RequestContextController requestContextController,
                           ServletContext servletContext) {
        this.userService = userService;
        this.directorService = directorService;
        this.movieService = movieService;
        this.requestContextController = requestContextController;
        this.servletContext = servletContext;
        this.avatarDirPath = getAvatarDirPath();
    }

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        boolean activated = requestContextController.activate();
        System.out.println("[INFO] context activated: " + activated);
        try {
            initData();
        } finally {
            if (activated) requestContextController.deactivate();
        }
    }

    @Transactional
    public void initData() {
        initUsers();
        initDirectors();
        initMovies();

        System.out.println("[INFO] Data initialized");
    }

    private void initUsers() {
        User user1 = User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .username("Michael B. Jordan")
                .registrationDate(LocalDate.now())
//                .avatar(readAvatar("michael-b-jordan.png"))
                .build();
        User user2 = User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .username("Jeremy Strong")
                .registrationDate(LocalDate.now())
//                .avatar(readAvatar("jeremy-strong.png"))
                .build();
        User user3 = User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                .username("Mikey Madison")
                .registrationDate(LocalDate.now())
//                .avatar(readAvatar("mikey-madison.png"))
                .build();
        User user4 = User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000004"))
                .username("Ayo Edebiri")
                .registrationDate(LocalDate.now())
//                .avatar(readAvatar("ayo-edebiri.png"))
                .build();
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        saveAvatarFile(user1);
        saveAvatarFile(user2);
        saveAvatarFile(user3);
        saveAvatarFile(user4);
    }

    private void initDirectors() {
        directorService.create(Director.builder()
                .id(UUID.fromString("10000000-0000-0000-0000-000000000001"))
                .name("Sean Baker")
                .description("Known for his independent films with a focus on marginalized communities.")
                .build());

        directorService.create(Director.builder()
                .id(UUID.fromString("10000000-0000-0000-0000-000000000002"))
                .name("Robert Eggers")
                .description("Acclaimed filmmaker and actress known for her work in coming-of-age films.")
                .build());

        directorService.create(Director.builder()
                .id(UUID.fromString("10000000-0000-0000-0000-000000000003"))
                .name("Denis Villeneuve")
                .description("Renowned for his visually stunning and thought-provoking films.")
                .build());
    }

    private void initMovies() {
        Optional<Director> seanBaker = directorService.find(UUID.fromString("10000000-0000-0000-0000-000000000001"));
        Optional<Director> robertEggers = directorService.find(UUID.fromString("10000000-0000-0000-0000-000000000002"));
        Optional<Director> denisVilleneuve = directorService.find(UUID.fromString("10000000-0000-0000-0000-000000000003"));

        Optional<User> user1 = userService.find(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        Optional<User> user2 = userService.find(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        user1.ifPresent(user -> {
            seanBaker.ifPresent(director -> {
                movieService.createWithLinks(new MovieRequest(
                                "The Florida Project",
                                LocalDate.of(2017, 10, 6),
                                List.of(Genre.DRAMA),
                                director.getId(),
                                user.getId())
                );
                movieService.createWithLinks(new MovieRequest(
                                "Tangerine",
                                LocalDate.of(2015, 7, 10),
                                List.of(Genre.COMEDY, Genre.DRAMA),
                                director.getId(),
                                user.getId())
                );
            });

            robertEggers.ifPresent(director -> {
                movieService.createWithLinks(new MovieRequest(
                                "The Witch",
                                LocalDate.of(2015, 1, 23),
                                List.of(Genre.HORROR, Genre.DRAMA),
                                director.getId(),
                                user.getId())
                );
                movieService.createWithLinks(new MovieRequest(
                                "The Lighthouse",
                                LocalDate.of(2019, 5, 19),
                                List.of(Genre.HORROR, Genre.DRAMA),
                                director.getId(),
                                user.getId())
                );
            });
        });

        user2.ifPresent(user -> {
            denisVilleneuve.ifPresent(director -> {
                movieService.createWithLinks(new MovieRequest(
                                "Arrival",
                                LocalDate.of(2016, 9, 1),
                                List.of(Genre.SCI_FI, Genre.DRAMA),
                                director.getId(),
                                user.getId())
                );
            });
        });
    }

    private Path getAvatarDirPath() {
        String avatarParam = servletContext.getInitParameter("avatarDir");
        String base = servletContext.getRealPath("/");
        if (base == null) base = System.getProperty("java.io.tmpdir");
        return Path.of(base, avatarParam);
    }

    private byte[] readAvatar(String fileName) {
        try {
            Path avatarPath = avatarDirPath.resolve(fileName);
            if (Files.exists(avatarPath)) {
                return Files.readAllBytes(avatarPath);
            } else {
                System.err.println("Avatar file not found: " + avatarPath);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading avatar " + fileName, e);
        }
    }

    private void saveAvatarFile(User user) {
        if (user == null || user.getAvatar() == null) return;
        try {
            if (!java.nio.file.Files.exists(avatarDirPath)) {
                java.nio.file.Files.createDirectories(avatarDirPath);
            }
            java.nio.file.Files.write(avatarDirPath.resolve(user.getId().toString() + ".png"), user.getAvatar());
        } catch (IOException e) {
            System.err.println("[WARN] Failed to persist avatar file for user " + user.getId() + ": " + e.getMessage());
        }
    }
}