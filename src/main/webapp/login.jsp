<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  String err = request.getParameter("err");
  String msg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login | SalonCitas</title>
  <link rel="stylesheet" href="<%=ctx%>/resources/css/app.css">
</head>
<body>
  <div class="container" style="margin-top:60px;">
    <div class="grid">
      <div class="card" style="grid-column: span 12; max-width:520px; margin:0 auto;">
        <div class="head">
          <h2>Iniciar sesión</h2>
          <p>Accede con tu usuario y contraseña.</p>
        </div>
        <div class="body">

          <% if ("1".equals(err)) { %>
            <div class="msg err">❌ Usuario o contraseña incorrectos.</div>
          <% } %>

          <% if ("logout".equals(msg)) { %>
            <div class="msg ok">✅ Sesión cerrada correctamente.</div>
          <% } %>

          <form action="<%=ctx%>/login" method="post" class="form">
            <div class="field full">
              <label>Usuario</label>
              <input type="text" name="usuario" placeholder="Ej: admin / recep" required>
            </div>

            <div class="field full">
              <label>Contraseña</label>
              <input type="password" name="password" placeholder="Ej: admin123 / recep123" required>
            </div>

            <div class="field full actions">
              <button class="btn" type="submit">Entrar</button>
            </div>
          </form>

          <div class="small" style="margin-top:10px;">
            Tip: usa un usuario existente en <b>tb_usuario</b>.
          </div>

          <div style="margin-top:12px; display:flex; gap:10px; flex-wrap:wrap;">
            <span class="badge">Rol: ADMIN</span>
            <span class="badge">Rol: RECEPCION</span>
          </div>

        </div>
      </div>
    </div>
  </div>

  <div class="footer">SalonCitas · Login</div>
</body>
</html>
