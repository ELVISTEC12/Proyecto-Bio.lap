package com.example.biolap.modelo;

import java.io.Serializable;

public class usuarioData implements Serializable {
    private int usuarioId;
    private String nombre;

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getUsuarioId(){
        return usuarioId;
    }
    public String getNombre(){
        return nombre;
    }

}
