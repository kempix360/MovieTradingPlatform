package com.app.movietradingplatform.entity.user.view;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.AvatarService;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Named
@ViewScoped
public class UserDetailsView implements Serializable {
    @Inject
    private UserService userService;
    @Inject
    private AvatarService avatarService;

    private UUID id;
    private User user;
    private Part avatarFile;

    public void loadUser() {
        if (id != null) {
            user = userService.getById(id);
        }
    }

    public void uploadAvatar() {
        if (avatarFile != null) {
            try (InputStream input = avatarFile.getInputStream()) {
                avatarService.save(id, input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String deleteMovie(UUID movieId) {
        userService.deleteMovie(id, movieId);
        return "user_details?faces-redirect=true&amp;id=" + id;
    }
}