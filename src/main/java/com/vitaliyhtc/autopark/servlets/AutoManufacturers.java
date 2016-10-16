package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.objects.AutoManufacturer;
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

@WebServlet(name = "AutoManufacturers")
public class AutoManufacturers extends HttpServlet {

    static Logger logger = Logger.getLogger(DrivingLicenceCategories.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = (Connection) getServletContext().getAttribute("DBConnection");


        /*
         * Return selected item for editing;
         */
        AutoManufacturer itemAMtoEdit=null; //if in adding/editing item occurs some error <<< used in next section too
        String itemIdToEdit = request.getParameter("itemIDtoEdit");
        if(itemIdToEdit!=null){
            try {
                int itemIDtoEditInt = Integer.parseInt(itemIdToEdit);
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                try{
                    ps1 = con.prepareStatement("select * from auto_manufacturer where id=? limit 1");
                    // id, manufacturer_name, description
                    ps1.setInt(1, itemIDtoEditInt);
                    rs1 = ps1.executeQuery();
                    if(rs1!=null && rs1.next()){
                            itemAMtoEdit = new AutoManufacturer(
                                    rs1.getInt("id"),
                                    rs1.getString("manufacturer_name"),
                                    rs1.getString("description")
                            );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        rs1.close();
                        ps1.close();
                    } catch (SQLException e) {
                        logger.error("SQLException in closing PreparedStatement or ResultSet");
                    }
                }
            }catch(NumberFormatException ex){
                logger.info("Oooops! NumberFormatException in GET request parameter itemIDtoEdit: " + itemIdToEdit);
            }
        }
        // Return selected item for editing;

        /*
         * Adding or updating item to auto_manufacturer table
         */
        String AddUpdSuccessful = null;
        StringBuffer errorMsgBuffer = new StringBuffer( 1024 );
        if(request.getParameter("manufacturer_id")!=null){
            //validating parameters
            String manufacturer_name = request.getParameter("manufacturer_name");
            String manufacturer_description = request.getParameter("manufacturer_description");
            if(manufacturer_name == null || manufacturer_name.equals("") || manufacturer_name.length()>64){
                errorMsgBuffer.append("Name can't be null or empty or more than 64 symbols.<br>");
            }
            if(manufacturer_description == null || manufacturer_description.equals("") ||
                    manufacturer_description.length()>255){
                errorMsgBuffer.append("Description can't be null or empty or more than 255 symbols.<br>");
            }
            if(errorMsgBuffer.length() > 3){
                itemAMtoEdit = new AutoManufacturer(
                        Integer.parseInt(request.getParameter("manufacturer_id")),
                        manufacturer_name,
                        manufacturer_description
                );
            }else{
                PreparedStatement ps2 = null;
                PreparedStatement ps3 = null;
                ResultSet rs3 = null;
                try{
                    int manufacturer_id = Integer.parseInt(request.getParameter("manufacturer_id"));
                    String manufacturerNameUC = manufacturer_name.toUpperCase();
                    int one = 1;
                    int two = 2;
                    int three = 3;
                    if(manufacturer_id == -1){
                        Boolean cancelAdding = false; // if duplication found - cancel adding.
                        ps3 = con.prepareStatement("select id, manufacturer_name from auto_manufacturer where manufacturer_name=? limit 1");
                        ps3.setString(1, manufacturerNameUC);
                        rs3 = ps3.executeQuery();
                        if(rs3!=null && rs3.next()){
                            if(manufacturerNameUC.equals(rs3.getString("manufacturer_name"))){
                                errorMsgBuffer.append("Manufacturer with name: " + manufacturer_name +
                                        ", already exists with ID: " + rs3.getInt("id") + ". See in table by ID.");
                                errorMsgBuffer.append("<br>You can edit existing manufacturers or add other.");
                                itemAMtoEdit = new AutoManufacturer(
                                        Integer.parseInt(request.getParameter("manufacturer_id")),
                                        manufacturer_name,
                                        manufacturer_description
                                );
                                cancelAdding=true;
                            }
                        }
                        if(!cancelAdding){
                            ps2 = con.prepareStatement("INSERT INTO auto_manufacturer(id, manufacturer_name, description) VALUES(null, ?, ?)");
                            // id, manufacturer_name, description
                            //INSERT INTO auto_manufacturer(id, manufacturer_name, description) VALUES(null, 'SCANIA', 'Best trucks & good sounding engines!');
                            ps2.setString(one, manufacturerNameUC);
                            ps2.setString(two, manufacturer_description);
                            ps2.execute();
                            AddUpdSuccessful = "Successful!";
                        }
                    }else{
                        ps2 = con.prepareStatement("UPDATE auto_manufacturer SET manufacturer_name=?, description=? WHERE id=?");
                        //UPDATE auto_manufacturer SET manufacturer_name='?', description='?' WHERE id=?;
                        ps2.setString(one, manufacturerNameUC);
                        ps2.setString(two, manufacturer_description);
                        ps2.setInt(three, manufacturer_id);
                        ps2.execute();
                        AddUpdSuccessful = "Successful!";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        if(rs3!=null){
                            rs3.close();
                        }
                        if(ps3!=null){
                            ps3.close();;
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    } catch (SQLException e) {
                        logger.error("SQLException in closing PreparedStatement or ResultSet");
                    }
                }
            }
            // manufacturer_id
        }
        // Adding or updating item to auto_manufacturer table

        /*
         * Generate list of manufacturers for our jsp :)
         */
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AutoManufacturer> listAM = new ArrayList<AutoManufacturer>();
        try{
            ps = con.prepareStatement("select * from auto_manufacturer");
            // id, manufacturer_name, description
            rs = ps.executeQuery();
            if(rs!=null){
                while(rs.next()){
                    AutoManufacturer autoManufacturer = new AutoManufacturer(
                            rs.getInt("id"),
                            rs.getString("manufacturer_name"),
                            rs.getString("description")
                    );
                    listAM.add(autoManufacturer);
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
        // Generate list of manufacturers for our jsp :)

        /*
         * Set attributes and follow to our jsp page (:
         */

        request.getSession().setAttribute("listAM", listAM);
        request.getSession().removeAttribute("itemAMtoEdit");
        if(itemAMtoEdit!=null){
            request.getSession().setAttribute("itemAMtoEdit", itemAMtoEdit);
        }
        request.getSession().removeAttribute("AddUpdSuccessful");
        if(AddUpdSuccessful!=null){
            request.getSession().setAttribute("AddUpdSuccessful", AddUpdSuccessful);
        }
        request.getSession().removeAttribute("AddUpdFailed");
        if(errorMsgBuffer.length() > 3){
            request.getSession().setAttribute("AddUpdFailed", errorMsgBuffer.toString());
        }
        request.getRequestDispatcher("automanufacturers.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
