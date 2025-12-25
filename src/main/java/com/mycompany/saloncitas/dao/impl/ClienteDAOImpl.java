package com.mycompany.saloncitas.dao.impl;

import com.mycompany.saloncitas.config.DBConnection;
import com.mycompany.saloncitas.dao.ClienteDAO;
import com.mycompany.saloncitas.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public List<Cliente> listar(String q) {
        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT id_cliente, dni, nombres AS nombre, telefono, correo AS email, estado " +
                     "FROM tb_cliente " +
                     "WHERE (? IS NULL OR dni LIKE ? OR nombres LIKE ?) " +
                     "ORDER BY id_cliente DESC";

        System.out.println("ClienteDAO.listar q=" + q);

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
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setDni(rs.getString("dni"));
                    c.setNombre(rs.getString("nombre"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setEstado(rs.getInt("estado"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR ClienteDAO.listar: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT id_cliente, dni, nombres AS nombre, telefono, correo AS email, estado " +
                     "FROM tb_cliente WHERE id_cliente=?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setDni(rs.getString("dni"));
                    c.setNombre(rs.getString("nombre"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setEstado(rs.getInt("estado"));
                    return c;
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR ClienteDAO.buscarPorId: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean crear(Cliente c) {
        String sql = "INSERT INTO tb_cliente(dni, nombres, telefono, correo, estado) VALUES(?,?,?,?,?)";
        System.out.println("ClienteDAO.crear dni=" + c.getDni());

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail()); // correo en BD
            ps.setInt(5, c.getEstado());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ERROR ClienteDAO.crear: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Cliente c) {
        String sql = "UPDATE tb_cliente SET dni=?, nombres=?, telefono=?, correo=?, estado=? WHERE id_cliente=?";
        System.out.println("ClienteDAO.actualizar id=" + c.getIdCliente());

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre()); // nombres en BD
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());  // correo en BD
            ps.setInt(5, c.getEstado());
            ps.setInt(6, c.getIdCliente());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ERROR ClienteDAO.actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_cliente WHERE id_cliente=?";
        System.out.println("ClienteDAO.eliminar id=" + id);

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ERROR ClienteDAO.eliminar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM tb_cliente";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (Exception e) {
            System.out.println("ERROR ClienteDAO.contar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}