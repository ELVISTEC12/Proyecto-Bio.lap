package com.example.biolap.modelo;

public class Nomenclaturas {
    private String nombreN;
    private String codigoN;
    private String formN;
    private String idN;

    public Nomenclaturas(String idN, String nombreN, String codigoN, String formN) {
        this.idN = idN;
        this.nombreN = nombreN;
        this.codigoN = codigoN;
        this.formN = formN;
    }

    public String getIdN() {
        return idN;
    }

    public String getNombreN() {
        return nombreN;
    }

    public String getFormN() {
        return formN;
    }

    public String getCodigoN() {
        return codigoN;
    }

    @Override
    public String toString() {
        return String.valueOf(codigoN); // Esto es lo que se mostrará en el spinner
    }

}
