package service;
import dao.UserDao;
import exceptions.ServiceException;
import model.User;
import utils.Rol;
import view.RegisterServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;

public class RegisterService {

    public void GetRegister(HttpServletRequest request, HttpServletResponse response, RegisterServlets registerServlets)
            throws ServletException, IOException {
        // Redirigir al jsp
        registerServlets.getServletContext().getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }

    public void PostRegister(HttpServletRequest request, HttpServletResponse response, RegisterServlets registerServlets)
            throws ServletException, IOException, ServiceException, SQLException {
        String action = request.getParameter("action");


        switch (action) {
            case "insert":
                String nombre = request.getParameter("nombre");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String rolString = request.getParameter("rolString");


                UserDao userDao = new UserDao();
                try {
                    userDao.conectar();

                    byte[] hashIngresado = generarHash(password.getBytes());
                    String hashIngresadoHex = convertirBytesAHexadecimal(hashIngresado);

                    // Convertir rol a string
                    Rol rol = (rolString == null || rolString.isEmpty()) ? Rol.cliente : Rol.valueOf(rolString.toLowerCase());

                    User newUser = new User();
                    newUser.setUsername(nombre);
                    newUser.setEmail(email);
                    newUser.setPassword(hashIngresadoHex);
                    newUser.setRol(rol);



                    userDao.insertNewUser(newUser);

                    // Almacenar el usuario en la sesión
                    request.getSession().setAttribute("login", newUser);
                    registerServlets.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                } catch (Exception e) {
                    throw new ServiceException(ServiceException.ERROR_DatosInvalidos, e);
                } finally {
                    userDao.desconectar();  // Asegurarse de desconectar
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
