package com.vitaliyhtc.autopark.usersmanagement;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "Register", urlPatterns = { "/Register" })
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(RegisterServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("username");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        StringBuffer errorMsgBuffer = new StringBuffer( 1024 );
        if(email == null || email.equals("")){
            errorMsgBuffer.append("Email ID can't be null or empty.<br>");
        }
        if(password == null || password.equals("") || password.length()<8){
            errorMsgBuffer.append("Password can't be null or empty or less than 8 symbols.<br>");
        }
        if(username == null || username.equals("")){
            errorMsgBuffer.append("Username can't be null or empty.<br>");
        }
        if(firstname == null || firstname.equals("")){
            errorMsgBuffer.append("Firstname can't be null or empty.<br>");
        }
        if(lastname == null || lastname.equals("")){
            errorMsgBuffer.append("Lastname can't be null or empty.<br>");
        }

        //Connection for our PreparedStatement s
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");

        //verify that new user is unique
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try{
            ps1 = con.prepareStatement("select username from users where username=? limit 1");
            ps1.setString(1, username);
            ps2 = con.prepareStatement("select email from users where email=? limit 1");
            ps2.setString(1, email);
            rs1 = ps1.executeQuery();
            rs2 = ps2.executeQuery();

            if(rs1 != null && rs1.next()){
                if(username.equals(rs1.getString("username"))){
                    errorMsgBuffer.append("User with name: "+username+" already exists! Please, use other name.<br>");
                    logger.info("User try to register with existing username: "+username+";");
                }
            }
            if(rs2 != null && rs2.next()){
                if(email.equals(rs2.getString("email"))){
                    errorMsgBuffer.append("User with email: "+email+" already exists! Please, use other email.<br>");
                    logger.info("User try to register with existing email: "+email+";");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }finally{
            try {
                rs1.close();
                ps1.close();
                rs2.close();
                ps2.close();
            } catch (SQLException e) {
                logger.error("SQLException in closing PreparedStatement");
            }
        }
        //end verify


        if(errorMsgBuffer.length() > 3){
            request.getSession().setAttribute("errors", "<font color=red>"+errorMsgBuffer.toString()+"</font><br>");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }else{

            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement("insert into Users(username, email, firstname, lastname, password) values (?,?,?,?,?)");
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, firstname);
                ps.setString(4, lastname);
                ps.setString(5, password);

                ps.execute();

                logger.info("User registered with email="+email);

                //forward to login page to login
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                PrintWriter out= response.getWriter();
                out.println("<font color=green>Registration successful, please login below.</font>");
                rd.include(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("SQLException in closing PreparedStatement");
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
