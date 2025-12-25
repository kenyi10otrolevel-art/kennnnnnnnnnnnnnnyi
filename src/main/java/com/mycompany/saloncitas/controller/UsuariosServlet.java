package com.mycompany.saloncitas.controller;

import com.mycompany.saloncitas.dao.UsuarioDAO;
import com.mycompany.saloncitas.dao.impl.UsuarioDAOImpl;
import com.mycompany.saloncitas.model.Usuario;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@WebServlet(name="UsuariosServlet", urlPatterns={"/admin/usuarios"})
public class UsuariosServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String q = request.getParameter("q");

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario u = usuarioDAO.buscarPorId(id);
            request.setAttribute("editUser", u);
        }

        List<Usuario> lista = usuarioDAO.listar(q);
        request.setAttribute("lista", lista);
        request.getRequestDispatcher("/admin/usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                Usuario u = new Usuario();
                u.setUsuario(request.getParameter("usuario"));
                u.setPassword(request.getParameter("password"));
                u.setRol(request.getParameter("rol"));
                u.setNombre(request.getParameter("nombre"));
                u.setEstado(Integer.parseInt(request.getParameter("estado")));

                boolean ok = usuarioDAO.crear(u);
                response.sendRedirect(request.getContextPath() + "/admin/usuarios?msg=" + (ok ? "ok" : "err"));
                return;
            }

            if ("update".equals(action)) {
                Usuario u = new Usuario();
                u.setIdUsuario(Integer.parseInt(request.getParameter("id_usuario")));
                u.setUsuario(request.getParameter("usuario"));
                u.setPassword(request.getParameter("password"));
                u.setRol(request.getParameter("rol"));
                u.setNombre(request.getParameter("nombre"));
                u.setEstado(Integer.parseInt(request.getParameter("estado")));

                boolean ok = usuarioDAO.actualizar(u);
                response.sendRedirect(request.getContextPath() + "/admin/usuarios?msg=" + (ok ? "ok" : "err"));
                return;
            }

            if ("toggle".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int estado = Integer.parseInt(request.getParameter("estado"));
                boolean ok = usuarioDAO.cambiarEstado(id, estado);
                response.sendRedirect(request.getContextPath() + "/admin/usuarios?msg=" + (ok ? "ok" : "err"));
                return;
            }

        } catch (Exception e) {
            System.out.println("ERROR UsuariosServlet: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/usuarios?msg=err");
    }
}
