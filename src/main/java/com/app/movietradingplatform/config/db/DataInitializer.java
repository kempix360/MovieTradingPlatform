package com.app.movietradingplatform.config.db;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.service.DirectorService;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.movie.service.MovieService;
import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * EJB singleton can be forced to start automatically when application starts. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@NoArgsConstructor
@DependsOn("AdminServiceInitializer")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
@RunAs(UserRoles.ADMIN)
@Log
public class DataInitializer {
    private UserService userService;
    private DirectorService directorService;
    private MovieService movieService;

    @EJB
    public void setUserService(UserService service) {
        this.userService = service;
    }
    @EJB
    public void setDirectorService(DirectorService service) {
        this.directorService = service;
    }
    @EJB
    public void setMovieService(MovieService service) {
        this.movieService = service;
    }

    @PostConstruct
    @SneakyThrows
    private void init() {
        User michaelBJordan = User.builder()
                .id(UUID.fromString("631aff3b-a99d-4a64-a397-f71eba999077"))
                .username("Michael B. Jordan")
                .password("michaelbjordan")
                .registrationDate(LocalDate.now())
                .roles(List.of(UserRoles.USER))
                .build();
        User jeremyStrong = User.builder()
                .id(UUID.fromString("416842e3-84d4-404d-ad22-810ba3bcaa3e"))
                .username("Jeremy Strong")
                .password("jeremystrong")
                .registrationDate(LocalDate.now())
                .roles(List.of(UserRoles.USER))
                .build();
        User mikeyMadison = User.builder()
                .id(UUID.fromString("fb3b5e04-0573-47bf-96d6-ca6ab430e17a"))
                .username("Mikey Madison")
                .password("mikeymadison")
                .registrationDate(LocalDate.now())
                .roles(List.of(UserRoles.USER))
                .build();
        User ayoEdebiri = User.builder()
                .id(UUID.fromString("4722bebe-1277-4ce0-8500-09398f8d8782"))
                .username("Ayo Edebiri")
                .password("ayoedebiri")
                .registrationDate(LocalDate.now())
                .roles(List.of(UserRoles.USER))
                .build();
        userService.update(michaelBJordan);
        userService.update(jeremyStrong);
        userService.update(mikeyMadison);
        userService.update(ayoEdebiri);

        Director seanBaker = Director.builder()
                .id(UUID.fromString("3427682e-aebc-4589-8061-52e124082ce2"))
                .name("Sean Baker")
                .description("Known for his independent films with a focus on marginalized communities.")
                .build();
        Director robertEggers = Director.builder()
                .id(UUID.fromString("a39e9326-f992-4df0-b9a0-381d7116ed80"))
                .name("Robert Eggers")
                .description("Acclaimed filmmaker and actress known for her work in coming-of-age films.")
                .build();
        Director denisVilleneuve = Director.builder()
                .id(UUID.fromString("373338b9-eaf5-4448-908c-77a5f50a49a2"))
                .name("Denis Villeneuve")
                .description("Renowned for his visually stunning and thought-provoking films.")
                .build();
        directorService.update(seanBaker);
        directorService.update(robertEggers);
        directorService.update(denisVilleneuve);

        Movie theFloridaProject = Movie.builder()
                .id(UUID.fromString("243526c0-de68-4334-85a4-37656a046e90"))
                .title("The Florida Project")
                .releaseDate(LocalDate.of(2017, 10, 6))
                .genres(List.of(Genre.DRAMA))
                .director(seanBaker)
                .user(michaelBJordan)
                .build();
        Movie tangerine = Movie.builder()
                .id(UUID.fromString("6c2b18b1-7818-4efc-a815-18bb7fc0aa52"))
                .title("Tangerine")
                .releaseDate(LocalDate.of(2015, 7, 10))
                .genres(List.of(Genre.COMEDY, Genre.DRAMA))
                .director(seanBaker)
                .user(michaelBJordan)
                .build();
        Movie theWitch = Movie.builder()
                .id(UUID.fromString("7a8ca089-76ef-437a-9218-e220dc89c0f5"))
                .title("The VVitch")
                .releaseDate(LocalDate.of(2015, 1, 23))
                .genres(List.of(Genre.HORROR, Genre.DRAMA))
                .director(robertEggers)
                .user(jeremyStrong)
                .build();
        Movie theLighthouse = Movie.builder()
                .id(UUID.fromString("7298ac20-daf6-4910-9bb6-561ee9f4d26f"))
                .title("The Lighthouse")
                .releaseDate(LocalDate.of(2019, 5, 19))
                .genres(List.of(Genre.HORROR, Genre.DRAMA))
                .director(robertEggers)
                .user(jeremyStrong)
                .build();
        Movie arrival = Movie.builder()
                .id(UUID.fromString("276e17fc-a7e2-4998-a58c-f4ff1a23206b"))
                .title("Arrival")
                .releaseDate(LocalDate.of(2016, 9, 1))
                .genres(List.of(Genre.SCI_FI, Genre.DRAMA))
                .director(denisVilleneuve)
                .user(mikeyMadison)
                .build();
        movieService.update(theFloridaProject);
                // link movie in Director and User objects so bidirectional relations are persisted
                if (theFloridaProject.getDirector() != null) {
                        Director d = theFloridaProject.getDirector();
                        if (d.getMovies() == null) d.setMovies(new java.util.ArrayList<>());
                        d.getMovies().add(theFloridaProject);
                        directorService.update(d);
                }
                if (theFloridaProject.getUser() != null) {
                        User u = theFloridaProject.getUser();
                        if (u.getOwnedMovies() == null) u.setOwnedMovies(new java.util.ArrayList<>());
                        u.getOwnedMovies().add(theFloridaProject);
                        userService.update(u);
                }
        movieService.update(tangerine);
                if (tangerine.getDirector() != null) {
                        Director d = tangerine.getDirector();
                        if (d.getMovies() == null) d.setMovies(new java.util.ArrayList<>());
                        d.getMovies().add(tangerine);
                        directorService.update(d);
                }
                if (tangerine.getUser() != null) {
                        User u = tangerine.getUser();
                        if (u.getOwnedMovies() == null) u.setOwnedMovies(new java.util.ArrayList<>());
                        u.getOwnedMovies().add(tangerine);
                        userService.update(u);
                }
        movieService.update(theWitch);
                if (theWitch.getDirector() != null) {
                        Director d = theWitch.getDirector();
                        if (d.getMovies() == null) d.setMovies(new java.util.ArrayList<>());
                        d.getMovies().add(theWitch);
                        directorService.update(d);
                }
                if (theWitch.getUser() != null) {
                        User u = theWitch.getUser();
                        if (u.getOwnedMovies() == null) u.setOwnedMovies(new java.util.ArrayList<>());
                        u.getOwnedMovies().add(theWitch);
                        userService.update(u);
                }
        movieService.update(theLighthouse);
                if (theLighthouse.getDirector() != null) {
                        Director d = theLighthouse.getDirector();
                        if (d.getMovies() == null) d.setMovies(new java.util.ArrayList<>());
                        d.getMovies().add(theLighthouse);
                        directorService.update(d);
                }
                if (theLighthouse.getUser() != null) {
                        User u = theLighthouse.getUser();
                        if (u.getOwnedMovies() == null) u.setOwnedMovies(new java.util.ArrayList<>());
                        u.getOwnedMovies().add(theLighthouse);
                        userService.update(u);
                }
        movieService.update(arrival);
                if (arrival.getDirector() != null) {
                        Director d = arrival.getDirector();
                        if (d.getMovies() == null) d.setMovies(new java.util.ArrayList<>());
                        d.getMovies().add(arrival);
                        directorService.update(d);
                }
                if (arrival.getUser() != null) {
                        User u = arrival.getUser();
                        if (u.getOwnedMovies() == null) u.setOwnedMovies(new java.util.ArrayList<>());
                        u.getOwnedMovies().add(arrival);
                        userService.update(u);
                }
    }
}