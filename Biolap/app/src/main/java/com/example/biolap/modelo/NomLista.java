package com.example.biolap.modelo;

import java.io.Serializable;

public class NomLista implements Serializable {
    private String id;
    private String nombre;
    private String codigo;
    private String formulario;

    public NomLista(String id, String nombre, String codigo, String form) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.formulario = form;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getFormulario() {
        return formulario;
    }
}
