package com.vitaliyhtc.autopark.usersmanagement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vitaliyhtc.autopark.util.User;
import org.apache.log4j.Logger;

@WebServlet(name = "Login", urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        StringBuffer errorMsgBuffer = new StringBuffer( 1024 );
        if(email == null || email.equals("")){
            errorMsgBuffer.append("User Email can't be null or empty.<br>");
        }
        if(password == null || password.equals("")){
            errorMsgBuffer.append("Password can't be null or empty.<br>");
        }

        if(errorMsgBuffer.length() > 3){
            request.getSession().setAttribute("errors", "<font color=red>"+errorMsgBuffer.toString()+"</font><br>");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }else{
            Connection con = (Connection) getServletContext().getAttribute("DBConnection");
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement("select id, username, email, firstname, lastname from users where email=? and password=? limit 1");
                ps.setString(1, email);
                ps.setString(2, password);
                rs = ps.executeQuery();

                if(rs != null && rs.next()){
                    User user = new User(rs.getString("username"), rs.getString("email"), rs.getInt("id"),
                            rs.getString("firstname"), rs.getString("lastname") );
                    logger.info("User found with details="+user);
                    HttpSession session = request.getSession();
                    session.setAttribute("User", user);
                    response.sendRedirect("home");
                }else{
                    logger.error("User not found with email="+email+"; or password.");
                    request.getSession().setAttribute("errors", "<font color=red>No user found with given email id " +
                            "or password. Please, use correct data or register first.</font><br>");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    logger.error("SQLException in closing PreparedStatement or ResultSet");
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
