package service;

import dao.Reserva_ActividadDao;
import dao.UserDao;
import exceptions.DAOException;
import exceptions.ServiceException;
import model.Reserva_Actividad;
import model.User;
import utils.Estado;
import utils.Rol;
import view.AdminServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminService {

    public void GetAdmin(HttpServletRequest request, HttpServletResponse response, AdminServlets adminServlets)
            throws ServletException, IOException, ServiceException, DAOException, SQLException, ClassNotFoundException {

        ArrayList<User> userArrayList = new ArrayList<>();
        UserDao userDao = new UserDao();

        try {
            userDao.conectar();
            userArrayList = userDao.getAllUsers();

            for (User user : userArrayList) {
                if(user.getRol() == Rol.admin){
                    System.out.println(user);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                userDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //
        ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
        Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();
        reserva_ActividadDao.conectar();

        reservaActividadArrayList = reserva_ActividadDao.getAllRa();

        request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);
        //

        request.setAttribute("userArrayList", userArrayList);
        adminServlets.getServletContext().getRequestDispatcher("/jsp/admin.jsp").forward(request, response);
    }

    public void PostAdmin(HttpServletRequest request, HttpServletResponse response, AdminServlets adminServlets)
            throws ServletException, IOException, ServiceException {
        String action = request.getParameter("action");


        switch (action) {
            case "admin":
                int id_update = Integer.parseInt(request.getParameter("id_update"));
                String rolString2 = request.getParameter("rolString2");

                UserDao userDao2 = new UserDao();
                try {

                    userDao2.conectar();

                    Rol rol = (rolString2 == null || rolString2.isEmpty()) ? Rol.cliente : Rol.valueOf(rolString2);

                    User updateUser = new User();
                    updateUser.setId(id_update);
                    updateUser.setRol(rol);

                    userDao2.updateAdmin(updateUser.getId());
                    //
                    ArrayList<User> userArrayList = new ArrayList<>();

                    userArrayList = userDao2.getAllUsers();

                    request.setAttribute("userArrayList", userArrayList);
                    //
                    ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
                    Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();
                    reserva_ActividadDao.conectar();

                    reservaActividadArrayList = reserva_ActividadDao.getAllRa();

                    request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);
                    //
                    request.getSession().setAttribute("admin", updateUser);
                    adminServlets.getServletContext().getRequestDispatcher("/jsp/admin.jsp").forward(request, response);

                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Error al conectar a la base de datos");
                    adminServlets.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        userDao2.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "insert":
                UserDao userDao = new UserDao();

                String usu = request.getParameter("usu");
                String email = request.getParameter("email");
                String pass = request.getParameter("pass");
                String rolString = request.getParameter("rolString");

                try {
                    userDao.conectar();

                    byte[] hashIngresado = generarHash(pass.getBytes());
                    String hashIngresadoHex = convertirBytesAHexadecimal(hashIngresado);

                    // Convertir rol a string
                    Rol rol = (rolString == null || rolString.isEmpty()) ? Rol.cliente : Rol.valueOf(rolString.toLowerCase());

                    User newUser = new User();
                    newUser.setUsername(usu);
                    newUser.setEmail(email);
                    newUser.setPassword(hashIngresadoHex);
                    newUser.setRol(rol);



                    // Método de inserción
                    userDao.insertNewUser(newUser);
                    ArrayList<User> userArrayList = new ArrayList<>();

                    userArrayList = userDao.getAllUsers();

                    request.setAttribute("userArrayList", userArrayList);
                    //
                    ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
                    Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();
                    reserva_ActividadDao.conectar();

                    reservaActividadArrayList = reserva_ActividadDao.getAllRa();
                    //
                    request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);
                    request.getSession().setAttribute("admin", newUser);
                    adminServlets.getServletContext().getRequestDispatcher("/jsp/admin.jsp").forward(request, response);

                } catch (ClassNotFoundException | SQLException e) {
                    request.setAttribute("errorMessage", "Error: " + e.getMessage());
                    adminServlets.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        userDao.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "delete":

                String estadoString2 = request.getParameter("estadoString2");
                int id = Integer.parseInt(request.getParameter("id"));

                UserDao userDao3 = new UserDao();

                try {
                    userDao3.conectar();

                    Estado estado = Estado.valueOf(estadoString2.toLowerCase());

                    User deleteUser = new User();

                    deleteUser.setEstado(estado);
                    deleteUser.setId(id);

                    userDao3.eliminarUser(deleteUser.getId());

                    ArrayList<User> userArrayList = new ArrayList<>();

                    userArrayList = userDao3.getAllUsers();

                    request.setAttribute("userArrayList", userArrayList);
                    //
                    ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
                    Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();
                    reserva_ActividadDao.conectar();

                    reservaActividadArrayList = reserva_ActividadDao.getAllRa();
                    //
                    request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);
                    request.getSession().setAttribute("admin", deleteUser);
                    adminServlets.getServletContext().getRequestDispatcher("/jsp/admin.jsp").forward(request, response);

                } catch (ClassNotFoundException | SQLException | DAOException e) {
                    request.setAttribute("errorMessage", "Error: " + e.getMessage());
                    adminServlets.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                } finally {
                    try {
                        userDao3.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;


            case "searchById":
                int id_search = Integer.parseInt(request.getParameter("id_search"));
                UserDao userDao4 = new UserDao();

                try {
                    userDao4.conectar();

                    User user_sarch = userDao4.getUserById(id_search);

                    System.out.println(user_sarch);


                    request.getSession().setAttribute("perfil", user_sarch);
                    adminServlets.getServletContext().getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);

                } catch (ClassNotFoundException | SQLException | DAOException e) {
                    request.setAttribute("errorMessage", "Error: " + e.getMessage());
                    adminServlets.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                } finally {
                    try {
                        userDao4.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                break;
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

