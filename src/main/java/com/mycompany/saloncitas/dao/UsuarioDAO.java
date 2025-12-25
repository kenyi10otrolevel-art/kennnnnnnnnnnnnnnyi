package com.mycompany.saloncitas.dao;

import com.mycompany.saloncitas.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario login(String usuario, String password);

    List<Usuario> listar(String q);
    Usuario buscarPorId(int id);
    boolean crear(Usuario u);
    boolean actualizar(Usuario u);
    boolean cambiarEstado(int id, int estado);
    int contar();
}
