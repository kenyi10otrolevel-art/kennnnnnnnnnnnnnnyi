package com.mycompany.saloncitas.filter;

import com.mycompany.saloncitas.model.Usuario;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ctx = request.getContextPath();
        String uri = request.getRequestURI();

        // Rutas públicas
        boolean isPublic =
                uri.equals(ctx + "/login") ||
                uri.equals(ctx + "/login.jsp") ||
                uri.equals(ctx + "/index.html") ||
                uri.startsWith(ctx + "/resources/") ||
                uri.startsWith(ctx + "/META-INF/") ||
                uri.startsWith(ctx + "/resources/")||
                uri.startsWith(ctx + "/WEB-INF/");

        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession s = request.getSession(false);
        Usuario u = (s != null) ? (Usuario) s.getAttribute("authUser") : null;

        if (u == null) {
            System.out.println("AuthFilter BLOQUEA => sin sesión, uri=" + uri);
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        // Restricción por rol (simple)
        String rol = (u.getRol() == null) ? "" : u.getRol().toUpperCase();

        if (uri.startsWith(ctx + "/admin/") && !"ADMIN".equals(rol)) {
            System.out.println("AuthFilter BLOQUEA => rol=" + rol + " intenta ADMIN, uri=" + uri);
            response.sendRedirect(ctx + "/inicio.jsp");
            return;
        }

        if (uri.startsWith(ctx + "/recepcion/") && !("RECEPCION".equals(rol) || "ADMIN".equals(rol))) {
            System.out.println("AuthFilter BLOQUEA => rol=" + rol + " intenta RECEPCION, uri=" + uri);
            response.sendRedirect(ctx + "/inicio.jsp");
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
