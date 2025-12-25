<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.saloncitas.model.Cliente"%>
<%
  String ctx = request.getContextPath();
  String msg = request.getParameter("msg");
  String q = request.getParameter("q");

  List<Cliente> lista = (List<Cliente>) request.getAttribute("lista");
  Cliente edit = (Cliente) request.getAttribute("editCliente");
  boolean isEdit = (edit != null);
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Clientes | SalonCitas</title>
</head>
<body>
 <jsp:include page="/_layout/header.jsp" />


  <div class="container">
    <div class="grid">

      <div class="card" style="grid-column: span 12;">
        <div class="head">
          <h2>Clientes</h2>
          <p>Registro y mantenimiento de clientes.</p>
        </div>
        <div class="body">

          <% if ("ok".equals(msg)) { %>
            <div class="msg ok">✅ Operación realizada correctamente.</div>
          <% } else if ("err".equals(msg)) { %>
            <div class="msg err">❌ Ocurrió un error, revisa el Output de NetBeans.</div>
          <% } %>

          <form method="get" action="<%=ctx%>/recepcion/clientes" class="split" style="margin-bottom:12px;">
            <div style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
              <input name="q" value="<%= (q!=null?q:"") %>" placeholder="Buscar por DNI o nombre..." style="min-width:260px;">
              <button class="btn btn-ghost" type="submit">Buscar</button>
              <a class="btn btn-ghost" href="<%=ctx%>/recepcion/clientes">Limpiar</a>
            </div>
          </form>

          <div class="card" style="margin-bottom:14px;">
            <div class="head">
              <h2><%= isEdit ? "Editar cliente" : "Nuevo cliente" %></h2>
              <p>Completa los datos y guarda.</p>
            </div>
            <div class="body">
              <form method="post" action="<%=ctx%>/recepcion/clientes" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "create" %>">
                <% if (isEdit) { %>
                  <input type="hidden" name="id_cliente" value="<%=edit.getIdCliente()%>">
                <% } %>

                <div class="field">
                  <label>DNI</label>
                  <input name="dni" required value="<%= isEdit ? edit.getDni() : "" %>">
                </div>

                <div class="field">
                  <label>Nombre</label>
                  <input name="nombre" required value="<%= isEdit ? edit.getNombre() : "" %>">
                </div>

                <div class="field">
                  <label>Teléfono</label>
                  <input name="telefono" value="<%= isEdit ? (edit.getTelefono()==null?"":edit.getTelefono()) : "" %>">
                </div>

                <div class="field">
                  <label>Email</label>
                  <input name="email" value="<%= isEdit ? (edit.getEmail()==null?"":edit.getEmail()) : "" %>">
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
                  <a class="btn btn-ghost" href="<%=ctx%>/recepcion/clientes">Cancelar</a>
                </div>
              </form>
            </div>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>ID</th><th>DNI</th><th>Nombre</th><th>Teléfono</th><th>Email</th><th>Estado</th><th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <% if (lista != null) for (Cliente c : lista) { %>
                <tr>
                  <td><%=c.getIdCliente()%></td>
                  <td><%=c.getDni()%></td>
                  <td><%=c.getNombre()%></td>
                  <td><%= (c.getTelefono()==null? "-" : c.getTelefono()) %></td>
                  <td><%= (c.getEmail()==null? "-" : c.getEmail()) %></td>
                  <td>
                    <% if (c.getEstado()==1) { %>
                      <span class="badge" style="border-color: rgba(34,197,94,.35);">ACTIVO</span>
                    <% } else { %>
                      <span class="badge" style="border-color: rgba(239,68,68,.35);">INACTIVO</span>
                    <% } %>
                  </td>
                  <td>
                    <a class="btn btn-ghost" href="<%=ctx%>/recepcion/clientes?action=edit&id=<%=c.getIdCliente()%>">Editar</a>

                    <form method="post" action="<%=ctx%>/recepcion/clientes" style="display:inline;">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="id" value="<%=c.getIdCliente()%>">
                      <button class="btn btn-ghost" type="submit" onclick="return confirm('¿Eliminar cliente?');">Eliminar</button>
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
