package com.example.biolap.modelo;

import java.io.Serializable;

public class usuarioData implements Serializable {
    private int usuarioId;
    private String nombre;

    public usuarioData(int usuarioId,String nombre){
        this.usuarioId=usuarioId;
        this.nombre=nombre;
    }

    public int getUsuarioId(){
        return usuarioId;
    }
    public String getNombre(){
        return nombre;
    }
}
