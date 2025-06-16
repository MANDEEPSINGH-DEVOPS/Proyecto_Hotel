package view;

import service.Reserva_HabitacionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/reservaHabitacion")
public class Reserva_HabitacionServlet extends HttpServlet {
    private Reserva_HabitacionService reserva_habitacionService;

    public Reserva_HabitacionServlet() {
        this.reserva_habitacionService = new Reserva_HabitacionService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.reserva_habitacionService.GetReserva_Habitacion(request,response,this);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.reserva_habitacionService.PostReserva_Habitacion(request,response,this);
    }

}
