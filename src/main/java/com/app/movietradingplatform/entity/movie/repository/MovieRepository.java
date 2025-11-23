package com.app.movietradingplatform.entity.movie.repository;

import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class MovieRepository {

    private EntityManager em;

    @PersistenceContext(unitName = "moviePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Optional<Movie> find(UUID id) {
        return Optional.ofNullable(em.find(Movie.class, id));
    }

    public List<Movie> findAll() {
        return em.createQuery("SELECT s FROM Movie s", Movie.class).getResultList();
    }

    public List<Movie> findByDirector(UUID directorId) {
        if (directorId == null) return List.of();
        return em.createQuery("SELECT m FROM Movie m WHERE m.director.id = :did", Movie.class)
                .setParameter("did", directorId)
                .getResultList();
    }

    public List<Movie> findByUser(UUID userId) {
        if (userId == null) return List.of();
        return em.createQuery("SELECT m FROM Movie m WHERE m.user.id = :uid", Movie.class)
                .setParameter("uid", userId)
                .getResultList();
    }

    public void create(Movie movie) {
        if (movie == null) return;
        em.persist(movie);
    }

    public void update(Movie movie) {
        em.merge(movie);
    }

    public void delete(Movie movie) {
        Movie managed = em.contains(movie) ? movie : em.merge(movie);
        em.remove(managed);
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM Movie").executeUpdate();
    }
}