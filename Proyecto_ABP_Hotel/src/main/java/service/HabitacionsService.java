package service;

import dao.Reserva_HabitacionDao;
import exceptions.DAOException;
import model.Habitacions;
import dao.HabitacionsDao;
import model.Reserva_Habitacion;
import utils.Estado_H;
import utils.Rol;
import utils.Tipo_Habitacion;
import view.HabitacionsServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class HabitacionsService {

    public void GetHabitacions(HttpServletRequest request, HttpServletResponse response, HabitacionsServlet habitacionsServlet)
            throws ServletException, IOException {

        ArrayList<Habitacions> habitacionsArrayList = new ArrayList<>();
        HabitacionsDao habitacionsDao = new HabitacionsDao();

        try {
            habitacionsDao.conectar();
            habitacionsArrayList = habitacionsDao.getAllHabitaciones();
            for (Habitacions habitacion : habitacionsArrayList) {
                if (habitacion.getImagen() != null) {
                    String imagenBase64 = Base64.getEncoder().encodeToString(habitacion.getImagen());
                    habitacion.setImagenBase64(imagenBase64);
                }
            }

            //--
            ArrayList<Reserva_Habitacion> reservaHabitacionesArrayList = new ArrayList<>();
            Reserva_HabitacionDao reserva_HabitacionDao = new Reserva_HabitacionDao();
            reserva_HabitacionDao.conectar();
            reservaHabitacionesArrayList = reserva_HabitacionDao.getAllRh();

            request.setAttribute("reservaHabitacionesArrayList", reservaHabitacionesArrayList);
            //--

            // Recuperar lista
            request.setAttribute("habitacionsArrayList", habitacionsArrayList);
            habitacionsServlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);

            for (Habitacions habitacion : habitacionsArrayList) {
                System.out.println(habitacion);
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                habitacionsDao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void PostHabitacion(HttpServletRequest request, HttpServletResponse response, HabitacionsServlet habitacionsServlet)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HabitacionsDao habitacionsDao = new HabitacionsDao();

        try {
            habitacionsDao.conectar();

            switch (action) {
                case "insert":

                    String tipoHabitacionStr = request.getParameter("tipoHabitacionStr");
                    String priceStr = request.getParameter("priceStr");
                    String estadoStr = request.getParameter("estadoStr");

                    try {
                        Tipo_Habitacion tipoHabitacion = Tipo_Habitacion.valueOf(tipoHabitacionStr.toLowerCase());
                        float price = Float.parseFloat(priceStr);
                        Estado_H estado = Estado_H.valueOf(estadoStr.toLowerCase());

                        Habitacions nuevaHabitacion = new Habitacions(tipoHabitacion, price, estado);

                        habitacionsDao.insertNewHabitacion(nuevaHabitacion);

                        request.setAttribute("successMessage", "Habitación añadida correctamente");
                        habitacionsServlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute("errorMessage", "Datos incorrectos: " + e.getMessage());
                        habitacionsServlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);
                    }

                    break;
                case "update":
                    String idStr = request.getParameter("idStr");
                    String estadoStr1 = request.getParameter("estadoStr1");

                    int id = Integer.parseInt(idStr);
                    Estado_H estado = Estado_H.valueOf(estadoStr1.toLowerCase());

                    Habitacions habitacionActualizada = new Habitacions(id,estado);

                    habitacionsDao.updateHabitacion(habitacionActualizada);

                    habitacionsServlet.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                    break;

                case "delete":

                    int deleteId = Integer.parseInt(request.getParameter("deleteId"));
                    habitacionsDao.deleteHabitacion(deleteId);

                    request.setAttribute("successMessage", "Habitación eliminada correctamente");
                    habitacionsServlet.getServletContext().getRequestDispatcher("/jsp/habitacions.jsp").forward(request, response);
                    break;
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al conectar a la base de datos");
            habitacionsServlet.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                habitacionsDao.desconectar(); // Desconectar al final
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
