package service;

import dao.ActivitatsDao;
import dao.Reserva_ActividadDao;
import exceptions.DAOException;
import exceptions.ServiceException;
import model.Activitats;
import model.Habitacions;
import model.Reserva_Actividad;
import model.User;
import utils.Estado_Reserva;
import view.ActivitatsServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

public class ActivitatsService {

    public void GetActivitat(HttpServletRequest request, HttpServletResponse response, ActivitatsServlets ActivitatsServlets)
            throws ServletException, IOException, ServiceException {
        ActivitatsDao activitatsDao = new ActivitatsDao();
        ArrayList<Activitats> activitatsList = new ArrayList<>();

        try {
            activitatsDao.conectar();

            HttpSession session = request.getSession();
            User usuarioActual = (User) session.getAttribute("login");

            if (usuarioActual == null) {
                // Si no hay usuario autenticado, redirigir al login
                request.setAttribute("errorMessage", "Por favor, inicia sesión para ver tus reservas.");
                ActivitatsServlets.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                return;
            }

            ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
            Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();
            reserva_ActividadDao.conectar();
            reservaActividadArrayList = reserva_ActividadDao.getAllRa();

            activitatsList = activitatsDao.getAllActividades();

            // Aquí no es necesario hacer cambios, ya que la lista de reservas ya se pasa como 'reservasList'

            // Convertir la imagen a Base64
            for (Activitats actividad : activitatsList) {
                if (actividad.getImagen() != null) {
                    String imagenBase64 = Base64.getEncoder().encodeToString(actividad.getImagen());
                    actividad.setImagenBase64(imagenBase64);
                }
            }

            request.setAttribute("activitatsList", activitatsList);
            request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);

            // Redirigimos al JSP
            ActivitatsServlets.getServletContext().getRequestDispatcher("/jsp/activitats.jsp").forward(request, response);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);  // Errores de base de datos ya gestionados
        } catch (DAOException e) {
            throw new RuntimeException(e);  // Gestionado a nivel DAO
        } finally {
            try {
                activitatsDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void PostActivitat(HttpServletRequest request, HttpServletResponse response, ActivitatsServlets activitatsService)
            throws ServletException, IOException, ServiceException {
        String action = request.getParameter("action");

        ActivitatsDao activitatsDao = new ActivitatsDao();

        switch (action) {
            case "insert":
                try {
                    // Obtener parámetros
                    String nom = request.getParameter("nom");
                    String descripcion = request.getParameter("descripcion");
                    float precio = Float.parseFloat(request.getParameter("precio"));
                    int cupo = Integer.parseInt(request.getParameter("cupo"));
                    LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));

                    // Validar datos
                    if (nom == null || descripcion == null || precio <= 0 || cupo <= 0) {
                        throw new ServiceException(ServiceException.ERROR_DatosInvalidos);
                    }

                    HttpSession session = request.getSession();
                    User usuarioActual = (User) session.getAttribute("login");

                    if (usuarioActual != null) {
                        // El usuario está autenticado, puedes usar su información
                        System.out.println("Usuario autenticado: " + usuarioActual.getUsername());
                        activitatsDao.conectar();
                        Activitats nuevaActividad = new Activitats(nom, descripcion, precio, cupo, fecha);
                        activitatsDao.insertNewActividad(nuevaActividad);

                        request.getSession().setAttribute("newActivity", nuevaActividad);
                        activitatsService.getServletContext().getRequestDispatcher("/jsp/activitats.jsp").forward(request, response);
                    }else{
                        response.sendRedirect("/jsp/login.jsp");
                    }
                } catch (ServiceException | ClassNotFoundException | SQLException e) {
                    request.setAttribute("errorMessage", "Error: " + e.getMessage());
                    activitatsService.getServletContext().getRequestDispatcher("/jsp/index.jsp").forward(request, response);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        activitatsDao.desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "update":
                try {
                    // Obtén el ID de la actividad a actualizar
                    int id_up = Integer.parseInt(request.getParameter("id_up"));

                    // Recupera los demás parámetros
                    String nom2 = request.getParameter("nom2");
                    String descripcion2 = request.getParameter("descripcion2");
                    float precio2 =  Float.parseFloat(request.getParameter("precio2"));
                    int cupo2 = Integer.parseInt(request.getParameter("cupo2"));
                    LocalDate fecha2 = LocalDate.parse(request.getParameter("fecha2"));

                    activitatsDao.conectar();
                    Activitats actividadActualizada = new Activitats(nom2, descripcion2, precio2, cupo2, fecha2);
                    activitatsDao.updateActividad(actividadActualizada, id_up);

                    request.getSession().setAttribute("actividadActualizada", actividadActualizada);

                    activitatsService.getServletContext().getRequestDispatcher("/jsp/activitats.jsp").forward(request, response);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                break;


            case "delete":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));

                    activitatsDao.conectar();
                    activitatsDao.deleteActividad(id);

                    activitatsService.getServletContext().getRequestDispatcher("/jsp/activitats.jsp").forward(request, response);

                } catch (DAOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                break;


        }
    }
}
