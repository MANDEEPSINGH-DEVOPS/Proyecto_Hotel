<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar Session</title>
    <!-- CSS de Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
             body {
                 background-image: url('<%= request.getContextPath() %>/imagen/fondo.jpg');
                 background-size: cover;
                 background-position: center;
                 background-repeat: no-repeat;
                 display: flex;
                 align-items: center;
                 justify-content: center;
                 min-height: 100vh;
                 font-family: Arial, sans-serif;
             }

              /* Estilos de la tarjeta */
              .card {
                  background-color: rgba(255, 255, 255, 0.9); /* Fondo blanco semi-transparente */
                  border: none;
                  border-radius: 8px;
                  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
              }
              .card-header {
                  background-color: #007BFF;
                  color: #fff;
                  border-top-left-radius: 8px;
                  border-top-right-radius: 8px;
                  font-size: 1.2rem;
                  text-align: center;
              }
              .btn-primary {
                  background-color: #007BFF;
                  border-color: #007BFF;
              }
              .btn-primary:hover {
                  background-color: #0056b3;
                  border-color: #0056b3;
              }

          </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">Login</div>
                    <div class="card-body">
                        <form action="login" method="post">
                            <input type="hidden" name="action" value="iniciar_seccion">
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre de Usuario</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" required>
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="d-flex gap-2">

                                <button type="submit" class="btn btn-primary w-100">Iniciar Session</button>
                                <button type="button" class="btn btn-primary w-100" onclick="window.location.href='index.jsp'">Volver Inicio</button>
                            </div>
                        </form>

                         <p style="color: red;">
                                    <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
                                </p>
                                <p>¿No tengo cuenta? <a href="/Proyecto_ABP_Hotel/register">Registrar por aquí</a>.</p>
                    </div>
                </div>
            </div>
        </div>


    </div>

    <!-- JS de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


