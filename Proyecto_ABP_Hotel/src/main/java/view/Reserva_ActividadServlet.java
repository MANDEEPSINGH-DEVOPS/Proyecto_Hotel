package view;

import service.Reserva_ActividadService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reservaActividad")
public class Reserva_ActividadServlet extends HttpServlet {

    private Reserva_ActividadService reservaActividadService;

    public Reserva_ActividadServlet() {
        this.reservaActividadService = new Reserva_ActividadService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.reservaActividadService.GetReservas(request, response, this);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.reservaActividadService.PostReservas(request, response, this);

    }
}
