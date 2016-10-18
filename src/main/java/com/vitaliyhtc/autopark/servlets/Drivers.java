package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.objects.Driver;
import com.vitaliyhtc.autopark.objects.Driver_Category;
import com.vitaliyhtc.autopark.objects.Driver_Truck;
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
import java.util.HashMap;

@WebServlet(name = "Drivers")
public class Drivers extends HttpServlet {

    static Logger logger = Logger.getLogger(DrivingLicenceCategories.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");

        /*
         * Return selected item for editing;
         */

        //Return selected item for editing;

        /*
         * 1. Add driver info & select drivingLicenceCategories
         * 2. Edit driver list of drivingLicenceCategories
         * 3. Edit driver list of trucks
         * Hmm. Interesting... How to do better?
         */

        /*
         * Generate HashMap of drivers for our jsp :)
         */
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Driver> driversMap = new HashMap<Integer, Driver>();
        try{
            ps = con.prepareStatement("select * from drivers");
            rs = ps.executeQuery();
            if(rs!=null){
                while(rs.next()){
                    Driver driver = new Driver(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("phone1"),
                            rs.getString("phone2"),
                            rs.getString("phone_relatives"),
                            rs.getString("email"),
                            new HashMap<Integer, Driver_Category>(),
                            new HashMap<Integer, Driver_Truck>()
                    );
                    driversMap.put(rs.getInt("id"), driver);
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
        // Generate maps of drivingLicenceCategories and trucks for driver
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try{
            ps2 = con.prepareStatement(
                    "select drivers.id, driving_licence_categories.id, driving_licence_categories.licence_category " +
                    "from drivers, driver_licence, driving_licence_categories " +
                    "where drivers.id=driver_licence.driver_id and driver_licence.licence_id=driving_licence_categories.id");
            rs2 = ps2.executeQuery();
            if(rs2!=null){
                while(rs2.next()){
                    Driver_Category driverCategory = new Driver_Category(
                            rs2.getInt("driving_licence_categories.id"),
                            rs2.getString("driving_licence_categories.licence_category")
                    );
                    driversMap.get(rs2.getInt("drivers.id")).getDriverCategoriesMap().put(rs2.getInt("driving_licence_categories.id"),driverCategory);
                }
            }

            ps3 = con.prepareStatement(
                    "select drivers.id, auto.id, auto_manufacturer.manufacturer_name, auto.model, auto.license_plate_number, auto.driving_licence_category " +
                    "from drivers, driver_auto, auto, auto_manufacturer " +
                    "where drivers.id=driver_auto.driver_id and driver_auto.auto_id=auto.id and auto.manufacturer=auto_manufacturer.id");
            rs3 = ps3.executeQuery();
            if(rs3!=null){
                while(rs3.next()){
                    Driver_Truck driverTruck = new Driver_Truck(
                            rs3.getInt("auto.id"),
                            rs3.getString("auto_manufacturer.manufacturer_name"),
                            rs3.getString("auto.model"),
                            rs3.getString("auto.license_plate_number"),
                            rs3.getInt("auto.driving_licence_category")
                    );
                    driversMap.get(rs3.getInt("drivers.id")).getDriverTrucksMap().put(rs3.getInt("auto.id"),driverTruck);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }finally{
            try {
                rs2.close();
                ps2.close();
                rs3.close();
                ps3.close();
            } catch (SQLException e) {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }

        // Generate HashMap of drivers for our jsp :)

        /*
         * Set attributes and follow to our jsp page (:
         */
        request.getSession().removeAttribute("listAM");
        request.getSession().removeAttribute("listDLC");
        request.getSession().removeAttribute("listTrucks");
        request.getSession().removeAttribute("driversMap");
        request.getSession().removeAttribute("itemAMtoEdit");
        request.getSession().removeAttribute("itemTruckToEdit");
        request.getSession().removeAttribute("AddUpdSuccessful");
        request.getSession().removeAttribute("AddUpdFailed");



        request.getSession().setAttribute("driversMap", driversMap);
        request.getRequestDispatcher("drivers.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
