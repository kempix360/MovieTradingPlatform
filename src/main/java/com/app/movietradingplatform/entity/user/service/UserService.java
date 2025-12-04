package com.app.movietradingplatform.entity.user.service;

import com.app.movietradingplatform.config.interceptor.binding.LogAccess;
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

import java.security.Principal;
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

    @PermitAll
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @PermitAll
    public Optional<User> find(UUID id) {
        return userRepository.find(id);
    }

    @PermitAll
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @PermitAll
    @LogAccess("CREATE USER")
    public User create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        userRepository.create(user);
        return user;
    }

    @PermitAll
    @LogAccess("UPDATE USER")
    public User update(User user) {
        userRepository.update(user);
        return user;
    }

    @PermitAll
    @LogAccess("DELETE USER")
    public void delete(UUID id) {
        userRepository.delete(userRepository.find(id).orElseThrow());
    }

    @RolesAllowed(UserRoles.ADMIN)
    @LogAccess("DELETE ALL USERS")
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public boolean verifyCallerPrincipal() {
        if (securityContext != null && !securityContext.isCallerInRole(UserRoles.ADMIN)) {
            Principal principal = securityContext.getCallerPrincipal();
            if (principal == null)
                return false;
            Optional<User> userOpt = findByUsername(principal.getName());
            if (userOpt.isEmpty())
                return false;
            User caller = userOpt.get();
            return caller.getUsername().equals(principal.getName());
        }
        return true;
    }
}
