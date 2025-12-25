package com.mycompany.saloncitas.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/bd_salon_citas?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "mysql"; 

    static {
        try {
            // MySQL 8
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el Driver MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
