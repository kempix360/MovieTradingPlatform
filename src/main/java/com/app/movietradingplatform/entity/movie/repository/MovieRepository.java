package com.app.movietradingplatform.entity.movie.repository;

import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
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

    @Transactional
    public void create(Movie song) {
        if (song == null) return;
        em.persist(song);
    }

    @Transactional
    public void update(Movie song) {
        em.merge(song);
    }

    @Transactional
    public void delete(Movie song) {
        Movie managed = em.contains(song) ? song : em.merge(song);
        em.remove(managed);
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("DELETE FROM Movie").executeUpdate();
    }
}