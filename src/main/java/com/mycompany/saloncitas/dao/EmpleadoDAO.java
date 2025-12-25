package com.mycompany.saloncitas.dao;

import com.mycompany.saloncitas.model.Empleado;
import java.util.List;

public interface EmpleadoDAO {
    List<Empleado> listar(String q);
    Empleado buscarPorId(int id);
    int contar();
}
