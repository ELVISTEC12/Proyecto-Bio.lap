package com.example.biolap.modelo;

import java.io.Serializable;

public class PacienteLista implements Serializable {
    private String idP;
    private String nombreP;
    private String obra_social;
    private String edad;
    private String dni;
    private String telefono;
    private String medico;
    private String rutina;
    private String fecha;

    public PacienteLista(String idP, String nombreP, String obra_social, String edad, String dni, String telefono, String medico, String rutina, String fecha) {
        this.idP = idP;
        this.nombreP = nombreP;
        this.obra_social = obra_social;
        this.edad = edad;
        this.dni = dni;
        this.telefono = telefono;
        this.medico = medico;
        this.rutina = rutina;
        this.fecha = fecha;
    }

    public String getIdP() {
        return idP;
    }

    public String getNombreP() {
        return nombreP;
    }

    public String getObra_social() {
        return obra_social;
    }

    public String getEdad() {
        return edad;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getMedico() {
        return medico;
    }

    public String getRutina() {
        return rutina;
    }

    public String getFecha() {
        return fecha;
    }
}
