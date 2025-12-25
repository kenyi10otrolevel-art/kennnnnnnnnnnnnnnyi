package com.mycompany.saloncitas.controller;

import com.mycompany.saloncitas.dao.UsuarioDAO;
import com.mycompany.saloncitas.dao.impl.UsuarioDAOImpl;
import com.mycompany.saloncitas.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Si ya está logueado, lo mandamos a inicio
        HttpSession s = request.getSession(false);
        if (s != null && s.getAttribute("authUser") != null) {
            response.sendRedirect(request.getContextPath() + "/inicio.jsp");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        System.out.println("=== LoginServlet.doPost ===");
        System.out.println("usuario param=[" + usuario + "], password param=[" + password + "]");

        Usuario u = usuarioDAO.login(usuario, password);

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?err=1");
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("authUser", u);
        session.setAttribute("rol", u.getRol());

        System.out.println("SESSION OK => authUser=" + u.getUsuario() + ", rol=" + u.getRol());

        // Puedes mandar directo por rol, o mandar a inicio.jsp (menú)
        response.sendRedirect(request.getContextPath() + "/inicio.jsp");
    }
}
