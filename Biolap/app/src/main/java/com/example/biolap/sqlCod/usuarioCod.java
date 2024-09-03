package com.example.biolap.sqlCod;

import com.example.biolap.conexion.Conexion;
import com.example.biolap.modelo.usuarioData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class usuarioCod {

    String usuarioN, contrasena;
    int usuarioId;

    public boolean verificar(String usuario, String contra){
        this.usuarioN=usuario;
        this.contrasena=contra;
        usuarioData ud = new usuarioData();
        String select = "SELECT * FROM usuarios WHERE usuarios=? AND contra=?";
        try{
            Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(select);
            ps.setString(1,usuarioN);
            ps.setString(2,contrasena);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.usuarioId=rs.getInt("id");
                ud.setUsuarioId(usuarioId);
                ud.setNombre(usuarioN);
                rs.close();
                ps.close();
                con.close();
                return true;
            } else {
                rs.close();
                ps.close();
                con.close();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
