<%@page import="model.User" %>
<%@page import="model.Reserva_Habitacion" %>
<%@page import="utils.Rol"%>
<%@page import="service.Reserva_HabitacionService" %>
<%@ page import="model.Reserva_Actividad" %>
<%@ page import="service.Reserva_ActividadService" %>
<%@page import="java.util.ArrayList" %>
<%@page import="utils.Estado_H" %>
<%@page import="utils.Estado_Reserva" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
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
        <a class="navbar-brand" href="#">RESERVAS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarTogglerDemo03" aria-controls="navbarTogglerDemo03" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarTogglerDemo03">
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
                <%
                    User usuarioActual2 = (User) session.getAttribute("login");
                    if(usuarioActual2.getRol().equals(Rol.admin)){
                %>
                <li class="nav-item">
                    <form action="admin" method="get">
                        <button class="btn nav-link" type="submit">Admin</button>
                    </form>
                </li>
                <%
                }
                %>
            </ul>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <form action="login" method="get">
                        <button class="btn nav-link" type="submit">Logout</button>
                    </form>
                </li>
            </ul>
        </div>
    </nav>

    <div class="container mt-4 custom-margin">
        <div class="card p-3">
        <h3>Habitaciones Reservadas</h3>
            <ul class="list-group">
                <%
                    ArrayList<Reserva_Habitacion> reservaHabitacion = (ArrayList<Reserva_Habitacion>) request.getAttribute("reservaHabitacionesArrayList");

                    if (reservaHabitacion != null && !reservaHabitacion.isEmpty()) {
                        for (Reserva_Habitacion rh : reservaHabitacion) {
                %>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Tienes la habitacion <%= rh.getId_habitacion() %> con reserva des del <%= rh.getFecha_entrada() %> hasta el <%= rh.getFecha_salida() %>
                        <div class="d-flex gap-2">
                            <form action="reservaHabitacion" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= rh.getId() %>">
                                <input type="hidden" name="estado_r2" value="cancelado">
                                <button type="submit" name="action" value="update"
                                        class="btn btn-danger btn-sm"
                                        style="<%= rh.getEstado().equals(Estado_Reserva.cancelado) || rh.getEstado().equals(Estado_Reserva.completado) ? "visibility:hidden;" : "" %>">
                                    Cancelar reserva
                                </button>
                            </form>

                        </div>

                    </li>
                <%
                                }
                            } else {
                        %>
                                <li class="list-group-item">No tienes ninguna habitacion reservada</li>
                        <%
                            }
                        %>

            </ul>
            </div>
    </div>

    <div class="container mt-4 custom-margin">
       <div class="card p-3">
       <h3>Actividades Reservadas</h3>
            <ul class="list-group">
                <%
                    ArrayList<Reserva_Actividad> reservaActividad = (ArrayList<Reserva_Actividad>) request.getAttribute("Reserva_ActividadArrayList");

                    if (reservaActividad != null && !reservaActividad.isEmpty()) {
                        for (Reserva_Actividad ra : reservaActividad) {
                %>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Actividad <%= ra.getId_actividad() %> para el  <%= ra.getFecha() %> (ESTADO: <%= ra.getEstadoReserva() %>)
                        <div class="d-flex gap-2">

                           <form action="reservaActividad" method="post" style="display:inline;">
                                <input type="hidden" name="id_u" value="<%= ra.getId() %>">
                                <input type="hidden" name="estado_r2" value="completado">
                               <button type="submit" name="action" value="update"
                                       class="btn btn-primary btn-sm"
                                       style="<%= (ra.getEstadoReserva().equals(Estado_Reserva.completado) || ra.getEstadoReserva().equals(Estado_Reserva.cancelado)) ? "visibility:hidden;" : "" %>">
                                   Actividad Completada!
                               </button>
                           </form>

                            <form action="reservaActividad" method="post" style="display:inline;">
                                <input type="hidden" name="id_u" value="<%= ra.getId() %>">
                                <input type="hidden" name="estado_r2" value="cancelado">
                                <button type="submit" name="action" value="update"
                                        class="btn btn-danger btn-sm"
                                        style="<%= ra.getEstadoReserva().equals(Estado_Reserva.cancelado) || ra.getEstadoReserva().equals(Estado_Reserva.completado) ? "visibility:hidden;" : "" %>">
                                    Cancelar  reserva
                                </button>
                            </form>
                        </div>
                    </li>
                <%
                                }
                            } else {
                        %>
                                <li class="list-group-item">No tienes ninguna actividad reservada</li>
                        <%
                            }
                        %>

            </ul>
        </div>
    </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
