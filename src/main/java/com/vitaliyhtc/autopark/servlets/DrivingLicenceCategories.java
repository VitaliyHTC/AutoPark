package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.objects.DriverLicenceCategory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "DrivingLicenceCategories")
public class DrivingLicenceCategories extends HttpServlet {

    private static Logger logger = Logger.getLogger(DrivingLicenceCategories.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = (Connection) getServletContext().getAttribute("DBConnection");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DriverLicenceCategory> listDLC = new ArrayList<DriverLicenceCategory>();
        try{
            ps = con.prepareStatement("select * from driving_licence_categories order by id");
            // id, licence_category, category_description
            rs = ps.executeQuery();
            if(rs != null){
                while(rs.next()){
                    DriverLicenceCategory driverLicenceCategory = new DriverLicenceCategory(
                            rs.getInt("id"),
                            rs.getString("licence_category"),
                            rs.getString("category_description")
                    );
                    listDLC.add(driverLicenceCategory);
                }
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


        request.getSession().removeAttribute("listAM");
        request.getSession().removeAttribute("listDLC");
        request.getSession().removeAttribute("listTrucks");
        request.getSession().removeAttribute("driversMap");
        request.getSession().removeAttribute("itemAMtoEdit");
        request.getSession().removeAttribute("itemTruckToEdit");
        request.getSession().removeAttribute("AddUpdSuccessful");
        request.getSession().removeAttribute("AddUpdFailed");

        request.getSession().removeAttribute("itemDriverToEdit");
        request.getSession().removeAttribute("dlcChecked");
        request.getSession().removeAttribute("listPossibleTrucks");
        request.getSession().removeAttribute("selectedTrucksSet");
        request.getSession().removeAttribute("itemDriverForTrucksListEditing");


        request.getSession().setAttribute("listDLC", listDLC);
        request.getRequestDispatcher("drivinglicencecategories.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
