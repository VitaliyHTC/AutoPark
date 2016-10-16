package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.util.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@javax.servlet.annotation.WebServlet(name = "Index")
public class Index extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType( "text/html" );
        StringBuffer buffer = new StringBuffer( 1024 );


        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("User");

        buffer.append("<!DOCTYPE html>");
        buffer.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        buffer.append("<meta charset=\"UTF-8\"><title>Login Page</title></head>");
        buffer.append("<body>");
        buffer.append("<h3>Hi "+user.getUsername()+".</h3>");
        buffer.append("<b>Your Email: "+user.getEmail()+"</b><br>");
        buffer.append("<b>Your First Name: "+user.getFirstname()+"</b><br>");
        buffer.append("<b>Your Last Name: "+user.getLastname()+"</b><br>");
        buffer.append("<form action=\"Logout\" method=\"post\">\n" +
                "    <input type=\"submit\" value=\"Logout\" >\n" +
                "</form>");
        buffer.append( "</body></html>" );
        PrintWriter out = response.getWriter();
        out.println( buffer.toString() );
        out.close();
    }
}
