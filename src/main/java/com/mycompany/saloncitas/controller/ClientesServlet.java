package com.mycompany.saloncitas.controller;

import com.mycompany.saloncitas.dao.ClienteDAO;
import com.mycompany.saloncitas.dao.impl.ClienteDAOImpl;
import com.mycompany.saloncitas.model.Cliente;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@WebServlet(name="ClientesServlet", urlPatterns={"/recepcion/clientes"})
public class ClientesServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String q = request.getParameter("q");

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Cliente c = clienteDAO.buscarPorId(id);
            request.setAttribute("editCliente", c);
        }

        List<Cliente> lista = clienteDAO.listar(q);
        request.setAttribute("lista", lista);

        request.getRequestDispatcher("/recepcion/clientes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                Cliente c = new Cliente();
                c.setDni(request.getParameter("dni"));
                c.setNombre(request.getParameter("nombre"));
                c.setTelefono(request.getParameter("telefono"));
                c.setEmail(request.getParameter("email"));
                c.setEstado(Integer.parseInt(request.getParameter("estado")));

                boolean ok = clienteDAO.crear(c);
                response.sendRedirect(request.getContextPath() + "/recepcion/clientes?msg=" + (ok ? "ok" : "err"));
                return;
            }

            if ("update".equals(action)) {
                Cliente c = new Cliente();
                c.setIdCliente(Integer.parseInt(request.getParameter("id_cliente")));
                c.setDni(request.getParameter("dni"));
                c.setNombre(request.getParameter("nombre"));
                c.setTelefono(request.getParameter("telefono"));
                c.setEmail(request.getParameter("email"));
                c.setEstado(Integer.parseInt(request.getParameter("estado")));

                boolean ok = clienteDAO.actualizar(c);
                response.sendRedirect(request.getContextPath() + "/recepcion/clientes?msg=" + (ok ? "ok" : "err"));
                return;
            }

            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = clienteDAO.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/recepcion/clientes?msg=" + (ok ? "ok" : "err"));
                return;
            }

        } catch (Exception e) {
            System.out.println("ERROR ClientesServlet: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/recepcion/clientes?msg=err");
    }
}
