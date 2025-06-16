package view;

import exceptions.DAOException;
import exceptions.ServiceException;
import service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin")
public class AdminServlets extends HttpServlet{
    private AdminService adminService;

    public AdminServlets(){
        this.adminService = new AdminService();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            this.adminService.GetAdmin(request,response,this);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            this.adminService.PostAdmin(request,response,this);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }


}
