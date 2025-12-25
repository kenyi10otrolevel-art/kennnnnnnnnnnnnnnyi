package com.mycompany.saloncitas.dao.impl;

import com.mycompany.saloncitas.config.DBConnection;
import com.mycompany.saloncitas.dao.CitaDAO;
import com.mycompany.saloncitas.model.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOImpl implements CitaDAO {

    @Override
    public List<Cita> listar(String q) {
        List<Cita> lista = new ArrayList<>();

        // Vista para mostrar cliente/empleado/especialidad
        String sql =
            "SELECT id_cita, fecha, hora, estado, total, cliente, empleado, especialidad " +
            "FROM vw_citas " +
            "WHERE (? IS NULL OR cliente LIKE ? OR empleado LIKE ? OR estado LIKE ?) " +
            "ORDER BY fecha DESC, hora DESC, id_cita DESC";

        System.out.println("CitaDAO.listar q=[" + q + "]");

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (q == null || q.trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);
            } else {
                String like = "%" + q.trim() + "%";
                // ✅ mínimo: que todos usen el mismo patrón LIKE
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    c.setFecha(rs.getString("fecha"));
                    c.setHora(rs.getString("hora"));
                    c.setEstado(rs.getString("estado"));
                    c.setTotal(rs.getDouble("total"));
                    c.setClienteNombre(rs.getString("cliente"));
                    c.setEmpleadoNombre(rs.getString("empleado"));
                    c.setEspecialidad(rs.getString("especialidad"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR CitaDAO.listar: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Cita buscarPorId(int id) {
        // Edit: de tb_cita para obtener IDs
        String sql =
            "SELECT id_cita, id_cliente, id_empleado, fecha, hora, estado, total " +
            "FROM tb_cita WHERE id_cita=?";

        System.out.println("CitaDAO.buscarPorId id=" + id);

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setIdEmpleado(rs.getInt("id_empleado"));
                    c.setFecha(rs.getString("fecha"));
                    c.setHora(rs.getString("hora"));
                    c.setEstado(rs.getString("estado"));
                    c.setTotal(rs.getDouble("total"));
                    return c;
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR CitaDAO.buscarPorId: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean crear(Cita c) {
        String sql =
            "INSERT INTO tb_cita(id_cliente, id_empleado, fecha, hora, estado, total) " +
            "VALUES(?,?,?,?,?,?)";

        System.out.println("CitaDAO.crear => cliente=" + c.getIdCliente()
                + " empleado=" + c.getIdEmpleado()
                + " fecha=" + c.getFecha()
                + " hora=" + c.getHora()
                + " estado=" + c.getEstado()
                + " total=" + c.getTotal());

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            validarRequeridos(c, "CREAR");

            ps.setInt(1, c.getIdCliente());
            ps.setInt(2, c.getIdEmpleado());
            ps.setDate(3, Date.valueOf(c.getFecha().trim()));
            ps.setTime(4, Time.valueOf(normalizarHora(c.getHora())));
            ps.setString(5, c.getEstado());
            ps.setDouble(6, c.getTotal());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ERROR CitaDAO.crear: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Cita c) {
        String sql =
            "UPDATE tb_cita SET id_cliente=?, id_empleado=?, fecha=?, hora=?, estado=?, total=? " +
            "WHERE id_cita=?";

        System.out.println("CitaDAO.actualizar => id=" + c.getIdCita()
                + " cliente=" + c.getIdCliente()
                + " empleado=" + c.getIdEmpleado()
                + " fecha=" + c.getFecha()
                + " hora=" + c.getHora()
                + " estado=" + c.getEstado()
                + " total=" + c.getTotal());

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (c.getIdCita() <= 0) {
                throw new IllegalArgumentException("ID_CITA inválido para ACTUALIZAR.");
            }
            validarRequeridos(c, "ACTUALIZAR");

            ps.setInt(1, c.getIdCliente());
            ps.setInt(2, c.getIdEmpleado());
            ps.setDate(3, Date.valueOf(c.getFecha().trim()));
            ps.setTime(4, Time.valueOf(normalizarHora(c.getHora())));
            ps.setString(5, c.getEstado());
            ps.setDouble(6, c.getTotal());
            ps.setInt(7, c.getIdCita());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ERROR CitaDAO.actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_cita WHERE id_cita=?";

        System.out.println("CitaDAO.eliminar id=" + id);

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ERROR CitaDAO.eliminar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM tb_cita";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (Exception e) {
            System.out.println("ERROR CitaDAO.contar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // ===== Helpers mínimos =====

    private void validarRequeridos(Cita c, String modo) {
        if (c.getIdCliente() <= 0) {
            throw new IllegalArgumentException("[" + modo + "] id_cliente inválido.");
        }
        if (c.getIdEmpleado() <= 0) {
            throw new IllegalArgumentException("[" + modo + "] id_empleado inválido.");
        }
        if (c.getFecha() == null || c.getFecha().trim().isEmpty()) {
            throw new IllegalArgumentException("[" + modo + "] fecha requerida.");
        }
        if (c.getHora() == null || c.getHora().trim().isEmpty()) {
            throw new IllegalArgumentException("[" + modo + "] hora requerida.");
        }
        if (c.getEstado() == null || c.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("[" + modo + "] estado requerido.");
        }
        // total NOT NULL en BD, pero si viene 0.00 es válido
    }

    // Convierte "HH:mm" a "HH:mm:00" para Time.valueOf()
    private String normalizarHora(String hora) {
        if (hora == null) return "00:00:00";
        hora = hora.trim();
        if (hora.length() == 5) return hora + ":00";
        if (hora.length() == 8) return hora;
        // si llega raro, al menos logueamos
        System.out.println("WARN normalizarHora: formato no esperado [" + hora + "], usando 00:00:00");
        return "00:00:00";
    }
}
