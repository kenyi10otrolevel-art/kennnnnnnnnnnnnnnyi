<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.saloncitas.model.Usuario"%>

<%
    String __ctx = request.getContextPath();
    Usuario auth = (Usuario) session.getAttribute("authUser");

    String _rol_ = "";
    if (auth != null && auth.getRol() != null) {
        _rol_ = auth.getRol().toUpperCase();
    }

    String _nombre_ = (auth != null && auth.getNombre() != null) ? auth.getNombre() : "Invitado";
%>

<link rel="stylesheet" href="<%=__ctx%>/resources/css/app.css">

<header class="topbar">
    <div class="topbar__wrap">

        <div class="topbar__left">
            <a class="brand-link" href="<%=__ctx%>/inicio.jsp">
                <span class="brand-badge"></span>
                <span class="brand-title">
                    <b>SalonCitas</b>
                    <small>Gestión de citas y clientes</small>
                </span>
            </a>

            <span class="badge"><%= _rol_ %></span>
        </div>

        <div class="topbar__right">
            <span class="user">👤 <b><%= _nombre_ %></b></span>
            <a class="btn btn-ghost" href="<%=__ctx%>/logout">Salir</a>
        </div>

    </div>
</header>

<nav class="nav">
    <div class="nav__inner">
        <a class="nav__link" href="<%=__ctx%>/inicio.jsp">Inicio</a>

        <% if ("ADMIN".equals(_rol_)) { %>
            <a class="nav__link" href="<%=__ctx%>/admin/dashboard.jsp">Dashboard</a>
            <a class="nav__link" href="<%=__ctx%>/admin/usuarios.jsp">Usuarios</a>
        <% } %>

        <% if ("RECEPCION".equals(_rol_) || "ADMIN".equals(_rol_)) { %>
            <a class="nav__link" href="<%=__ctx%>/recepcion/citas.jsp">Citas</a>
            <a class="nav__link" href="<%=__ctx%>/recepcion/clientes.jsp">Clientes</a>
        <% } %>
    </div>
</nav>
