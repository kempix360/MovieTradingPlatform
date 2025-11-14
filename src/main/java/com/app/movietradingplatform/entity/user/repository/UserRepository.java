package com.app.movietradingplatform.entity.user.repository;

import com.app.movietradingplatform.entity.user.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
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
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Transactional
    public void create(User user) {
        if (user == null) return;
        em.persist(user);
    }

    @Transactional
    public void update(User user) {
        em.merge(user);
    }

    @Transactional
    public void delete(User user) {
        User managed = em.contains(user) ? user : em.merge(user);
        em.remove(managed);
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("DELETE FROM User").executeUpdate();
    }

}