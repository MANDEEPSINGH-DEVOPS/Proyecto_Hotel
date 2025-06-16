<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Redirigiendo...</title>
    <script type="text/javascript">
        // Redirigir automáticamente después de cargar la página
        window.location.href = "<%= request.getContextPath() %>/jsp/welcome.jsp";
    </script>
</head>
<body>
    <p>Redirigiendo a la página de bienvenida...</p>
</body>
</html>
