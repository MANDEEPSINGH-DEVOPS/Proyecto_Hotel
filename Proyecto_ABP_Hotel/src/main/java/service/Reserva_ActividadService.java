package service;

import dao.ActivitatsDao;
import dao.HabitacionsDao;
import dao.Reserva_ActividadDao;
import dao.Reserva_HabitacionDao;
import exceptions.DAOException;
import model.*;
import utils.Estado_H;
import utils.Estado_Reserva;
import utils.Rol;
import utils.Tipo_Habitacion;
import view.ActivitatsServlets;
import view.Reserva_ActividadServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class Reserva_ActividadService {

    public void GetReservas(HttpServletRequest request, HttpServletResponse response, Reserva_ActividadServlet reservaActividadServlet)
            throws ServletException, IOException {

        ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
        Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();

        try {
            reserva_ActividadDao.conectar();
            reservaActividadArrayList = reserva_ActividadDao.getAllRa(); // Recuperar lista

            request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);


            for (Reserva_Actividad reserva : reservaActividadArrayList) {
                System.out.println(reserva);
            }
            reservaActividadServlet.getServletContext().getRequestDispatcher("/jsp/reservas.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reserva_ActividadDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public void PostReservas(HttpServletRequest request, HttpServletResponse response, Reserva_ActividadServlet reserva_ActividadServlet)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();
        try {
            reserva_ActividadDao.conectar();
            HttpSession session = request.getSession();
            User usuarioActual = (User) session.getAttribute("login");

            reserva_ActividadDao.conectar();
            switch (action) {
                case "insert":

                    int id_user = usuarioActual.getId();
                    int id_act = Integer.parseInt(request.getParameter("id_act"));
                    String estado_r = request.getParameter("estado_r");
                    Estado_Reserva estado = (estado_r == null || estado_r.isEmpty()) ? Estado_Reserva.reservado : Estado_Reserva.valueOf(estado_r.toLowerCase());
                    System.out.println("ID Reserva: " + ", id_user: " + id_user+", id_act: "+id_act+", Estado:"+ estado);
                    Reserva_Actividad nuevoReserva_Actividad = new Reserva_Actividad(id_user,id_act, estado);
                    reserva_ActividadDao.insertNewRa(nuevoReserva_Actividad);

                    //Cargas:

                    //Actividades
                    ActivitatsDao activitatsDao = new ActivitatsDao();
                    ArrayList<Activitats> activitatsList = new ArrayList<>();
                    activitatsDao.conectar();
                    activitatsList = activitatsDao.getAllActividades();
                    for (Activitats actividad : activitatsList) {
                        if (actividad.getImagen() != null) {
                            String imagenBase64 = Base64.getEncoder().encodeToString(actividad.getImagen());
                            actividad.setImagenBase64(imagenBase64);
                        }
                    }

                    //Reserva de actividades
                    ArrayList<Reserva_Actividad> reservaActividadArrayList = new ArrayList<>();
                    Reserva_ActividadDao reserva_ActividadDao2 = new Reserva_ActividadDao();
                    reserva_ActividadDao2.conectar();
                    reservaActividadArrayList = reserva_ActividadDao2.getAllRa();

                    request.setAttribute("activitatsList", activitatsList);
                    request.setAttribute("Reserva_ActividadArrayList", reservaActividadArrayList);

                    reserva_ActividadServlet.getServletContext().getRequestDispatcher("/jsp/activitats.jsp").forward(request, response);
                    break;

                case "update":
                    int id_u = Integer.parseInt(request.getParameter("id_u"));
                    String estado_r2 = request.getParameter("estado_r2");

                    Estado_Reserva estado2 = (estado_r2 == null || estado_r2.isEmpty()) ? Estado_Reserva.reservado : Estado_Reserva.valueOf(estado_r2.toLowerCase());

                    System.out.println("ID Reserva: " + id_u + ", estado: " + estado2);

                    Reserva_Actividad nuevoReserva_Actividad1 = new Reserva_Actividad(id_u, estado2);

                    reserva_ActividadDao.updateRa(nuevoReserva_Actividad1);

                    //CARGAR LAS RA
                    //carregar reserves novament:
                    ArrayList<Reserva_Habitacion> reservaHabitacionesArrayList2 = new ArrayList<>();
                    ArrayList<Reserva_Actividad> reservaActividadesArrayList2 = new ArrayList<>();

                    Reserva_HabitacionDao reserva_HabitacionDao2 = new Reserva_HabitacionDao();
                    Reserva_ActividadDao reserva_ActividadDao3 = new Reserva_ActividadDao();

                    reserva_HabitacionDao2.conectar();
                    reserva_ActividadDao3.conectar();

                    ArrayList<Reserva_Habitacion> todasLasReservasHabitaciones = reserva_HabitacionDao2.getAllRh();
                    ArrayList<Reserva_Actividad> todasLasReservasActividades = reserva_ActividadDao3.getAllRa();

                    HttpSession session2 = request.getSession();
                    User usuarioActual2 = (User) session2.getAttribute("login");


                    for (Reserva_Habitacion reserva : todasLasReservasHabitaciones) {
                        System.out.println("Reserva ID Usuario: " + reserva.getId_usuario() + ", Usuario Actual: " + usuarioActual2.getId());
                        if (reserva.getId_usuario() == usuarioActual2.getId()) {
                            reservaHabitacionesArrayList2.add(reserva);
                        }
                    }


                    for (Reserva_Actividad ra : todasLasReservasActividades) {
                        System.out.println("Reserva Actividad ID Usuario: " + ra.getId_user() + ", Usuario Actual: " + usuarioActual2.getId());
                        if (ra.getId_user() == usuarioActual2.getId()) {
                            reservaActividadesArrayList2.add(ra);
                        }
                    }

                    //request i redireccio a reservas
                    request.setAttribute("reservaHabitacionesArrayList", reservaHabitacionesArrayList2);
                    request.setAttribute("Reserva_ActividadArrayList", reservaActividadesArrayList2);


                    reserva_ActividadServlet.getServletContext().getRequestDispatcher("/jsp/reservas.jsp").forward(request, response);
                    break;

            }
        } catch (ClassNotFoundException | SQLException | DAOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al conectar a la base de datos");
            reserva_ActividadServlet.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } finally {
            try {
                reserva_ActividadDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}