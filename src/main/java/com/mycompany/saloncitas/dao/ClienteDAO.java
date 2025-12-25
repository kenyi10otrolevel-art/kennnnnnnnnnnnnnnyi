package com.mycompany.saloncitas.dao;

import com.mycompany.saloncitas.model.Cliente;
import java.util.List;

public interface ClienteDAO {
    List<Cliente> listar(String q);
    Cliente buscarPorId(int id);
    boolean crear(Cliente c);
    boolean actualizar(Cliente c);
    boolean eliminar(int id);
    int contar();
}
