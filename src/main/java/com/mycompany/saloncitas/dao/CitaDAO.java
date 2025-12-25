package com.mycompany.saloncitas.dao;

import com.mycompany.saloncitas.model.Cita;
import java.util.List;

public interface CitaDAO {
    List<Cita> listar(String q);
    Cita buscarPorId(int id);
    boolean crear(Cita c);
    boolean actualizar(Cita c);
    boolean eliminar(int id);
    int contar();
}
