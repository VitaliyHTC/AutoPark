package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.objects.Driver;
import com.vitaliyhtc.autopark.objects.DriverLicenceCategory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@WebServlet(name = "Drivers")
public class Drivers extends HttpServlet {

    private static Logger logger = Logger.getLogger(DrivingLicenceCategories.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");

        /*
         * Return selected driver item for editing; itemDriverToEdit
         */
        Driver itemDriverToEdit = null; // if in adding/editing item occurs some error <<< used in Add/Edit section
        ArrayList<Boolean> dlcChecked=null;
        String itemIdToEdit = request.getParameter("itemIDtoEdit");
        String errorItemIdToEdit = null; // also used in other sections
        if(itemIdToEdit!=null){
            try{
                int itemIDtoEditInt = Integer.parseInt(itemIdToEdit);
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                try{
                    ps1 = con.prepareStatement("select * from drivers where id=?");
                    ps1.setInt(1, itemIDtoEditInt);
                    rs1 = ps1.executeQuery();
                    if(rs1!=null && rs1.next()){
                            itemDriverToEdit = new Driver(
                                    rs1.getInt("id"),
                                    rs1.getString("username"),
                                    rs1.getString("firstname"),
                                    rs1.getString("lastname"),
                                    rs1.getString("phone1"),
                                    rs1.getString("phone2"),
                                    rs1.getString("phone_relatives"),
                                    rs1.getString("email"),
                                    new HashMap<Integer, Driver_Category>(),
                                    new HashMap<Integer, Driver_Truck>()
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
        // Generate maps of drivingLicenceCategories and trucks for driver
        if (itemDriverToEdit != null) {
            dlcChecked = new ArrayList<Boolean>();
            for (int i = 0; i < 13; i++) { dlcChecked.add(false); }
            PreparedStatement ps4 = null;
            ResultSet rs4 = null;
            try{
                ps4 = con.prepareStatement(
                    "select drivers.id, driving_licence_categories.id, driving_licence_categories.licence_category " +
                        "from drivers, driver_licence, driving_licence_categories " +
                        "where drivers.id=driver_licence.driver_id and driver_licence.licence_id=driving_licence_categories.id " +
                                "and drivers.id=?");
                ps4.setInt(1, itemDriverToEdit.getId());
                rs4 = ps4.executeQuery();
                if(rs4!=null){
                    while(rs4.next()){
                        Driver_Category driverCategory = new Driver_Category(
                                rs4.getInt("driving_licence_categories.id"),
                                rs4.getString("driving_licence_categories.licence_category")
                        );
                        dlcChecked.set(rs4.getInt("driving_licence_categories.id")-1, true);
                        itemDriverToEdit.getDriverCategoriesMap().put(rs4.getInt("driving_licence_categories.id"),driverCategory);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    rs4.close();
                    ps4.close();
                } catch (SQLException e) {
                    logger.error("SQLException in closing PreparedStatement or ResultSet");
                }
            }
        }
        //Return selected item for editing;

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
                PreparedStatement ps40 = null; // delete driver if no selected trucks
                PreparedStatement ps41 = null; // get list of trucks selected for this driver
                PreparedStatement ps42 = null; // delete driver DLCs
                ResultSet rs41 = null;
                try{
                    ps41 = con.prepareStatement(
                        "select auto.id from drivers, driver_auto, auto where drivers.id=driver_auto.driver_id and " +
                                "driver_auto.auto_id=auto.id and drivers.id=?");
                    ps41.setInt(1, itemIDtoDeleteInt);
                    rs41 = ps41.executeQuery();
                    int count = 0;
                    if(rs41!=null){
                        deleteFailedBuffer.append("Deleting failed! Automobiles with next IDs are used by driver: ");
                        while(rs41.next()){
                            if(count>0){deleteFailedBuffer.append(", ");}
                            deleteFailedBuffer.append(rs41.getInt("auto.id"));
                            count++;
                        }
                        deleteFailedBuffer.append("; Cancel selection of automobiles first, and then remove driver.");
                    }
                    if(deleteFailedBuffer.length()>3 && count>0){
                        DeleteFailed = deleteFailedBuffer.toString();
                    }
                    if(DeleteFailed==null){
                        ps42 = con.prepareStatement("DELETE from driver_licence where driver_id=?");
                        ps42.setInt(1, itemIDtoDeleteInt);
                        ps42.execute();
                        ps40 = con.prepareStatement("DELETE from drivers where id=?");
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
                        if(ps42!=null){ps42.close();}
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
         * Return selected driver with items for truck list editing
         */
        Driver itemDriverForTrucksListEditing = null;
        ArrayList<Driver_Truck> listPossibleTrucks = null;
        HashSet<Integer> selectedTrucksSet = null;
        String itemIDtoEditTrucks = request.getParameter("itemIDtoEditTrucks");
        if(itemIDtoEditTrucks!=null){
            try{
                int itemIDtoEditTrucksInt = Integer.parseInt(itemIDtoEditTrucks);
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                PreparedStatement ps2 = null;
                ResultSet rs2 = null;
                PreparedStatement ps3 = null;
                ResultSet rs3 = null;
                try{
                    ps1 = con.prepareStatement("select id, username, firstname, lastname from drivers where id=?");
                    ps1.setInt(1, itemIDtoEditTrucksInt);
                    rs1 = ps1.executeQuery();
                    if(rs1!=null && rs1.next()){
                        itemDriverForTrucksListEditing = new Driver(
                                rs1.getInt("id"),
                                rs1.getString("username"),
                                rs1.getString("firstname"),
                                rs1.getString("lastname"),
                                "", "", "", "", null, null
                        );
                    }

                    listPossibleTrucks = new ArrayList<Driver_Truck>();
                    ps2 = con.prepareStatement(
                    "select auto.id, auto_manufacturer.manufacturer_name, auto.model, auto.license_plate_number " +
                        "from drivers, driver_licence, auto, auto_manufacturer " +
                        "where drivers.id=driver_licence.driver_id and auto.driving_licence_category = driver_licence.licence_id " +
                            "and auto.manufacturer=auto_manufacturer.id and drivers.id=?");
                    ps2.setInt(1, itemIDtoEditTrucksInt);
                    rs2 = ps2.executeQuery();
                    if(rs2!=null){
                        while(rs2.next()){
                            Driver_Truck driverTruck = new Driver_Truck(
                                    rs2.getInt("auto.id"),
                                    rs2.getString("auto_manufacturer.manufacturer_name"),
                                    rs2.getString("auto.model"),
                                    rs2.getString("auto.license_plate_number"),
                                    0
                            );
                            listPossibleTrucks.add(driverTruck);
                        }
                    }

                    selectedTrucksSet = new HashSet<Integer>();
                    ps3 = con.prepareStatement(
                            "select auto.id from drivers, driver_auto, auto " +
                                    "where drivers.id=driver_auto.driver_id and driver_auto.auto_id=auto.id " +
                                    "and drivers.id=?");
                    ps3.setInt(1, itemIDtoEditTrucksInt);
                    rs3 = ps3.executeQuery();
                    if(rs3!=null){
                        while(rs3.next()){
                            selectedTrucksSet.add(rs3.getInt("auto.id"));
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        if(rs1!=null){rs1.close();}
                        if(ps1!=null){ps1.close();}
                        if(rs2!=null){rs2.close();}
                        if(ps2!=null){ps2.close();}
                        if(rs3!=null){rs3.close();}
                        if(ps3!=null){ps3.close();}
                    } catch (SQLException e) {
                        logger.error("SQLException in closing PreparedStatement or ResultSet");
                    }
                }
            }catch(NumberFormatException ex){
                logger.info("Oooops! NumberFormatException in GET request parameter itemIDtoEditTrucks: " + itemIDtoEditTrucks);
                errorItemIdToEdit="Getting item for edit failed. NumberFormatException.";
            }
        }
        // Return selected driver trucks list listPossibleTrucks

        /*
         * Generate listDLC
         */
        PreparedStatement ps11 = null;
        ResultSet rs11 = null;
        ArrayList<DriverLicenceCategory> listDLC = new ArrayList<DriverLicenceCategory>();
        try{
            ps11 = con.prepareStatement("select id, licence_category from driving_licence_categories order by id");
            rs11 = ps11.executeQuery();
            if(rs11!=null){
                while(rs11.next()){
                    DriverLicenceCategory driverLicenceCategory = new DriverLicenceCategory(
                            rs11.getInt("id"), rs11.getString("licence_category"), "");
                    //we don't need DLC description for adding/editing driver
                    listDLC.add(driverLicenceCategory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }finally{
            try {
                rs11.close();
                ps11.close();
            } catch (SQLException e) {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
        // Generate listDLC

        /*
         * Adding new Driver or updating existing driver
         */
        String AddUpdSuccessful = null;
        StringBuffer errorMsgBuffer = new StringBuffer( 1024 );
        if(request.getParameter("driver_id")!=null){
            //validating parameters
            String username = request.getParameter("username");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String phone1 = request.getParameter("phone1");
            String phone2 = request.getParameter("phone2");
            String phoneRelatives = request.getParameter("phoneRelatives");
            String email = request.getParameter("email");
            ArrayList<Boolean> dlcCheckedVerification = new ArrayList<Boolean>();
            for (int i = 0; i < 13; i++) { dlcCheckedVerification.add(false); }
            for (DriverLicenceCategory drivingLicenceCategory : listDLC) {
                String isCheckedDLC = request.getParameter(drivingLicenceCategory.getId()+"_"+drivingLicenceCategory.getCategory());
                if("on".equals(isCheckedDLC)){
                    dlcCheckedVerification.set(drivingLicenceCategory.getId()-1, true);
                }
            }
            if(username == null || username.equals("") || username.length()>64){
                errorMsgBuffer.append("Username can't be null or empty or more than 64 symbols.<br>");
            }
            if(firstname == null || firstname.equals("") || firstname.length()>64){
                errorMsgBuffer.append("First name can't be null or empty or more than 64 symbols.<br>");
            }
            if(lastname == null || lastname.equals("") || lastname.length()>64){
                errorMsgBuffer.append("Last name can't be null or empty or more than 64 symbols.<br>");
            }
            if(phone1 == null || phone1.equals("") || phone1.length()>24){
                errorMsgBuffer.append("Phone1 name can't be null or empty or more than 24 symbols.<br>");
            }
            if(phone2.length()>24){ errorMsgBuffer.append("Phone2 can't be more than 24 symbols.<br>");}
            if(phoneRelatives.length()>24){ errorMsgBuffer.append("PhoneRelatives name can't be more than 24 symbols.<br>"); }
            if(email.length()>255){ errorMsgBuffer.append("Email can't be more than 255 symbols.<br>"); }
            if(dlcCheckedVerification.get(9-1) && !dlcCheckedVerification.get(4 - 1)){
                errorMsgBuffer.append("If driver has category BE, category B also must be selected!<br>");
                dlcCheckedVerification.set(4-1, true);
            }
            if(dlcCheckedVerification.get(10-1) && !dlcCheckedVerification.get(5 - 1)){
                errorMsgBuffer.append("If driver has category C1E, category C1 also must be selected!<br>");
                dlcCheckedVerification.set(5-1, true);
            }
            if(dlcCheckedVerification.get(11-1) && !dlcCheckedVerification.get(6 - 1)){
                errorMsgBuffer.append("If driver has category CE, category C also must be selected!<br>");
                dlcCheckedVerification.set(6-1, true);
            }
            if(errorMsgBuffer.length()>3){ // if errors found - return for editing
                itemDriverToEdit = new Driver(
                        Integer.parseInt(request.getParameter("driver_id")),
                        username, firstname, lastname, phone1, phone2, phoneRelatives, email,
                        new HashMap<Integer, Driver_Category>(),
                        new HashMap<Integer, Driver_Truck>()
                );
                dlcChecked = dlcCheckedVerification;
            }else{ // if no errors - INSERT or UPDATE
                PreparedStatement ps20 = null; // for adding or updating driver.
                PreparedStatement ps21 = null; // verify username for duplicates
                PreparedStatement ps22 = null; // verify firstname+lastname for duplicates
                PreparedStatement ps23 = null; // get newDriverID for adding driver licence categories
                PreparedStatement ps24 = null; // adding driver licence categories
                PreparedStatement ps25 = null; // get list of trucks categories for drivers.id=?
                PreparedStatement ps26 = null; // remove pairs (driver_id, licence_id) for selected driver before \r\n
                PreparedStatement ps27 = null; // adding new pairs (driver_id, licence_id) for selected driver
                ResultSet rs21 = null;
                ResultSet rs22 = null;
                ResultSet rs23 = null;
                ResultSet rs25 = null;

                try{
                    int driver_id = Integer.parseInt(request.getParameter("driver_id"));
                    if(driver_id == -1) { //add new driver
                        Boolean cancelAdding = false; // if duplication found - cancel adding.
                        ps21 = con.prepareStatement("select id, username from drivers where username=?");
                        ps21.setString(1, username);
                        rs21 = ps21.executeQuery();
                        if(rs21!=null && rs21.next()){
                            if(username.equals(rs21.getString("username"))){
                                errorMsgBuffer.append("Driver with username: ");
                                errorMsgBuffer.append(username);
                                errorMsgBuffer.append(" already exists with ID: ");
                                errorMsgBuffer.append(rs21.getInt("id"));
                                errorMsgBuffer.append(". See in table by ID.<br>");
                                cancelAdding=true;
                            }
                        }
                        ps22 = con.prepareStatement("select id, firstname, lastname from drivers where firstname=? and lastname=?");
                        ps22.setString(1, firstname);
                        ps22.setString(2, lastname);
                        rs22 = ps22.executeQuery();
                        if(rs22!=null && rs22.next()){
                            if(firstname.equals(rs22.getString("firstname"))&&lastname.equals(rs22.getString("lastname"))){
                                errorMsgBuffer.append("Driver with name: ");
                                errorMsgBuffer.append(firstname);
                                errorMsgBuffer.append(" ");
                                errorMsgBuffer.append(lastname);
                                errorMsgBuffer.append(" already exists with ID: ");
                                errorMsgBuffer.append(rs22.getInt("id"));
                                errorMsgBuffer.append(". See in table by ID.<br>");
                                cancelAdding=true;
                            }
                        }
                        if(cancelAdding){ // if duplicate found - return for editing
                            itemDriverToEdit = new Driver(
                                    Integer.parseInt(request.getParameter("driver_id")),
                                    username, firstname, lastname, phone1, phone2, phoneRelatives, email,
                                    new HashMap<Integer, Driver_Category>(),
                                    new HashMap<Integer, Driver_Truck>()
                            );
                            dlcChecked = dlcCheckedVerification;
                            errorMsgBuffer.append("You can edit existing drivers or add other.<br>");
                        }
                        if(!cancelAdding){ //INSERT...
                            ps20 = con.prepareStatement("INSERT INTO drivers VALUES(null, ?, ?, ?, ?, ?, ?, ?)");
                            //INSERT INTO drivers VALUES(null, 'IBTruckingGirl', 'Iwona', 'Blecharczyk',
                            // '1-865-522-6166', '', '', 'IwonaBlecharczyk@gmail.com');
                            ps20.setString(1, username);
                            ps20.setString(2, firstname);
                            ps20.setString(3, lastname);
                            ps20.setString(4, phone1);
                            ps20.setString(5, phone2);
                            ps20.setString(6, phoneRelatives);
                            ps20.setString(7, email);
                            ps20.execute();

                            ps23 = con.prepareStatement("select id from drivers where username=? and firstname=?");
                            ps23.setString(1, username);
                            ps23.setString(2, firstname);
                            rs23 = ps23.executeQuery();
                            int newDriverID = 0;
                            if(rs23!=null && rs23.next()){
                                newDriverID = rs23.getInt("id");
                            }
                            StringBuffer licenceCatQuery = new StringBuffer(256);
                            //INSERT INTO driver_licence VALUES(1,4),(1,5),(1,6),(1,10),(1,11)
                            licenceCatQuery.append("INSERT INTO driver_licence VALUES");
                            int addedDLCcount=0;
                            for (int i = 0; i < 13; i++) {
                                if(dlcCheckedVerification.get(i)){
                                    if(addedDLCcount>0){ licenceCatQuery.append(","); }
                                    int categoryID = i+1;
                                    licenceCatQuery.append("(");
                                    licenceCatQuery.append(newDriverID);
                                    licenceCatQuery.append(",");
                                    licenceCatQuery.append(categoryID);
                                    licenceCatQuery.append(")");
                                    addedDLCcount++;
                                }
                            }
                            if(addedDLCcount>0){
                                ps24 = con.prepareStatement(licenceCatQuery.toString());
                                ps24.execute();
                            }
                            AddUpdSuccessful = "Successful";
                        }
                    }else{ //update existing driver
                        Boolean cancelUpdate = false; // if driver not has categories for selected trucks
                            // or duplicate found - return for editing
                        ps21 = con.prepareStatement("select id, username from drivers where username=? and id!=?");
                        ps21.setString(1, username);
                        ps21.setInt(2, driver_id);
                        rs21 = ps21.executeQuery();
                        if(rs21!=null && rs21.next()){
                            if(username.equals(rs21.getString("username"))){
                                errorMsgBuffer.append("Driver with username: ");
                                errorMsgBuffer.append(username);
                                errorMsgBuffer.append(" already exists with ID: ");
                                errorMsgBuffer.append(rs21.getInt("id"));
                                errorMsgBuffer.append(". See in table by ID.<br>");
                                errorMsgBuffer.append("You can't create driver with the same username twice. Please correct data.<br>");
                                cancelUpdate=true;
                            }
                        }
                        ps22 = con.prepareStatement("select id, firstname, lastname from drivers where firstname=? and lastname=? and id!=?");
                        ps22.setString(1, firstname);
                        ps22.setString(2, lastname);
                        ps22.setInt(3, driver_id);
                        rs22 = ps22.executeQuery();
                        if(rs22!=null && rs22.next()){
                            if(firstname.equals(rs22.getString("firstname"))&&lastname.equals(rs22.getString("lastname"))){
                                errorMsgBuffer.append("Driver with name: ");
                                errorMsgBuffer.append(firstname);
                                errorMsgBuffer.append(" ");
                                errorMsgBuffer.append(lastname);
                                errorMsgBuffer.append(" already exists with ID: ");
                                errorMsgBuffer.append(rs22.getInt("id"));
                                errorMsgBuffer.append(". See in table by ID.<br>");
                                errorMsgBuffer.append("You can't create driver with the same firstname and lastname twice. Please correct data.<br>");
                                cancelUpdate=true;
                            }
                        }

                        ps25 = con.prepareStatement(
                            "select distinct auto.driving_licence_category from drivers, driver_auto, auto " +
                                "where drivers.id=driver_auto.driver_id and auto.id=driver_auto.auto_id and drivers.id=?");
                        ps25.setInt(1, driver_id);
                        rs25 = ps25.executeQuery();
                        HashSet<Integer> neededDLC = new HashSet<Integer>();
                        int neededDLCcount = 0;
                        if(rs25!=null){
                            while(rs25.next()){
                                neededDLC.add(rs25.getInt("auto.driving_licence_category"));
                                neededDLCcount++;
                            }
                        }
                        if(neededDLCcount>0){
                            for (Integer neededDLCID : neededDLC) {
                                if(!dlcCheckedVerification.get(neededDLCID-1)){
                                    cancelUpdate=true;
                                    errorMsgBuffer.append("For driver are selected automobiles with categories: ");
                                    errorMsgBuffer.append(listDLC.get(neededDLCID-1).getCategory());
                                    errorMsgBuffer.append(". If you need to remove this category - first remove ");
                                    errorMsgBuffer.append("automobiles with this categoties, and then ");
                                    errorMsgBuffer.append("edit driver categories.<br>");
                                }
                            }
                        }
                        if(cancelUpdate){ // if driver not has categories for selected trucks - return for editing
                            itemDriverToEdit = new Driver(
                                    Integer.parseInt(request.getParameter("driver_id")),
                                    username, firstname, lastname, phone1, phone2, phoneRelatives, email,
                                    new HashMap<Integer, Driver_Category>(),
                                    new HashMap<Integer, Driver_Truck>()
                            );
                            dlcChecked = dlcCheckedVerification;
                        }
                        if(!cancelUpdate){
                            ps20 = con.prepareStatement("UPDATE drivers SET username=?, firstname=?, lastname=?, " +
                                    "phone1=?, phone2=?, phone_relatives=?, email=? WHERE id=?");
                            //id, username, firstname, lastname, phone1, phone2, phone_relatives, email.
                            ps20.setString(1, username);
                            ps20.setString(2, firstname);
                            ps20.setString(3, lastname);
                            ps20.setString(4, phone1);
                            ps20.setString(5, phone2);
                            ps20.setString(6, phoneRelatives);
                            ps20.setString(7, email);
                            ps20.setInt(8, driver_id);
                            ps20.execute();

                            ps26 = con.prepareStatement("DELETE from driver_licence where driver_id=?");
                            ps26.setInt(1, driver_id);
                            ps26.execute();
                            StringBuffer licenceCatQuery = new StringBuffer(256);
                            //INSERT INTO driver_licence VALUES(1,4),(1,5),(1,6),(1,10),(1,11)
                            licenceCatQuery.append("INSERT INTO driver_licence VALUES");
                            int addedDLCcount=0;
                            for (int i = 0; i < 13; i++) {
                                if(dlcCheckedVerification.get(i)){
                                    if(addedDLCcount>0){ licenceCatQuery.append(","); }
                                    int categoryID = i+1;
                                    licenceCatQuery.append("(");
                                    licenceCatQuery.append(driver_id);
                                    licenceCatQuery.append(",");
                                    licenceCatQuery.append(categoryID);
                                    licenceCatQuery.append(")");
                                    addedDLCcount++;
                                }
                            }
                            if(addedDLCcount>0){
                                ps27 = con.prepareStatement(licenceCatQuery.toString());
                                ps27.execute();
                            }
                            AddUpdSuccessful = "Successful";
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        if(rs21!=null){rs21.close();}
                        if(ps21!=null){ps21.close();}
                        if(rs22!=null){rs22.close();}
                        if(ps22!=null){ps22.close();}
                        if(rs23!=null){rs23.close();}
                        if(ps23!=null){ps23.close();}
                        if(ps24!=null){ps24.close();}
                        if(rs25!=null){rs25.close();}
                        if(ps25!=null){ps25.close();}
                        if(ps26!=null){ps26.close();}
                        if(ps27!=null){ps27.close();}
                        if(ps20!=null){ps20.close();}
                    } catch (SQLException e) {
                        logger.error("SQLException in closing PreparedStatement or ResultSet");
                    }
                }
            }
            //driver_id
        }
        // Adding new Driver or updating driver

        /*
         * Editing list of driver trucks
         */
        HashSet<Integer> possibleTrucksSet = null;
        HashSet<Integer> checkedTrucksSet = null;
        if(request.getParameter("driverTrucks_id")!=null){
            PreparedStatement ps31 = null; // get set of possible trucks for selected driver
            PreparedStatement ps32 = null; // delete pairs (driver_id, auto_id)
            PreparedStatement ps33 = null; // add new pairs (driver_id, auto_id)
            ResultSet rs31 = null;
            int driverTrucksIDint = Integer.parseInt(request.getParameter("driverTrucks_id"));

            try{
                //select auto.id from drivers, driver_licence where drivers.id=driver_licence.driver_id
                // and auto.driving_licence_category = driver_licence.licence_id and drivers.id=?
                possibleTrucksSet = new HashSet<Integer>();
                ps31 = con.prepareStatement(
                    "select auto.id from drivers, driver_licence, auto " +
                        "where drivers.id=driver_licence.driver_id and auto.driving_licence_category = driver_licence.licence_id " +
                            "and drivers.id=?");
                ps31.setInt(1, driverTrucksIDint);
                rs31 = ps31.executeQuery();
                if(rs31!=null){
                    while(rs31.next()){
                        possibleTrucksSet.add(rs31.getInt("auto.id"));
                    }
                }
                if(!possibleTrucksSet.isEmpty()){// if !isEmpty() get list of checkboxes and do update...
                    checkedTrucksSet = new HashSet<Integer>();
                    for (Integer possibleTruckIDint : possibleTrucksSet) {
                        String isCheckedTruckCheckBox = request.getParameter("truckID_"+possibleTruckIDint);
                        if("on".equals(isCheckedTruckCheckBox)){
                            checkedTrucksSet.add(possibleTruckIDint);
                        }
                    }

                    ps32 = con.prepareStatement("DELETE from driver_auto where driver_id=?");
                    ps32.setInt(1, driverTrucksIDint);
                    ps32.execute();

                    StringBuffer driverAutoPairsQuery = new StringBuffer(256);
                    //INSERT INTO driver_auto VALUES(1,1),(1,2);
                    driverAutoPairsQuery.append("INSERT INTO driver_auto VALUES");
                    int addedDAPairsCount = 0;
                    for(Integer checkedTruckIDint : checkedTrucksSet){
                        if(addedDAPairsCount>0){driverAutoPairsQuery.append(",");}
                        driverAutoPairsQuery.append("(");
                        driverAutoPairsQuery.append(driverTrucksIDint);
                        driverAutoPairsQuery.append(",");
                        driverAutoPairsQuery.append(checkedTruckIDint);
                        driverAutoPairsQuery.append(")");
                        addedDAPairsCount++;
                    }
                    if(addedDAPairsCount>0){
                        ps33 = con.prepareStatement(driverAutoPairsQuery.toString());
                        ps33.execute();
                    }
                    AddUpdSuccessful = "Successful";

                }//if(!possibleTrucksSet.isEmpty())
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    if(rs31!=null){rs31.close();}
                    if(ps31!=null){ps31.close();}
                    if(ps32!=null){ps32.close();}
                    if(ps33!=null){ps33.close();}
                } catch (SQLException e) {
                    logger.error("SQLException in closing PreparedStatement or ResultSet");
                }
            }
        }
        //Editing list of driver trucks

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

        if(itemDriverForTrucksListEditing!=null){
            request.getSession().setAttribute("itemDriverForTrucksListEditing", itemDriverForTrucksListEditing);
            request.getSession().setAttribute("listPossibleTrucks", listPossibleTrucks);
            request.getSession().setAttribute("selectedTrucksSet", selectedTrucksSet);
        }
        if(itemDriverToEdit!=null){
            request.getSession().setAttribute("itemDriverToEdit", itemDriverToEdit);
            request.getSession().setAttribute("dlcChecked", dlcChecked);
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
        request.getSession().setAttribute("listDLC", listDLC);
        request.getSession().setAttribute("driversMap", driversMap);
        request.getRequestDispatcher("drivers.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
