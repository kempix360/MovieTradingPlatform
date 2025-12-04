package com.app.movietradingplatform.entity.director.repository;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.movie.Movie;
import com.app.movietradingplatform.entity.user.User;
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
public class DirectorRepository {

    private EntityManager em;

    @PersistenceContext(unitName = "moviePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Optional<Director> find(UUID id) {
        return Optional.ofNullable(em.find(Director.class, id));
    }

    public List<Director> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Director> cq = cb.createQuery(Director.class);
        Root<Director> directorRoot = cq.from(Director.class);
        cq.select(directorRoot);
        return em.createQuery(cq).getResultList();
    }

    public void create(Director director) {
        if (director == null) return;
        if (director.getId() == null) director.setId(UUID.randomUUID());
        em.persist(director);
    }

    public void update(Director director) {
        em.merge(director);
    }

    public void delete(Director director) {
        Director managed = em.contains(director) ? director : em.merge(director);
        em.remove(managed);
    }

    public void deleteAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Director> cd = cb.createCriteriaDelete(Director.class);
        em.createQuery(cd).executeUpdate();
    }
}