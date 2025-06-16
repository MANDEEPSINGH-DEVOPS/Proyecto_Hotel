package view;

import service.UserService;
import exceptions.ServiceException;
import exceptions.DAOException;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class UserServlets extends HttpServlet {
    private UserService userService;

    public UserServlets() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if("logout".equals(action)){
            HttpSession session = request.getSession(false);
            if(session!=null){
                session.invalidate();
            }
        }else {
            try {
                this.userService.GetLogin(request, response, this);
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            this.userService.PostLogin(request, response, this);
        } catch (ServiceException e) {
            // Manejar ServiceException
            request.setAttribute("errorMessage", e.getMessage());
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
