package com.app.movietradingplatform.entity.director.repository;

import com.app.movietradingplatform.entity.director.Director;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        return em.createQuery("SELECT a FROM Director a", Director.class).getResultList();
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
        em.createQuery("DELETE FROM Director").executeUpdate();
    }
}