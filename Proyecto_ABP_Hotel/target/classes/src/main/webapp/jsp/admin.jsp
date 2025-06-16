<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="utils.Estado"%>
<%@page import="utils.Rol"%>
<%@page import="service.AdminService"%>
<%@page import="utils.Rol"%>
<%@page import="utils.Estado"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Reserva_Actividad" %>
<%@ page import="service.Reserva_ActividadService" %>
<%@page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <%
        ArrayList<Reserva_Actividad> Reserva_ActividadArrayList = (ArrayList<Reserva_Actividad>) request.getAttribute("Reserva_ActividadArrayList");
    %>
    <style>
        body {
                   background-color: #a3c4f3; /* Azul pastel suave */
                   font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                   olor: #333;
                }

        .custom-margin {
            margin-left: 1rem;
            margin-right: 1rem;
            margin: 0 auto;
        }
    </style>
</head>

<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarTogglerDemo03" aria-controls="navbarTogglerDemo03" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <a class="navbar-brand" href="#">Admin</a>
            <ul class="navbar-nav me-auto mt-2 mt-lg-0">
                <li class="nav-item">
                    <form action="activitats" method="get">
                        <button class="btn nav-link" type="submit">Actividades</button>
                    </form>
                </li>

                <li class="nav-item">
                    <form action="habitacions" method="get">
                        <button class="btn nav-link" type="submit">Habitaciones</button>
                    </form>
                </li>

                <li class="nav-item">
                    <form action="reservaHabitacion" method="get">
                        <button class="btn nav-link" type="submit">Reservas</button>
                    </form>
                </li>

                <li class="nav-item">
                    <form action="login" method="get">
                        <input type="hidden" name="action" value="por_id">
                        <button class="btn nav-link" type="submit">Perfil</button>
                    </form>
                </li>

                <% %>
                    <li class="nav-item">
                        <form action="admin" method="get">
                            <button class="btn nav-link" type="submit">Admin</button>
                        </form>
                    </li>
                <% %>

            </ul>
    </nav>

    <div class="container mt-4">
        <div class="card p-3">
            <h3>Lista de Usuarios</h3>
            <ul class="list-group">

            <%
                List<User> userArrayList = (List<User>) request.getAttribute("userArrayList");

                if (userArrayList != null && !userArrayList.isEmpty()) {
                    for (User user : userArrayList) {
            %>
            <li class="list-group-item d-flex justify-content-between align-items-center">

                <div>
                    <%= user.getUsername() %> - <%= user.getRol() %>
                </div>

                <div class="d-flex gap-2">
                    <form action="admin" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= user.getId() %>">
                        <input type="hidden" name="estadoString2" value="eliminado">
                        <button type="submit" name="action" value="delete"
                                class="btn btn-danger btn-sm"
                                style="<%= user.getEstado().equals(Estado.eliminado) ? "visibility:hidden;" : "" %>">
                            Eliminar
                        </button>
                    </form>

                    <form action="admin" method="post" style="display:inline;">
                        <input type="hidden" name="id_update" value="<%= user.getId() %>">
                        <input type="hidden" name="rolString2" value="">
                        <button type="submit" name="action" value="admin"
                                class="btn btn-primary btn-sm"
                                style="<%= (user.getRol().equals(Rol.admin) || user.getEstado().equals(Estado.eliminado)) ? "visibility:hidden;" : "" %>">
                            Convertir en Admin
                        </button>
                    </form>
                </div>
            </li>
            <%
                    }
                } else {
            %>
            <li class="list-group-item">No hay usuarios disponibles.</li>
            <%
                }
            %>
        </ul>
        </div>

        <div class="card p-3 mt-4">
            <h3>Crear Nuevo Usuario</h3>
            <form action="admin" method="post">
                <div class="mb-3">
                    <label for="usu" class="form-label">Nombre de Usuario</label>
                    <input type="text" class="form-control" id="usu" name="usu" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="pass" class="form-label">Contraseña</label>
                    <input type="password" class="form-control" id="pass" name="pass" required>
                </div>
                <div>
                   <input type="hidden" name="rolString" value="cliente">
                </div>

                <button type="submit" name="action" value="insert" class="btn btn-success">Crear Usuario</button>
            </form>
        </div>

        <div class="card p-3 mt-4">
            <h3>Actividades Reservadas</h3>
            <ul class="list-group">

                    <%
                        ArrayList<Reserva_Actividad> ra = (ArrayList<Reserva_Actividad>) request.getAttribute("Reserva_ActividadArrayList");
                    %>
                    <% for (Reserva_Actividad p : ra) { %>

                    <li class="list-group-item">
                        El usuario con id: <%= p.getId_user() %> ha reservado la actividad con id: <%= p.getId_actividad() %>
                    </li>

                    <% } %>
            </ul>
        </div>
    </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
