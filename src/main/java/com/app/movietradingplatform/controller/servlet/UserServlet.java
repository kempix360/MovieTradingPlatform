package com.app.movietradingplatform.controller.servlet;

import com.app.movietradingplatform.entities.User;
import com.app.movietradingplatform.service.UserService;
import com.app.movietradingplatform.utils.JsonUtil;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "UserServlet", urlPatterns = "/api/users/*")
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            JsonUtil.writeJson(resp, userService.getAll());
        } else {
            try {
                UUID id = UUID.fromString(path.substring(1));
                User u = userService.getById(id);
                if (u == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    JsonUtil.writeJson(resp, u);
                }
            } catch (IllegalArgumentException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
