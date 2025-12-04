package com.app.movietradingplatform.entity.user.repository;

import com.app.movietradingplatform.entity.user.User;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class UserRepository {

    private EntityManager em;

    @PersistenceContext(unitName = "moviePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Optional<User> find(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public List<User> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        cq.select(user);
        return em.createQuery(cq).getResultList();
    }

    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        cq.select(user).where(cb.equal(user.get("username"), username));
        try {
            return Optional.of(em.createQuery(cq).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public void create(User user) {
        em.persist(user);
    }

    public void update(User user) {
        em.merge(user);
    }

    public void delete(User user) {
        User managed = em.contains(user) ? user : em.merge(user);
        em.remove(managed);
    }

    public void deleteAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
        em.createQuery(cd).executeUpdate();
    }
}