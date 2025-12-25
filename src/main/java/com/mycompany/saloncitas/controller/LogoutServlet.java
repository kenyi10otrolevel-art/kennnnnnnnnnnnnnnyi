package com.mycompany.saloncitas.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("=== LogoutServlet ===");

        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login.jsp?msg=logout");
    }
}
