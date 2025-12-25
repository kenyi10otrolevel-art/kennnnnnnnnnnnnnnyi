<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.saloncitas.model.Usuario"%>
<%
  Usuario u = (Usuario) session.getAttribute("authUser");
  String rol = (u != null && u.getRol()!=null) ? u.getRol() : "";
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Inicio | SalonCitas</title>
</head>
<body>
<jsp:include page="/_layout/header.jsp" />

  <div class="container">
    <div class="grid">

      <div class="card" style="grid-column: span 12;">
        <div class="head">
          <h2>Panel principal</h2>
          <p>Bienvenido/a, aquí tienes accesos rápidos según tu rol.</p>
        </div>
        <div class="body">
          <div class="split">
            <div>
              <div class="kpi">
                <div>
                  <div class="value"><%= (u!=null && u.getNombre()!=null) ? u.getNombre() : "Invitado" %></div>
                  <div class="label">Usuario autenticado</div>
                </div>
                <span class="badge">Rol: <%= (rol.isEmpty()? "-" : rol) %></span>
              </div>
              <div class="small" style="margin-top:8px;">
                Recomendación: crea clientes y programa citas desde Recepción.
              </div>
            </div>
          </div>
        </div>
      </div>

      <% if ("ADMIN".equalsIgnoreCase(rol)) { %>
      <div class="card" style="grid-column: span 6;">
        <div class="head">
          <h2>Administración</h2>
          <p>Gestiona usuarios y visualiza métricas.</p>
        </div>
        <div class="body">
          <div class="actions" style="justify-content:flex-start;">
            <a class="btn" href="<%=request.getContextPath()%>/admin/dashboard">Ver Dashboard</a>
            <a class="btn btn-ghost" href="<%=request.getContextPath()%>/admin/usuarios">Usuarios</a>
          </div>
        </div>
      </div>
      <% } %>

      <% if ("RECEPCION".equalsIgnoreCase(rol) || "ADMIN".equalsIgnoreCase(rol)) { %>
      <div class="card" style="grid-column: span 6;">
        <div class="head">
          <h2>Recepción</h2>
          <p>Registra clientes y programa citas.</p>
        </div>
        <div class="body">
          <div class="actions" style="justify-content:flex-start;">
            <a class="btn" href="<%=request.getContextPath()%>/recepcion/clientes">Clientes</a>
            <a class="btn btn-ghost" href="<%=request.getContextPath()%>/recepcion/citas">Citas</a>
          </div>
        </div>
      </div>
      <% } %>

    </div>

    <%@include file="/_layout/footer.jsp" %>
  </div>
</body>
</html>
