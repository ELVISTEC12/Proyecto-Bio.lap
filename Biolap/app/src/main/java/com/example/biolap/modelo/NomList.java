package com.example.biolap.modelo;

import java.io.Serializable;

public class NomList implements Serializable {
    private int id_usuario;
    private String nombre;
    private String codigo;
    private String formulario;

    public NomList(String codigo, String nombre, String formulario) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.formulario = formulario;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFormulario() {
        return formulario;
    }
}