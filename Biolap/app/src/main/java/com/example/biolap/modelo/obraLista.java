package com.example.biolap.modelo;

public class obraLista {
    private String id;
    private String nombre;

    public obraLista(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return id + " -  " + nombre;
    }
}