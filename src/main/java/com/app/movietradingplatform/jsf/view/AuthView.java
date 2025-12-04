package com.app.movietradingplatform.jsf.view;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.UserRoles;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ejb.EJB;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Named
public class AuthView {

    @EJB
    private UserService userService;

    public void checkAuth() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return;
        if (fc.getExternalContext().getUserPrincipal() == null) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/view/auth/login.xhtml");
        }
    }

    public UUID getCurrentUserId() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return null;
        Principal p = fc.getExternalContext().getUserPrincipal();
        if (p == null) return null;
        Optional<User> u = userService.findByUsername(p.getName());
        return u.map(User::getId).orElse(null);
    }

    public boolean isAdmin() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return false;
        return fc.getExternalContext().isUserInRole(UserRoles.ADMIN);
    }

    public boolean isOwner(Object ownerId) {
        if (ownerId == null) return false;
        UUID owner;
        try {
            if (ownerId instanceof UUID) owner = (UUID) ownerId;
            else owner = UUID.fromString(ownerId.toString());
        } catch (Exception e) {
            return false;
        }
        UUID cur = getCurrentUserId();
        return cur != null && cur.equals(owner);
    }

    public void checkAdminAuth() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return;
        if (fc.getExternalContext().getUserPrincipal() == null) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/view/auth/login.xhtml");
            return;
        }
        if (!isAdmin()) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/errors/403.xhtml");
        }
    }
}