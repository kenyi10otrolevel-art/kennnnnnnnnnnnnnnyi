package com.mycompany.saloncitas.controller;

import com.mycompany.saloncitas.config.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TestConexionServlet", urlPatterns = {"/testConexion"})
public class TestConexionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        try (Connection cn = DBConnection.getConnection()) {
            DatabaseMetaData md = cn.getMetaData();
            response.getWriter().println("✅ CONEXIÓN OK");
            response.getWriter().println("DB: " + md.getDatabaseProductName() + " " + md.getDatabaseProductVersion());
            response.getWriter().println("URL: " + md.getURL());
            response.getWriter().println("USER: " + md.getUserName());
        } catch (Exception e) {
            response.getWriter().println("❌ ERROR DE CONEXIÓN");
            response.getWriter().println(e.getMessage());
        }
    }
}
