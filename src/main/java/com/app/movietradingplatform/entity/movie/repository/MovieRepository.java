package com.app.movietradingplatform.entity.movie.repository;

import com.app.movietradingplatform.entity.movie.Movie;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> movie = cq.from(Movie.class);
        cq.select(movie);
        return em.createQuery(cq).getResultList();
    }

    public List<Movie> findByDirector(UUID directorId) {
        if (directorId == null) return List.of();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> movie = cq.from(Movie.class);
        cq.select(movie).where(cb.equal(movie.get("director").get("id"), directorId));
        return em.createQuery(cq).getResultList();
    }

    public List<Movie> findByUser(UUID userId) {
        if (userId == null) return List.of();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> movie = cq.from(Movie.class);
        cq.select(movie).where(cb.equal(movie.get("user").get("id"), userId));
        return em.createQuery(cq).getResultList();
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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Movie> cd = cb.createCriteriaDelete(Movie.class);
        em.createQuery(cd).executeUpdate();
    }
}