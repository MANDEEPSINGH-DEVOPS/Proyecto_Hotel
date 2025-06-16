<%@page import="model.Habitacions" %>
<%@page import="utils.Rol"%>
<%@page import="model.User" %>
<%@page import="view.HabitacionsServlet" %>
<%@page import="service.HabitacionsService" %>
<%@page import="model.Reserva_Habitacion" %>
<%@page import="service.Reserva_HabitacionService" %>
<%@page import="java.util.ArrayList" %>
<%@page import="utils.Estado_Reserva" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Habitaciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #a3c4f3; /* Azul pastel suave */
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #333;
        }

        .navbar {
            background-color: rgba(0, 123, 255, 0.9);
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1000;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .navbar .navbar-brand, .navbar .nav-link {
            font-size: 1.1rem;
            color: #fff;
        }

        .navbar .nav-link:hover {
            color: #dfe9f3;
        }

        .container {
            margin-top: 100px;
        }

        .card {
            background-color: rgba(255, 255, 255, 0.95);
            border: none;
            border-radius: 10px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
        }

        .img-habitacion {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 0.25rem 0.25rem 0 0;
        }

        .card-body {
            padding: 20px;
            text-align: center;
        }

        .card-body h5 {
            font-size: 1.25rem;
            color: #007BFF;
            margin-bottom: 10px;
        }

        .card-body p {
            font-size: 1rem;
            color: #555;
        }

        .card-body .btn-primary {
            font-size: 1rem;
            padding: 10px 20px;
            color: #fff;
            background-color: #007BFF;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .card-body .btn-primary:hover {
            background-color: #0056b3;
        }

        /* Para el Popup de Alerta */
        .alert {
            display: none;
        }
    </style>

    <script type="text/javascript">
        function showConflictAlert() {
            alert("¡La reserva ya existe! Hay un conflicto con las fechas seleccionadas.");
        }
    </script>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <a class="navbar-brand" href="#">HABITACIONES</a>
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

    <div class="container">
        <%
            // Si hay un mensaje de error, se muestra en el popup
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="alert alert-danger text-center mt-3" role="alert">
                <%= errorMessage %>
            </div>
        <%
            }

            // Mostrar el conflicto si existe
            Boolean conflictoReserva = (Boolean) request.getAttribute("conflictoReserva");
            if (conflictoReserva != null && conflictoReserva) {
        %>
            <div class="alert alert-danger text-center mt-3" role="alert">
                ¡Hay un conflicto con las fechas seleccionadas! La habitación ya está reservada en ese rango de fechas.
            </div>
        <%
            }
        %>

        <!-- Mostrar las habitaciones y reservas -->
        <div class="row justify-content-center">
            <%
                // Se obtienen las habitaciones y las reservas
                ArrayList<Habitacions> habitaciones = (ArrayList<Habitacions>) request.getAttribute("habitacionsArrayList");
                ArrayList<Reserva_Habitacion> reservaHabitacion = (ArrayList<Reserva_Habitacion>) request.getAttribute("reservaHabitacionesArrayList");

                // Si hay habitaciones disponibles, las mostramos
                if (habitaciones != null && !habitaciones.isEmpty()) {
                    for (Habitacions h : habitaciones) {
            %>

            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card">
                    <img src="data:image/jpeg;base64,<%= h.getImagenBase64() %>" alt="Imagen de la Habitación" class="img-habitacion" />

                    <div class="card-body">
                        <h5 class="card-title">Habitación <%= h.getId() %></h5>
                        <p><strong>Tipo:</strong> <%= h.getTipo_habitacion() %></p>

                        <%
                            boolean hayReservas = false;
                            if (reservaHabitacion != null && !reservaHabitacion.isEmpty()) {
                                for (Reserva_Habitacion rh : reservaHabitacion) {
                                    if (h.getId() == rh.getId_habitacion() && rh.getEstado().equals(Estado_Reserva.reservado)) {
                                        hayReservas = true;
                        %>
                                        <p>De <%= rh.getFecha_entrada() %> al <%= rh.getFecha_salida() %></p>
                        <%
                                    }
                                }
                            }
                        %>

                        <p><%= h.getPrice() %> €</p>

                        <form action="reservaHabitacion" method="post">
                            <input type="hidden" name="id_habitacion" value="<%= h.getId() %>">
                            <div class="mb-3">
                                <label for="fecha_entrada_<%= h.getId() %>">Fecha de entrada:</label>
                                <input type="date" id="fecha_entrada" name="fecha_entrada" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="fecha_salida_<%= h.getId() %>">Fecha de salida:</label>
                                <input type="date" id="fecha_salida" name="fecha_salida" class="form-control" required>
                            </div>
                            <input type="hidden" name="estado_r" value="reservado">
                            <button type="submit" name="action" value="insert" class="btn btn-primary">
                                Confirmar Reserva
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <%
                    }
                } else {
            %>
            <div class="alert alert-warning text-center" role="alert">
                No se encontraron habitaciones disponibles.
            </div>
            <%
                }
            %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
