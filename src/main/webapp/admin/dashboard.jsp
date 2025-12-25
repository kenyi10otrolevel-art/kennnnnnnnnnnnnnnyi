<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  int usuarios = (request.getAttribute("usuariosCount") != null) ? (int)request.getAttribute("usuariosCount") : 0;
  int clientes = (request.getAttribute("clientesCount") != null) ? (int)request.getAttribute("clientesCount") : 0;
  int citas = (request.getAttribute("citasCount") != null) ? (int)request.getAttribute("citasCount") : 0;
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Dashboard | SalonCitas</title>
</head>
<body>
  <jsp:include page="/_layout/header.jsp" />


  <div class="container">
    <div class="grid">

      <div class="card" style="grid-column: span 12;">
        <div class="head">
          <h2>Dashboard</h2>
          <p>Métricas del sistema (usuarios, clientes y citas).</p>
        </div>
        <div class="body">
          <div class="grid">
            <div class="card" style="grid-column: span 4;">
              <div class="head"><h2>Usuarios</h2><p>Total registrados</p></div>
              <div class="body"><div class="kpi"><div><div class="value"><%=usuarios%></div><div class="label">Usuarios</div></div><span class="badge">Admin</span></div></div>
            </div>

            <div class="card" style="grid-column: span 4;">
              <div class="head"><h2>Clientes</h2><p>Total registrados</p></div>
              <div class="body"><div class="kpi"><div><div class="value"><%=clientes%></div><div class="label">Clientes</div></div><span class="badge">Recepción</span></div></div>
            </div>

            <div class="card" style="grid-column: span 4;">
              <div class="head"><h2>Citas</h2><p>Total programadas</p></div>
              <div class="body"><div class="kpi"><div><div class="value"><%=citas%></div><div class="label">Citas</div></div><span class="badge">Agenda</span></div></div>
            </div>
          </div>

          <div class="actions" style="justify-content:flex-start; margin-top:14px;">
            <a class="btn" href="<%=request.getContextPath()%>/admin/usuarios">Gestionar Usuarios</a>
            <a class="btn btn-ghost" href="<%=request.getContextPath()%>/recepcion/clientes">Clientes</a>
            <a class="btn btn-ghost" href="<%=request.getContextPath()%>/recepcion/citas">Citas</a>
          </div>
        </div>
      </div>

    </div>

    <%@include file="/_layout/footer.jsp" %>
  </div>
</body>
</html>
