<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.saloncitas.model.Cita"%>
<%@page import="com.mycompany.saloncitas.model.Cliente"%>
<%@page import="com.mycompany.saloncitas.model.Empleado"%>
<%
  String ctx = request.getContextPath();
  String msg = request.getParameter("msg");
  String q = request.getParameter("q");

  List<Cita> lista = (List<Cita>) request.getAttribute("lista");
  List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
  List<Empleado> empleados = (List<Empleado>) request.getAttribute("empleados");

  Cita edit = (Cita) request.getAttribute("editCita");
  boolean isEdit = (edit != null);

  // Total para el input (evita nulls y muestra 0.00)
  String totalValue = "0.00";
  if (isEdit) {
    totalValue = String.format(java.util.Locale.US, "%.2f", edit.getTotal());
  }
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Citas | SalonCitas</title>
</head>
<body>
  <jsp:include page="/_layout/header.jsp" />
  <div class="container">
    <div class="grid">
      <div class="card" style="grid-column: span 12;">
        <div class="head">
          <h2>Citas</h2>
          <p>Programación y control de citas.</p>
        </div>

        <div class="body">
          <% if ("ok".equals(msg)) { %>
            <div class="msg ok">✅ Operación realizada correctamente.</div>
          <% } else if ("err".equals(msg)) { %>
            <div class="msg err">❌ Ocurrió un error, revisa el Output de NetBeans.</div>
          <% } %>

          <form method="get" action="<%=ctx%>/recepcion/citas" class="split" style="margin-bottom:12px;">
            <div style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
              <input name="q" value="<%= (q!=null?q:"") %>" placeholder="Buscar por cliente, empleado o estado..." style="min-width:260px;">
              <button class="btn btn-ghost" type="submit">Buscar</button>
              <a class="btn btn-ghost" href="<%=ctx%>/recepcion/citas">Limpiar</a>
            </div>
          </form>

          <div class="card" style="margin-bottom:14px;">
            <div class="head">
              <h2><%= isEdit ? "Editar cita" : "Nueva cita" %></h2>
              <p>Selecciona cliente, empleado, fecha/hora.</p>
            </div>

            <div class="body">
              <form method="post" action="<%=ctx%>/recepcion/citas" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "create" %>">
                <% if (isEdit) { %>
                  <input type="hidden" name="id_cita" value="<%=edit.getIdCita()%>">
                <% } %>

                <div class="field">
                  <label>Cliente</label>
                  <select name="id_cliente" required>
                    <option value="">-- Seleccionar --</option>
                    <% if (clientes != null) for (Cliente cl : clientes) { %>
                      <option value="<%=cl.getIdCliente()%>"
                        <%= (isEdit && edit.getIdCliente()==cl.getIdCliente()) ? "selected" : "" %>>
                        <%=cl.getNombre()%> (DNI: <%=cl.getDni()%>)
                      </option>
                    <% } %>
                  </select>
                </div>

                <div class="field">
                  <label>Empleado</label>
                  <select name="id_empleado" required>
                    <option value="">-- Seleccionar --</option>
                    <% if (empleados != null) for (Empleado e : empleados) { %>
                      <option value="<%=e.getIdEmpleado()%>"
                        <%= (isEdit && edit.getIdEmpleado()==e.getIdEmpleado()) ? "selected" : "" %>>
                        <%=e.getNombres()%> (<%=e.getEspecialidad()%>)
                      </option>
                    <% } %>
                  </select>
                </div>

                <div class="field">
                  <label>Fecha</label>
                  <input type="date" name="fecha" required value="<%= isEdit ? edit.getFecha() : "" %>">
                </div>

                <div class="field">
                  <label>Hora</label>
                  <input type="time" name="hora" required
                         value="<%= (isEdit && edit.getHora()!=null && edit.getHora().length()>=5) ? edit.getHora().substring(0,5) : "" %>">
                </div>

                <div class="field">
                  <label>Estado</label>
                  <select name="estado" required>
                    <option value="RESERVADA" <%= (!isEdit || "RESERVADA".equalsIgnoreCase(edit.getEstado())) ? "selected" : "" %>>RESERVADA</option>
                    <option value="ATENDIDA"  <%= (isEdit && "ATENDIDA".equalsIgnoreCase(edit.getEstado())) ? "selected" : "" %>>ATENDIDA</option>
                    <option value="CANCELADA" <%= (isEdit && "CANCELADA".equalsIgnoreCase(edit.getEstado())) ? "selected" : "" %>>CANCELADA</option>
                  </select>
                </div>

                <div class="field">
                  <label>Total</label>
                  <!-- IMPORTANTE: type=number + step para decimal -->
                  <input type="number" step="0.01" min="0" name="total" required value="<%= totalValue %>">
                </div>

                <div class="field full actions">
                  <button class="btn" type="submit"><%= isEdit ? "Actualizar" : "Crear" %></button>
                  <a class="btn btn-ghost" href="<%=ctx%>/recepcion/citas">Cancelar</a>
                </div>
              </form>
            </div>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>ID</th><th>Cliente</th><th>Empleado</th><th>Especialidad</th>
                <th>Fecha</th><th>Hora</th><th>Estado</th><th>Total</th><th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <% if (lista != null) for (Cita c : lista) { %>
                <tr>
                  <td><%=c.getIdCita()%></td>
                  <td><%=c.getClienteNombre()%></td>
                  <td><%=c.getEmpleadoNombre()%></td>
                  <td><%=c.getEspecialidad()%></td>
                  <td><%=c.getFecha()%></td>
                  <td><%= (c.getHora()!=null && c.getHora().length()>=5 ? c.getHora().substring(0,5) : "-") %></td>
                  <td><span class="badge"><%=c.getEstado()%></span></td>
                  <td><%= String.format(java.util.Locale.US, "%.2f", c.getTotal()) %></td>
                  <td>
                    <a class="btn btn-ghost" href="<%=ctx%>/recepcion/citas?action=edit&id=<%=c.getIdCita()%>">Editar</a>
                    <form method="post" action="<%=ctx%>/recepcion/citas" style="display:inline;">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="id" value="<%=c.getIdCita()%>">
                      <button class="btn btn-ghost" type="submit" onclick="return confirm('¿Eliminar cita?');">Eliminar</button>
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
