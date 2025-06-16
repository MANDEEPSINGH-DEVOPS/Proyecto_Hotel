package view;

import service.HabitacionsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/habitacions")
public class HabitacionsServlet extends HttpServlet {

    private HabitacionsService habitacionsService;

    public HabitacionsServlet() {
        this.habitacionsService = new HabitacionsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        habitacionsService.GetHabitacions(request, response, this);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        habitacionsService.PostHabitacion(request, response, this);
    }
}
