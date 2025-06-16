<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página Principal</title>
    <style>
        /* Estilos CSS */
        * {
            box-sizing: border-box;
        }

        html, body {
            height: 100vh; /* Asegura que el fondo cubra toda la altura de la ventana */
            width: 100vw; /* Asegura que el fondo cubra toda la anchura de la ventana */
            margin: 0;
            background-image: url('<%= request.getContextPath() %>/imagen/fondo.jpg');
            background-size: cover; /* Escala la imagen para que cubra completamente el fondo */
            background-position: center center; /* Centra la imagen en la pantalla */
            background-repeat: no-repeat; /* Evita que la imagen se repita */
            font-family: Arial, sans-serif; /* Fuente de la página */
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .container {
            max-width: 500px;
            width: 100%;
            padding: 20px;
            background: rgba(255, 255, 255, 0.8); /* Fondo blanco semitransparente */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
        }

        h1 {
            color: #007BFF;
            margin-bottom: 20px;
        }

        .button-container {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
        }

        .button {
            display: inline-block;
            padding: 15px 30px;
            font-size: 16px;
            color: white;
            background-color: #007BFF;
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s;
        }

        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenido al Sistema de Gestión Hotelera</h1>
        <p>Por favor, elige una opción:</p>
        <div class="button-container">
            <a class="button" href="<%= request.getContextPath() %>/login">Iniciar Sesión</a>
            <a class="button" href="<%= request.getContextPath() %>/register">Registrarse</a>
        </div>
    </div>
</body>
</html>
