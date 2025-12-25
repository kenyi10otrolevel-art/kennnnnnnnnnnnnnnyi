package com.mycompany.saloncitas.dao.impl;

import com.mycompany.saloncitas.config.DBConnection;
import com.mycompany.saloncitas.dao.UsuarioDAO;
import com.mycompany.saloncitas.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private static final String SQL_LOGIN =
        "SELECT id_usuario, usuario, password, rol, nombre, estado " +
        "FROM tb_usuario " +
        "WHERE usuario=? AND password=? AND estado=1 LIMIT 1";

    @Override
    public Usuario login(String usuario, String password) {
        System.out.println("=== UsuarioDAO.login === usuario=[" + usuario + "]");

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_LOGIN)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = map(rs);
                    System.out.println("LOGIN OK => " + u.getUsuario() + " rol=" + u.getRol());
                    return u;
                }
            }

            System.out.println("LOGIN FAIL => credenciales inválidas o estado=0");
            return null;

        } catch (Exception e) {
            System.out.println("ERROR login DAO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Usuario> listar(String q) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, usuario, password, rol, nombre, estado " +
                     "FROM tb_usuario " +
                     "WHERE (? IS NULL OR usuario LIKE ? OR nombre LIKE ? OR rol LIKE ?) " +
                     "ORDER BY id_usuario DESC";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (q == null || q.trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);
            } else {
                String like = "%" + q.trim() + "%";
                ps.setString(1, q);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR UsuarioDAO.listar: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT id_usuario, usuario, password, rol, nombre, estado FROM tb_usuario WHERE id_usuario=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            System.out.println("ERROR UsuarioDAO.buscarPorId: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean crear(Usuario u) {
        String sql = "INSERT INTO tb_usuario(usuario,password,rol,nombre,estado) VALUES(?,?,?,?,?)";
        System.out.println("UsuarioDAO.crear user=" + u.getUsuario());
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNombre());
            ps.setInt(5, u.getEstado());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("ERROR UsuarioDAO.crear: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE tb_usuario SET usuario=?, password=?, rol=?, nombre=?, estado=? WHERE id_usuario=?";
        System.out.println("UsuarioDAO.actualizar id=" + u.getIdUsuario());
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNombre());
            ps.setInt(5, u.getEstado());
            ps.setInt(6, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("ERROR UsuarioDAO.actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cambiarEstado(int id, int estado) {
        String sql = "UPDATE tb_usuario SET estado=? WHERE id_usuario=?";
        System.out.println("UsuarioDAO.cambiarEstado id=" + id + " estado=" + estado);
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("ERROR UsuarioDAO.cambiarEstado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM tb_usuario";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            System.out.println("ERROR UsuarioDAO.contar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private Usuario map(ResultSet rs) throws Exception {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setUsuario(rs.getString("usuario"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setNombre(rs.getString("nombre"));
        u.setEstado(rs.getInt("estado"));
        return u;
    }
}
