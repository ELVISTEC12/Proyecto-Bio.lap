package com.example.biolap.modelo;

import java.io.Serializable;

public class registros implements Serializable {
    private static registros instancia;
    private String id;
    private String nombreC;
    private String edad;
    private String obra_social;
    private String telefono;
    private String medico;
    private String dni;

    public registros() { }

    public static registros getInstance() {
        if (instancia == null) {
            instancia = new registros();
        }
        return instancia;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setObra_social(String obra_social) {
        this.obra_social = obra_social;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setNombreC(String nombre) {
        this.nombreC = nombre;
    }

    public String getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getMedico() {
        return medico;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getObra_social() {
        return obra_social;
    }

    public String getEdad() {
        return edad;
    }

    public String getNombreC() {
        return nombreC;
    }
}