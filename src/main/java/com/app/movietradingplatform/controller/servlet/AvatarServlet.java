package com.app.movietradingplatform.controller.servlet;

import com.app.movietradingplatform.service.AvatarService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet(name = "AvatarServlet", urlPatterns = "/api/avatars/*")
@MultipartConfig
public class AvatarServlet extends HttpServlet {
    private AvatarService avatarService;

    @Override
    public void init() throws ServletException {
        String dir = getServletContext().getInitParameter("avatarDir");
        try {
            avatarService = new AvatarService(dir);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getPathInfo().substring(1);
        if (!avatarService.exists(userId)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType("image/png");
        try (InputStream in = avatarService.load(userId);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userId = req.getPathInfo().substring(1);
        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file");
            return;
        }
        avatarService.save(userId, filePart.getInputStream());
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getPathInfo().substring(1);
        if (avatarService.delete(userId)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
