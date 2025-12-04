package com.app.movietradingplatform.entity.movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.enums.Genre;
import com.app.movietradingplatform.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movie implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private UUID id;

    private String title;
    private LocalDate releaseDate;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Genre> genres = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private Director director;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}