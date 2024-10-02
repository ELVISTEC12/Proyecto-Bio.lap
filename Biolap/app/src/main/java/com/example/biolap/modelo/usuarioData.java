package com.example.biolap.modelo;

import java.io.Serializable;

public class usuarioData implements Serializable {
    private static usuarioData instancia;
    private String usuario_nombre;
    private String id_usuario;

    public usuarioData() { }

    public static usuarioData getInstance() {
        if (instancia == null) {
            instancia = new usuarioData();
        }
        return instancia;
    }

    public void setId_usuario(String id) {
        this.id_usuario = id;
    }

    public void setUsuario_nombre(String nombre) {
        this.usuario_nombre = nombre;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public String getUsuario_nombre() {
        return usuario_nombre;
    }

}
