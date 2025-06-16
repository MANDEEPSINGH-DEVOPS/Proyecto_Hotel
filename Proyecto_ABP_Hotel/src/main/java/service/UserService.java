package service;

import exceptions.DAOException;
import exceptions.ServiceException;
import model.Reserva_Habitacion;
import model.User;
import utils.Estado;
import utils.Rol;
import dao.UserDao;
import view.ActivitatsServlets;
import view.UserServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UserService {

    public void GetLogin(HttpServletRequest request, HttpServletResponse response, UserServlets userServlets)
            throws ServletException, IOException, ServiceException {
        ArrayList<User> login = new ArrayList<>();
        UserDao userDao = new UserDao();
// Obtener el parámetro "action" y verificar si es null
        String action = request.getParameter("action");

        if (action == null) {
            // Si action es null, puedes establecer un valor por defecto o manejar el error
            action = "todo"; // por ejemplo, "todo" como valor por defecto
        }

        switch (action) {
            case "todo":
                try {
                    userDao.conectar();
                    login = userDao.getAllUsers();

                    request.setAttribute("login", login);
                    userServlets.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        userDao.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "por_id":
                try {
                    userDao.conectar();
                    User usuarioActual = (User) request.getSession().getAttribute("login");

                    if (usuarioActual != null) {
                        request.setAttribute("perfil", usuarioActual);
                        userServlets.getServletContext().getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("login.jsp");
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        userDao.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    public void PostLogin(HttpServletRequest request, HttpServletResponse response, UserServlets userServlets)
            throws ServletException, IOException, ServiceException, SQLException {
        String action = request.getParameter("action");

        if (action == null) {
            throw new ServiceException(ServiceException.ERROR_DatosInvalidos);  // Acción no especificada
        }

        switch (action) {
            case "iniciar_seccion":
                String nombre = request.getParameter("nombre");
                String password = request.getParameter("password");

                if (nombre == null || nombre.isEmpty() || password == null || password.isEmpty()) {
                    throw new ServiceException(ServiceException.ERROR_DatosInvalidos);  // Datos inválidos
                }

                UserDao userDao = new UserDao();
                try {
                    userDao.conectar();

                    byte[] hashIngresado = generarHash(password.getBytes());
                    String hashIngresadoHex = convertirBytesAHexadecimal(hashIngresado);

                    User login = userDao.getUserByUsernameAndPassword(nombre, hashIngresadoHex);

                    // Guardar usuario al logar
                    if (login != null) {
                        request.getSession().setAttribute("login", login);
                        response.sendRedirect(request.getContextPath() + "/activitats");

                    } else {
                        // Si el usuario no existe, establecer un mensaje de error y redirigir a la página de inicio de sesión
                        request.setAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos.");
                        userServlets.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServiceException(ServiceException.ERROR_DatosInvalidos, e);  // Error genérico
                } finally {
                    userDao.desconectar();
                }
                break;

            default:
                throw new ServiceException(ServiceException.ERROR_DatosInvalidos);
        }

    }


    public static byte[] generarHash(byte[] datos) throws Exception {
        // Crear un objeto MessageDigest para generar el hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Proporcionar los datos para generar el hash
        digest.update(datos);

        // Generar el hash
        return digest.digest();
    }

    public static String convertirBytesAHexadecimal(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


}
