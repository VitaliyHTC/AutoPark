package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.objects.AutoManufacturer;
import com.vitaliyhtc.autopark.objects.Truck;
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

@WebServlet(name = "AutoMobiles")
public class AutoMobiles extends HttpServlet {

    static Logger logger = Logger.getLogger(DrivingLicenceCategories.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = (Connection) getServletContext().getAttribute("DBConnection");



        /*
         * Generate list of trucks/cars for our jsp :)
         */
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Truck> listTrucks = new ArrayList<Truck>();
        try{
            ps = con.prepareStatement("select auto.*, auto_manufacturer.manufacturer_name, driving_licence_categories.licence_category from auto, auto_manufacturer, driving_licence_categories where auto.manufacturer = auto_manufacturer.id and auto.driving_licence_category = driving_licence_categories.id");
            // dee in database.sql
            rs = ps.executeQuery();
            if(rs!=null){
                while(rs.next()){
                    Truck autoManufacturer = new Truck(
                            rs.getInt("id"),
                            rs.getInt("manufacturer"),
                            rs.getString("model"),
                            rs.getString("vin_number"),
                            rs.getInt("driving_licence_category"),
                            rs.getString("engine_model"),
                            rs.getInt("engine_power"),
                            rs.getInt("engine_eco"),
                            rs.getString("gearbox"),
                            rs.getString("chassis_type"),
                            rs.getInt("max_weight"),
                            rs.getInt("equipped_weight"),
                            rs.getString("license_plate_number"),
                            rs.getString("description"),
                            rs.getString("manufacturer_name"),
                            rs.getString("licence_category")
                    );
                    listTrucks.add(autoManufacturer);
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
        // Generate list of trucks/cars for our jsp :)

        /*
         * Set attributes and follow to our jsp page (:
         */

        request.getSession().removeAttribute("listAM");
        request.getSession().removeAttribute("itemAMtoEdit");
        request.getSession().removeAttribute("AddUpdSuccessful");
        request.getSession().removeAttribute("AddUpdFailed");

        request.getSession().setAttribute("listTrucks", listTrucks);
        request.getRequestDispatcher("automobiles.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
