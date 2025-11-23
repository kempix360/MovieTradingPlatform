package com.app.movietradingplatform.entity.user.service;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.repository.UserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class UserService {
    private UserRepository userRepository;
    private Pbkdf2PasswordHash passwordHash;
    private SecurityContext securityContext;

    @Inject
    public UserService(UserRepository userRepository,
                       @SuppressWarnings("CdiInjectionPointsInspection") Pbkdf2PasswordHash passwordHash,
                       SecurityContext securityContext) {
        this.userRepository = userRepository;
        this.passwordHash = passwordHash;
        this.securityContext = securityContext;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<User> find(UUID id) {
        return userRepository.find(id);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        userRepository.create(user);
        return user;
    }

    public User update(User user) {
        userRepository.update(user);
        return user;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void delete(UUID id) {
        userRepository.delete(userRepository.find(id).orElseThrow());
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @PermitAll
    public boolean verify(String username, String password) {
        return findByUsername(username)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

    public void updateAvatar(UUID id, InputStream is) {
        userRepository.find(id).ifPresent(user -> {
            try {
                user.setAvatar(is.readAllBytes());
                userRepository.update(user);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    /**
     * Updates last login time for current caller principal.
     */
//    @PermitAll
//    public void updateCallerPrincipalLastLoginDateTime() {
//        findCallerPrincipal().ifPresent(principal -> principal.setLastLoginDateTime(LocalDateTime.now()));
//    }

    /**
     * @return logged user entity
     */
    public Optional<User> findCallerPrincipal() {
        if (securityContext.getCallerPrincipal() != null) {
            return findByUsername(securityContext.getCallerPrincipal().getName());
        } else {
            return Optional.empty();
        }
    }

}
