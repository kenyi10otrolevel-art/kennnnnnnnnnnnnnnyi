package com.mycompany.saloncitas.controller;

import com.mycompany.saloncitas.dao.CitaDAO;
import com.mycompany.saloncitas.dao.ClienteDAO;
import com.mycompany.saloncitas.dao.EmpleadoDAO;
import com.mycompany.saloncitas.dao.impl.CitaDAOImpl;
import com.mycompany.saloncitas.dao.impl.ClienteDAOImpl;
import com.mycompany.saloncitas.dao.impl.EmpleadoDAOImpl;
import com.mycompany.saloncitas.model.Cita;
import com.mycompany.saloncitas.model.Cliente;
import com.mycompany.saloncitas.model.Empleado;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CitasServlet", urlPatterns = {"/recepcion/citas"})
public class CitasServlet extends HttpServlet {

    private final CitaDAO citaDAO = new CitaDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String q = request.getParameter("q");

        // combos
        List<Cliente> clientes = clienteDAO.listar(null);
        request.setAttribute("clientes", clientes);

        List<Empleado> empleados = empleadoDAO.listar(null);
        request.setAttribute("empleados", empleados);

        if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Cita c = citaDAO.buscarPorId(id);
            request.setAttribute("editCita", c);
        }

        List<Cita> lista = citaDAO.listar(q);
        request.setAttribute("lista", lista);

        request.getRequestDispatcher("/recepcion/citas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");
        if (action == null) action = ""; // evita NullPointer

        try {
            if ("create".equalsIgnoreCase(action)) {

                Cita c = new Cita();
                c.setIdCliente(Integer.parseInt(request.getParameter("id_cliente")));
                c.setIdEmpleado(Integer.parseInt(request.getParameter("id_empleado")));
                c.setFecha(request.getParameter("fecha"));
                c.setHora(request.getParameter("hora"));
                c.setEstado(request.getParameter("estado"));

                // total seguro (acepta "12.50" y "12,50")
                double total = parseTotal(request.getParameter("total"));
                c.setTotal(total);

                System.out.println("CITAS CREATE => cliente=" + c.getIdCliente()
                        + " empleado=" + c.getIdEmpleado()
                        + " fecha=" + c.getFecha()
                        + " hora=" + c.getHora()
                        + " estado=" + c.getEstado()
                        + " total=" + c.getTotal());

                boolean ok = citaDAO.crear(c);
                response.sendRedirect(request.getContextPath() + "/recepcion/citas?msg=" + (ok ? "ok" : "err"));
                return;
            }

            if ("update".equalsIgnoreCase(action)) {

                Cita c = new Cita();
                c.setIdCita(Integer.parseInt(request.getParameter("id_cita")));
                c.setIdCliente(Integer.parseInt(request.getParameter("id_cliente")));
                c.setIdEmpleado(Integer.parseInt(request.getParameter("id_empleado")));
                c.setFecha(request.getParameter("fecha"));
                c.setHora(request.getParameter("hora"));
                c.setEstado(request.getParameter("estado"));

                double total = parseTotal(request.getParameter("total"));
                c.setTotal(total);

                System.out.println("CITAS UPDATE => id=" + c.getIdCita()
                        + " cliente=" + c.getIdCliente()
                        + " empleado=" + c.getIdEmpleado()
                        + " fecha=" + c.getFecha()
                        + " hora=" + c.getHora()
                        + " estado=" + c.getEstado()
                        + " total=" + c.getTotal());

                boolean ok = citaDAO.actualizar(c);
                response.sendRedirect(request.getContextPath() + "/recepcion/citas?msg=" + (ok ? "ok" : "err"));
                return;
            }

            if ("delete".equalsIgnoreCase(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                System.out.println("CITAS DELETE => id=" + id);

                boolean ok = citaDAO.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/recepcion/citas?msg=" + (ok ? "ok" : "err"));
                return;
            }

            // Si llega un action desconocido
            System.out.println("CITAS POST => action desconocida: [" + action + "]");
            response.sendRedirect(request.getContextPath() + "/recepcion/citas?msg=err");
            return;

        } catch (Exception e) {
            System.out.println("ERROR CitasServlet action=[" + action + "]: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/recepcion/citas?msg=err");
    }

    // ===== Helper mínimo para evitar NumberFormatException por comas/empty =====
    private double parseTotal(String totalStr) {
        try {
            if (totalStr == null) return 0.00;
            totalStr = totalStr.trim();
            if (totalStr.isEmpty()) return 0.00;
            totalStr = totalStr.replace(",", "."); // por si escriben 12,50
            return Double.parseDouble(totalStr);
        } catch (Exception e) {
            System.out.println("WARN parseTotal: valor inválido [" + totalStr + "], usando 0.00");
            return 0.00;
        }
    }
}
