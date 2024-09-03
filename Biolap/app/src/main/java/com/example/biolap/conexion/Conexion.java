package com.example.biolap.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "mysql://u9zuhn25edprht9e:X6ULKbK1q2AWbo7Ciba3@bxew7xvoyzkdkvbwno27-mysql.services.clever-cloud.com:3306/bxew7xvoyzkdkvbwno27";

    private static final String USER = "u9zuhn25edprht9e";
    private static final String PASSWORD = "X6ULKbK1q2AWbo7Ciba3";

    public static Connection getConnection() {
        
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            if (conn != null) {
                System.out.println("Conexión establecida con éxito.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador JDBC.");
            e.printStackTrace();
        }

        catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public String getConnection1() {
        Connection conn = null;
        String v;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                return v="Conexión establecida con éxito.";
            }
        } catch (ClassNotFoundException e) {
            return v="Error con jdbc";
        } catch (SQLException e) {
            return v="error sql "+e;
        }
        return v="a";
    }
}
