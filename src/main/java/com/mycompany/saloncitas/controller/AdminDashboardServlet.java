package com.mycompany.saloncitas.controller;

import com.mycompany.saloncitas.dao.UsuarioDAO;
import com.mycompany.saloncitas.dao.ClienteDAO;
import com.mycompany.saloncitas.dao.CitaDAO;
import com.mycompany.saloncitas.dao.impl.UsuarioDAOImpl;
import com.mycompany.saloncitas.dao.impl.ClienteDAOImpl;
import com.mycompany.saloncitas.dao.impl.CitaDAOImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

@WebServlet(name="AdminDashboardServlet", urlPatterns={"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final CitaDAO citaDAO = new CitaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int usuarios = usuarioDAO.contar();
        int clientes = clienteDAO.contar();
        int citas = citaDAO.contar();

        request.setAttribute("usuariosCount", usuarios);
        request.setAttribute("clientesCount", clientes);
        request.setAttribute("citasCount", citas);

        System.out.println("Dashboard => usuarios=" + usuarios + ", clientes=" + clientes + ", citas=" + citas);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
