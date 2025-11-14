package com.app.movietradingplatform.entity.director.service;

import com.app.movietradingplatform.entity.director.Director;
import com.app.movietradingplatform.entity.director.repository.DirectorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.*;

@ApplicationScoped
@NoArgsConstructor()
public class DirectorService {
    private DirectorRepository directorRepository;

    @Inject
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    public Optional<Director> find(UUID id) {
        return directorRepository.find(id);
    }

    @Transactional
    public Director create(Director director) {
        directorRepository.create(director);
        return director;
    }

    @Transactional
    public Director update(Director director) {
        directorRepository.update(director);
        return director;
    }

    @Transactional
    public void delete(UUID id) {
        directorRepository.find(id).ifPresent(directorRepository::delete);
    }

    @Transactional
    public void deleteAll() {
        directorRepository.deleteAll();
    }
}