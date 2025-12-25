package com.mycompany.saloncitas.dao.impl;

import com.mycompany.saloncitas.config.DBConnection;
import com.mycompany.saloncitas.dao.EmpleadoDAO;
import com.mycompany.saloncitas.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    @Override
    public List<Empleado> listar(String q) {
        List<Empleado> lista = new ArrayList<>();

        // Importante: tb_empleado en tu BD tiene columnas:
        // id_empleado, nombres, especialidad, telefono, estado, creado_en
        String sql =
            "SELECT id_empleado, nombres, especialidad, telefono, estado " +
            "FROM tb_empleado " +
            "WHERE estado = 1 AND (? IS NULL OR nombres LIKE ? OR especialidad LIKE ?) " +
            "ORDER BY nombres ASC";

        System.out.println("EmpleadoDAO.listar q=" + q);

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (q == null || q.trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
            } else {
                String like = "%" + q.trim() + "%";
                ps.setString(1, q);
                ps.setString(2, like);
                ps.setString(3, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado e = new Empleado();
                    e.setIdEmpleado(rs.getInt("id_empleado"));
                    e.setNombres(rs.getString("nombres"));
                    e.setEspecialidad(rs.getString("especialidad"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setEstado(rs.getInt("estado"));
                    lista.add(e);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR EmpleadoDAO.listar: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Empleado buscarPorId(int id) {
        String sql = "SELECT id_empleado, nombres, especialidad, telefono, estado FROM tb_empleado WHERE id_empleado=?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado e = new Empleado();
                    e.setIdEmpleado(rs.getInt("id_empleado"));
                    e.setNombres(rs.getString("nombres"));
                    e.setEspecialidad(rs.getString("especialidad"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setEstado(rs.getInt("estado"));
                    return e;
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR EmpleadoDAO.buscarPorId: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM tb_empleado";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (Exception e) {
            System.out.println("ERROR EmpleadoDAO.contar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
