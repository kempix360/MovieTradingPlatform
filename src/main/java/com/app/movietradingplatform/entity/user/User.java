package com.app.movietradingplatform.entity.user;

import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private UUID id;

    private String username;
    @ToString.Exclude
    private String password;
    private LocalDate registrationDate;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] avatar;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Movie> ownedMovies = new ArrayList<>();

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
}