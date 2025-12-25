<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.saloncitas.model.Usuario"%>
<%
  String ctx = request.getContextPath();
  String msg = request.getParameter("msg");
  String q = request.getParameter("q");

  List<Usuario> lista = (List<Usuario>) request.getAttribute("lista");
  Usuario edit = (Usuario) request.getAttribute("editUser");

  boolean isEdit = (edit != null);
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Usuarios | SalonCitas</title>
</head>
<body>
  <jsp:include page="/_layout/header.jsp" />


  <div class="container">
    <div class="grid">

      <div class="card" style="grid-column: span 12;">
        <div class="head">
          <h2>Usuarios</h2>
          <p>Crear, editar y activar/desactivar usuarios del sistema.</p>
        </div>
        <div class="body">

          <% if ("ok".equals(msg)) { %>
            <div class="msg ok">✅ Operación realizada correctamente.</div>
          <% } else if ("err".equals(msg)) { %>
            <div class="msg err">❌ Ocurrió un error, revisa el Output de NetBeans.</div>
          <% } %>

          <form method="get" action="<%=ctx%>/admin/usuarios" class="split" style="margin-bottom:12px;">
            <div style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
              <input name="q" value="<%= (q!=null?q:"") %>" placeholder="Buscar por usuario, nombre o rol..." style="min-width:260px;">
              <button class="btn btn-ghost" type="submit">Buscar</button>
              <a class="btn btn-ghost" href="<%=ctx%>/admin/usuarios">Limpiar</a>
            </div>
          </form>

          <div class="card" style="margin-bottom:14px;">
            <div class="head">
              <h2><%= isEdit ? "Editar usuario" : "Nuevo usuario" %></h2>
              <p>Completa los datos y guarda.</p>
            </div>
            <div class="body">
              <form method="post" action="<%=ctx%>/admin/usuarios" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "create" %>">
                <% if (isEdit) { %>
                  <input type="hidden" name="id_usuario" value="<%=edit.getIdUsuario()%>">
                <% } %>

                <div class="field">
                  <label>Usuario</label>
                  <input name="usuario" required value="<%= isEdit ? edit.getUsuario() : "" %>">
                </div>

                <div class="field">
                  <label>Password</label>
                  <input name="password" required value="<%= isEdit ? edit.getPassword() : "" %>">
                </div>

                <div class="field">
                  <label>Nombre</label>
                  <input name="nombre" required value="<%= isEdit ? edit.getNombre() : "" %>">
                </div>

                <div class="field">
                  <label>Rol</label>
                  <select name="rol" required>
                    <option value="ADMIN" <%= (isEdit && "ADMIN".equalsIgnoreCase(edit.getRol())) ? "selected" : "" %>>ADMIN</option>
                    <option value="RECEPCION" <%= (isEdit && "RECEPCION".equalsIgnoreCase(edit.getRol())) ? "selected" : "" %>>RECEPCION</option>
                  </select>
                </div>

                <div class="field">
                  <label>Estado</label>
                  <select name="estado" required>
                    <option value="1" <%= (!isEdit || edit.getEstado()==1) ? "selected" : "" %>>ACTIVO</option>
                    <option value="0" <%= (isEdit && edit.getEstado()==0) ? "selected" : "" %>>INACTIVO</option>
                  </select>
                </div>

                <div class="field full actions">
                  <button class="btn" type="submit"><%= isEdit ? "Actualizar" : "Crear" %></button>
                  <a class="btn btn-ghost" href="<%=ctx%>/admin/usuarios">Cancelar</a>
                </div>
              </form>
            </div>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>ID</th><th>Usuario</th><th>Nombre</th><th>Rol</th><th>Estado</th><th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <% if (lista != null) for (Usuario u : lista) { %>
                <tr>
                  <td><%=u.getIdUsuario()%></td>
                  <td><%=u.getUsuario()%></td>
                  <td><%=u.getNombre()%></td>
                  <td><span class="badge"><%=u.getRol()%></span></td>
                  <td>
                    <% if (u.getEstado()==1) { %>
                      <span class="badge" style="border-color: rgba(34,197,94,.35);">ACTIVO</span>
                    <% } else { %>
                      <span class="badge" style="border-color: rgba(239,68,68,.35);">INACTIVO</span>
                    <% } %>
                  </td>
                  <td>
                    <a class="btn btn-ghost" href="<%=ctx%>/admin/usuarios?action=edit&id=<%=u.getIdUsuario()%>">Editar</a>

                    <form method="post" action="<%=ctx%>/admin/usuarios" style="display:inline;">
                      <input type="hidden" name="action" value="toggle">
                      <input type="hidden" name="id" value="<%=u.getIdUsuario()%>">
                      <input type="hidden" name="estado" value="<%= (u.getEstado()==1 ? 0 : 1) %>">
                      <button class="btn btn-ghost" type="submit">
                        <%= (u.getEstado()==1 ? "Desactivar" : "Activar") %>
                      </button>
                    </form>
                  </td>
                </tr>
              <% } %>
            </tbody>
          </table>

        </div>
      </div>

    </div>

    <%@include file="/_layout/footer.jsp" %>
  </div>
</body>
</html>
