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

    private static Logger logger = Logger.getLogger(DrivingLicenceCategories.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = (Connection) getServletContext().getAttribute("DBConnection");

        /*
         * Return selected item for editing;
         */
        AutoManufacturer itemAMtoEdit = null; //if in adding/editing item occurs some error <<< used in next section
        String itemIdToEdit = request.getParameter("itemIDtoEdit");
        String errorItemIdToEdit = null;
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
                errorItemIdToEdit="Getting item for edit failed. NumberFormatException.";
            }
        }
        // Return selected item for editing;

        /*
         * Deleting selected driver item itemIDtoDelete
         */
        String itemIDtoDelete = request.getParameter("itemIDtoDelete");
        String DeleteSuccessful = null;
        String DeleteFailed = null;
        StringBuffer deleteFailedBuffer = new StringBuffer(256);
        if(itemIDtoDelete!=null){
            try {
                int itemIDtoDeleteInt = Integer.parseInt(itemIDtoDelete);
                PreparedStatement ps40 = null; // delete truck if no useges found
                PreparedStatement ps41 = null; // get list of usages
                ResultSet rs41 = null;
                try{
                    ps41 = con.prepareStatement(
                        "select auto.id from auto, auto_manufacturer where auto.manufacturer=auto_manufacturer.id " +
                                    "and auto_manufacturer.id=?;");
                    ps41.setInt(1, itemIDtoDeleteInt);
                    rs41 = ps41.executeQuery();
                    int count = 0;
                    if(rs41!=null){
                        deleteFailedBuffer.append("Deleting failed! This auto manufacturer are used by automobiles with next IDs: ");
                        while(rs41.next()){
                            if(count>0){deleteFailedBuffer.append(", ");}
                            deleteFailedBuffer.append(rs41.getInt("auto.id"));
                            count++;
                        }
                        deleteFailedBuffer.append("; If you like to remove this auto manufacturer, you need remove " +
                                " automobiles first, and then remove it.");
                    }
                    if(deleteFailedBuffer.length()>3 && count>0){
                        DeleteFailed = deleteFailedBuffer.toString();
                    }
                    if(DeleteFailed==null){
                        ps40 = con.prepareStatement("DELETE from auto_manufacturer where id=?");
                        ps40.setInt(1, itemIDtoDeleteInt);
                        ps40.execute();
                        DeleteSuccessful = "Deleting successful!";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        if(rs41!=null){rs41.close();}
                        if(ps41!=null){ps41.close();}
                        if(ps40!=null){ps40.close();}
                    } catch (SQLException e) {
                        logger.error("SQLException in closing PreparedStatement or ResultSet");
                    }
                }
            }catch(NumberFormatException ex){
                logger.info("Oooops! NumberFormatException in GET request parameter itemIDtoDelete: " + itemIDtoDelete);
                errorItemIdToEdit="Getting item for edit failed. NumberFormatException.";
            }
        }
        // Deleting driver item

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
                    if(manufacturer_id == -1){
                        Boolean cancelAdding = false; // if duplication found - cancel adding.
                        ps3 = con.prepareStatement("select id, manufacturer_name from auto_manufacturer where manufacturer_name=? limit 1");
                        ps3.setString(1, manufacturerNameUC);
                        rs3 = ps3.executeQuery();
                        if(rs3!=null && rs3.next()){
                            if(manufacturerNameUC.equals(rs3.getString("manufacturer_name"))){
                                errorMsgBuffer.append("Manufacturer with name: ");
                                errorMsgBuffer.append(manufacturer_name);
                                errorMsgBuffer.append(", already exists with ID: ");
                                errorMsgBuffer.append(rs3.getInt("id"));
                                errorMsgBuffer.append(". See in table by ID.");
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
                            ps2.setString(1, manufacturerNameUC);
                            ps2.setString(2, manufacturer_description);
                            ps2.execute();
                            AddUpdSuccessful = "Successful!";
                        }
                    }else{
                        Boolean cancelAdding = false; // if duplication found - cancel adding.
                        ps3 = con.prepareStatement("select id, manufacturer_name from auto_manufacturer where manufacturer_name=?  and id!=? limit 1");
                        ps3.setString(1, manufacturerNameUC);
                        ps3.setInt(2, manufacturer_id);
                        rs3 = ps3.executeQuery();
                        if(rs3!=null && rs3.next()){
                            if(manufacturerNameUC.equals(rs3.getString("manufacturer_name"))){
                                errorMsgBuffer.append("Manufacturer with the same name: ");
                                errorMsgBuffer.append(manufacturer_name);
                                errorMsgBuffer.append(", already exists with ID: ");
                                errorMsgBuffer.append(rs3.getInt("id"));
                                errorMsgBuffer.append(". See in table by ID.");
                                errorMsgBuffer.append("<br>You can't create the same manufacturer twice. Please correct data.");
                                itemAMtoEdit = new AutoManufacturer(
                                        Integer.parseInt(request.getParameter("manufacturer_id")),
                                        manufacturer_name,
                                        manufacturer_description
                                );
                                cancelAdding=true;
                            }
                        }
                        if(!cancelAdding){
                            ps2 = con.prepareStatement("UPDATE auto_manufacturer SET manufacturer_name=?, description=? WHERE id=?");
                            //UPDATE auto_manufacturer SET manufacturer_name='?', description='?' WHERE id=?;
                            ps2.setString(1, manufacturerNameUC);
                            ps2.setString(2, manufacturer_description);
                            ps2.setInt(3, manufacturer_id);
                            ps2.execute();
                            AddUpdSuccessful = "Successful!";
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        if(rs3!=null){rs3.close();}
                        if(ps3!=null){ps3.close();}
                        if(ps2!=null){ps2.close();}
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

        request.getSession().removeAttribute("listAM");
        request.getSession().removeAttribute("listDLC");
        request.getSession().removeAttribute("listTrucks");
        request.getSession().removeAttribute("driversMap");
        request.getSession().removeAttribute("itemAMtoEdit");
        request.getSession().removeAttribute("itemTruckToEdit");
        request.getSession().removeAttribute("AddUpdSuccessful");
        request.getSession().removeAttribute("AddUpdFailed");
        request.getSession().removeAttribute("DeleteSuccessful");
        request.getSession().removeAttribute("DeleteFailed");

        request.getSession().removeAttribute("itemDriverToEdit");
        request.getSession().removeAttribute("dlcChecked");
        request.getSession().removeAttribute("listPossibleTrucks");
        request.getSession().removeAttribute("selectedTrucksSet");
        request.getSession().removeAttribute("itemDriverForTrucksListEditing");

        if(errorItemIdToEdit!=null){
            errorMsgBuffer.append(errorItemIdToEdit);
        }

        if(itemAMtoEdit!=null){
            request.getSession().setAttribute("itemAMtoEdit", itemAMtoEdit);
        }
        if(AddUpdSuccessful!=null){
            request.getSession().setAttribute("AddUpdSuccessful", AddUpdSuccessful);
        }
        if(errorMsgBuffer.length() > 3){
            request.getSession().setAttribute("AddUpdFailed", errorMsgBuffer.toString());
        }
        if(DeleteSuccessful!=null){
            request.getSession().setAttribute("DeleteSuccessful", DeleteSuccessful);
        }
        if(DeleteFailed!=null){
            request.getSession().setAttribute("DeleteFailed", DeleteFailed);
        }
        request.getSession().setAttribute("listAM", listAM);
        request.getRequestDispatcher("automanufacturers.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
