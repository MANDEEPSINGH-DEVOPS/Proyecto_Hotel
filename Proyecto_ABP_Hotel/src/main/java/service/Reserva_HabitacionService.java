package service;


import dao.HabitacionsDao;
import dao.Reserva_ActividadDao;
import dao.Reserva_HabitacionDao;
import exceptions.DAOException;
import model.Habitacions;
import model.Reserva_Actividad;
import model.Reserva_Habitacion;
import model.User;
import utils.Estado_H;
import utils.Estado_Reserva;
import view.Reserva_HabitacionServlet;
import view.UserServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class Reserva_HabitacionService {

    public void GetReserva_Habitacion(HttpServletRequest request, HttpServletResponse response, Reserva_HabitacionServlet reserva_HabitacionSerlet) throws ServletException, IOException {
        ArrayList<Reserva_Habitacion> reservaHabitacionesArrayList = new ArrayList<>();
        ArrayList<Reserva_Actividad> reservaActividadesArrayList = new ArrayList<>();
        Reserva_HabitacionDao reserva_HabitacionDao = new Reserva_HabitacionDao();
        Reserva_ActividadDao reserva_ActividadDao = new Reserva_ActividadDao();

        try {
            reserva_HabitacionDao.conectar();
            reserva_ActividadDao.conectar();


            ArrayList<Reserva_Habitacion> todasLasReservasHabitaciones = reserva_HabitacionDao.getAllRh();
            ArrayList<Reserva_Actividad> todasLasReservasActividades = reserva_ActividadDao.getAllRa();

            HttpSession session = request.getSession();
            User usuarioActual = (User) session.getAttribute("login");


            // Filtrar reservas de habitaciones
            for (Reserva_Habitacion reserva : todasLasReservasHabitaciones) {
                if (reserva.getId_usuario() == usuarioActual.getId()) {
                    reservaHabitacionesArrayList.add(reserva);
                }
            }

            // Filtrar reservas de actividades
            for (Reserva_Actividad ra : todasLasReservasActividades) {
                if (ra.getId_user() == usuarioActual.getId()) {
                    reservaActividadesArrayList.add(ra);
                }
            }


            request.setAttribute("reservaHabitacionesArrayList", reservaHabitacionesArrayList);
            request.setAttribute("Reserva_ActividadArrayList", reservaActividadesArrayList);

            reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/reservas.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al conectar a la base de datos");
            reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/index.jsp").forward(request, response);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reserva_HabitacionDao.desconectar();
                reserva_ActividadDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void PostReserva_Habitacion(HttpServletRequest request, HttpServletResponse response, Reserva_HabitacionServlet reserva_HabitacionSerlet)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Reserva_HabitacionDao reserva_HabitacionDao = new Reserva_HabitacionDao();

        try {
            reserva_HabitacionDao.conectar();
            HttpSession session = request.getSession();
            User usuarioActual = (User) session.getAttribute("login");
            switch (action) {

                case "insert":
                    try {
                        int id_usuario = usuarioActual.getId();
                        int id_habitacion = Integer.parseInt(request.getParameter("id_habitacion"));
                        LocalDate fecha_entrada = LocalDate.parse(request.getParameter("fecha_entrada"));
                        LocalDate fecha_salida = LocalDate.parse(request.getParameter("fecha_salida"));
                        Estado_Reserva estado_r = Estado_Reserva.valueOf(request.getParameter("estado_r").toLowerCase());

                        // Obtener todas las reservas para verificar conflictos
                        ArrayList<Reserva_Habitacion> reservas = reserva_HabitacionDao.getAllRh();
                        boolean conflicto = false;

                        //For en condicio
                        for (Reserva_Habitacion reserva : reservas) {
                            if (reserva.getId_habitacion() == id_habitacion && reserva.getEstado().equals(Estado_Reserva.reservado)) {

                                // Verificar si las fechas de la nueva reserva se solapan con las de la reserva existente
                                boolean fechasSolapadas = (fecha_entrada.isBefore(reserva.getFecha_salida()) && fecha_salida.isAfter(reserva.getFecha_entrada()));

                                // Si las fechas se solapan y es un usuario diferente, marcar conflicto
                                if (fechasSolapadas && reserva.getId_usuario() != id_usuario) {
                                    conflicto = true;
                                    break;
                                }
                            }
                        }

                        if (conflicto) {
                            // Si hay un conflicto, redirigir con el mensaje de conflicto
                            ArrayList<Reserva_Habitacion> reservaHabitacionesArrayList = new ArrayList<>();
                            Reserva_HabitacionDao reserva_HabitacionDao2 = new Reserva_HabitacionDao();
                            reserva_HabitacionDao2.conectar();
                            reservaHabitacionesArrayList = reserva_HabitacionDao2.getAllRh();

                            ArrayList<Habitacions> habitacionsArrayList = new ArrayList<>();
                            HabitacionsDao habitacionsDao = new HabitacionsDao();
                            habitacionsDao.conectar();
                            habitacionsArrayList = habitacionsDao.getAllHabitaciones();

                            for (Habitacions habitacion : habitacionsArrayList) {
                                if (habitacion.getImagen() != null) {
                                    String imagenBase64 = Base64.getEncoder().encodeToString(habitacion.getImagen());
                                    habitacion.setImagenBase64(imagenBase64);
                                }
                            }

                            request.setAttribute("conflictoReserva", true);

                            // Pasar las listas al JSP
                            request.setAttribute("habitacionsArrayList", habitacionsArrayList);
                            request.setAttribute("reservaHabitacionesArrayList", reservaHabitacionesArrayList);

                            reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);
                            return;
                        }

                        // Si no hay conflicto, proceder a insertar la nueva reserva
                        Reserva_Habitacion nuevaReserva = new Reserva_Habitacion(id_usuario, id_habitacion, fecha_entrada, fecha_salida, estado_r);
                        reserva_HabitacionDao.insertNewRh(nuevaReserva);

                        // Volver a cargar las reservas del usuario actual
                        ArrayList<Reserva_Habitacion> reservasActualizadas = reserva_HabitacionDao.getAllRh();
                        ArrayList<Reserva_Habitacion> reservasUsuario = new ArrayList<>();
                        for (Reserva_Habitacion reserva : reservasActualizadas) {
                            if (reserva.getId_usuario() == id_usuario) {
                                reservasUsuario.add(reserva);
                            }
                        }

                        request.setAttribute("reservaHabitacionesArrayList", reservasUsuario);

                        reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);

                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute("errorMessage", "Error al procesar la reserva");
                        reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);
                    }
                    break;

                case "update":
                    // Obtener los parámetros de la solicitud
                    int id = Integer.parseInt(request.getParameter("id"));

                    Estado_Reserva estado_r2 = Estado_Reserva.valueOf(request.getParameter("estado_r2").toLowerCase());


                    //SOUT
                    System.out.println("ID Usuario: " + id +  ", Estado: " + estado_r2);

                    Reserva_Habitacion updateReserva = new Reserva_Habitacion(id, estado_r2);

                    try {
                        reserva_HabitacionDao.updateReservaHabitacion(updateReserva);
                        //carregar reserves novament:
                        ArrayList<Reserva_Habitacion> reservaHabitacionesArrayList2 = new ArrayList<>();
                        ArrayList<Reserva_Actividad> reservaActividadesArrayList2 = new ArrayList<>();

                        Reserva_HabitacionDao reserva_HabitacionDao2 = new Reserva_HabitacionDao();
                        Reserva_ActividadDao reserva_ActividadDao2 = new Reserva_ActividadDao();

                        reserva_HabitacionDao2.conectar();
                        reserva_ActividadDao2.conectar();

                        ArrayList<Reserva_Habitacion> todasLasReservasHabitaciones = reserva_HabitacionDao2.getAllRh();
                        ArrayList<Reserva_Actividad> todasLasReservasActividades = reserva_ActividadDao2.getAllRa();

                        HttpSession session2 = request.getSession();
                        User usuarioActual2 = (User) session2.getAttribute("login");

                        // Filtrar reservas de habitaciones
                        for (Reserva_Habitacion reserva : todasLasReservasHabitaciones) {
                            System.out.println("Reserva ID Usuario: " + reserva.getId_usuario() + ", Usuario Actual: " + usuarioActual2.getId());
                            if (reserva.getId_usuario() == usuarioActual2.getId()) {
                                reservaHabitacionesArrayList2.add(reserva);
                            }
                        }

                        // Filtrar reservas de actividades
                        for (Reserva_Actividad ra : todasLasReservasActividades) {
                            System.out.println("Reserva Actividad ID Usuario: " + ra.getId_user() + ", Usuario Actual: " + usuarioActual2.getId());
                            if (ra.getId_user() == usuarioActual2.getId()) {
                                reservaActividadesArrayList2.add(ra);
                            }
                        }

                        //request i redireccio a reservas
                        request.setAttribute("reservaHabitacionesArrayList", reservaHabitacionesArrayList2);
                        request.setAttribute("Reserva_ActividadArrayList", reservaActividadesArrayList2);

                        reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/reservas.jsp").forward(request, response);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        request.setAttribute("errorMessage", "Error al insertar la reserva en la base de datos");
                        reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/index.jsp").forward(request, response);
                    }
                    break;


            }
        } catch (ClassNotFoundException | SQLException | DAOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al conectar a la base de datos");
            reserva_HabitacionSerlet.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } finally {
            try {
                reserva_HabitacionDao.desconectar(); // Desconectar al final
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }





}