package com.example.biolap.sqlCod;

import com.example.biolap.conexion.Conexion;
import com.example.biolap.modelo.usuarioData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class registarCod {
    String contrasena,nombre,correo;

    public boolean registro(String user, String email, String contra){
    this.nombre=user;
    this.contrasena=contra;
    this.correo=email;
    String insert = "INSERT INTO usuarios (nombre,correo,contra,fecha_creacion) VALUES (?,?,?,'2024-08-25')";
        try{
            Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1,nombre);
            ps.setString(2,correo);
            ps.setString(3,contrasena);
            int rowsAffected = ps.executeUpdate();
            ps.close();
            con.close();
            return rowsAffected > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
