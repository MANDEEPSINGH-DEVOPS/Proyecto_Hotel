<%@page import="model.Activitats" %>
<%@page import="model.Reserva_Actividad" %>
<%@page import="model.User" %>
<%@page import="utils.Estado_Reserva" %>
<%@page import="java.util.ArrayList" %>
<%@page import="utils.Rol"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Actividades</title>
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

        .card-body .btn-secondary {
            font-size: 1rem;
            padding: 10px 20px;
            color: #fff;
            background-color: #6c757d;
            border: none;
            border-radius: 5px;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <a class="navbar-brand" href="#">ACTIVIDADES</a>
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
    <div class="row justify-content-center">
        <%
            ArrayList<Activitats> activitatsList = (ArrayList<Activitats>) request.getAttribute("activitatsList");
            ArrayList<Reserva_Actividad> reservasList = (ArrayList<Reserva_Actividad>) request.getAttribute("Reserva_ActividadArrayList");
            User usuarioActual = (User) session.getAttribute("login");

            if (activitatsList != null && !activitatsList.isEmpty()) {
                for (Activitats actividad : activitatsList) {
                    boolean estaReservada = false;

                    if (reservasList != null && !reservasList.isEmpty()) {
                        for (Reserva_Actividad reserva : reservasList) {
                            if (reserva.getId_actividad() == actividad.getId() && reserva.getId_user() == usuarioActual.getId()
                                    && reserva.getEstadoReserva().equals(Estado_Reserva.reservado)) {
                                estaReservada = true;
                                break;
                            }
                        }
                    }
        %>
        <div class="col-md-6 col-lg-4 mb-4">
            <div class="card">
                <img src="data:image/jpeg;base64,<%= actividad.getImagenBase64() %>" alt="Imagen de la Actividad" class="img-habitacion" />
                <div class="card-body">
                    <h5 class="card-title"><%= actividad.getNom().toUpperCase() %></h5>
                    <p><strong>Descripción:</strong> <%= actividad.getDescripcion() %></p>
                    <p><strong>Cupo:</strong> <%= actividad.getCupo() %> Personas</p>
                    <p><strong>Fecha:</strong> <%= actividad.getFecha_actividad() %></p>
                    <p><strong>Precio:</strong> <%= actividad.getPrecio() %> Euros</p>

                    <%
                    if (estaReservada) {
                    %>
                    <button class="btn btn-secondary" disabled>Ya Reservado</button>
                    <%
                    } else {
                    %>
                    <form action="reservaActividad" method="post">
                        <input type="hidden" name="id_act" value="<%= actividad.getId() %>">
                        <input type="hidden" name="estado_r" value="reservado">
                        <button type="submit" class="btn btn-primary" name="action" value="insert">Reservar</button>
                    </form>
                    <%
                    }
                    %>
                </div>
            </div>
        </div>
        <%
                }
            } else {
        %>
        <div class="alert alert-warning text-center" role="alert">
            No se encontraron actividades.
        </div>
        <%
            }
        %>
    </div>
</div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
