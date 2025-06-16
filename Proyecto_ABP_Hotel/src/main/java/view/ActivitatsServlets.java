package view;


import exceptions.ServiceException;
import service.ActivitatsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activitats")
public class ActivitatsServlets extends HttpServlet{

    private ActivitatsService activitatsService;

    public ActivitatsServlets(){
        this.activitatsService = new ActivitatsService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            this.activitatsService.GetActivitat(request,response,this);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            this.activitatsService.PostActivitat(request,response,this);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

}
