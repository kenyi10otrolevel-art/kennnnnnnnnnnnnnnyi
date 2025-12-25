package com.mycompany.saloncitas.model;

import java.io.Serializable;

public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEmpleado;
    private String nombres;
    private String especialidad;
    private String telefono;
    private int estado;

    public Empleado() {}

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}
