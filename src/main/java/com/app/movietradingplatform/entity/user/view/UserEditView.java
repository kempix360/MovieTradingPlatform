package com.app.movietradingplatform.entity.user.view;

import com.app.movietradingplatform.entity.user.User;
import com.app.movietradingplatform.entity.user.service.UserService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Named("userEditView")
@ViewScoped
public class UserEditView implements Serializable {
    @Inject
    private UserService userService;

    private UUID id;
    private User user;

    public void loadUser() {
        if (id != null) {
            user = userService.getById(id);
        }
    }

    public String update() {
        userService.update(user);
        return "/view/user/user_list.xhtml?faces-redirect=true";
    }
}
