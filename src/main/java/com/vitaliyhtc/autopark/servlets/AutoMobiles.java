package com.vitaliyhtc.autopark.servlets;

import com.vitaliyhtc.autopark.objects.AutoManufacturer;
import com.vitaliyhtc.autopark.objects.DriverLicenceCategory;
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
         * Return selected item for editing;
         */
        Truck itemTruckToEdit = null; //if in adding/editing item occurs some error <<< used in next section
        String itemIdToEdit = request.getParameter("itemIDtoEdit");
        String errorItemIdToEdit = null;
        if(itemIdToEdit!=null){
            try{
                int itemIDtoEditInt = Integer.parseInt(itemIdToEdit);
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                try{
                    ps1 = con.prepareStatement("select auto.*, auto_manufacturer.manufacturer_name, driving_licence_categories.licence_category from auto, auto_manufacturer, driving_licence_categories where auto.id=? and auto.manufacturer = auto_manufacturer.id and auto.driving_licence_category = driving_licence_categories.id limit 1");
                    ps1.setInt(1, itemIDtoEditInt);
                    rs1 = ps1.executeQuery();
                    if(rs1!=null && rs1.next()){
                        itemTruckToEdit = new Truck(
                                rs1.getInt("id"),
                                rs1.getInt("manufacturer"),
                                rs1.getString("model"),
                                rs1.getString("vin_number"),
                                rs1.getInt("driving_licence_category"),
                                rs1.getString("engine_model"),
                                rs1.getInt("engine_power"),
                                rs1.getInt("engine_eco"),
                                rs1.getString("gearbox"),
                                rs1.getString("chassis_type"),
                                rs1.getInt("max_weight"),
                                rs1.getInt("equipped_weight"),
                                rs1.getString("license_plate_number"),
                                rs1.getString("description"),
                                rs1.getString("manufacturer_name"),
                                rs1.getString("licence_category")
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
         * Generate listAM, listDLC for adding/editing section
         */
        PreparedStatement ps4 = null;
        PreparedStatement ps5 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ArrayList<DriverLicenceCategory> listDLC = new ArrayList<DriverLicenceCategory>();
        ArrayList<AutoManufacturer> listAM = new ArrayList<AutoManufacturer>();
        try{
            ps4 = con.prepareStatement("select id, licence_category from driving_licence_categories");
            ps5 = con.prepareStatement("select id, manufacturer_name from auto_manufacturer");
            // id, licence_category, category_description
            // id, manufacturer_name, description
            rs4 = ps4.executeQuery();
            rs5 = ps5.executeQuery();
            if(rs4 != null){
                while(rs4.next()){
                    DriverLicenceCategory driverLicenceCategory = new DriverLicenceCategory(
                            rs4.getInt("id"),
                            rs4.getString("licence_category"),
                            ""
                    );//we don't need DLC description for adding/editing automobile
                    listDLC.add(driverLicenceCategory);
                }
            }
            if(rs5!=null){
                while(rs5.next()){
                    AutoManufacturer autoManufacturer = new AutoManufacturer(
                            rs5.getInt("id"),
                            rs5.getString("manufacturer_name"),
                            ""
                    );//we don't need AM description for adding/editing automobile
                    listAM.add(autoManufacturer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }finally{
            try {
                rs4.close();
                rs5.close();
                ps4.close();
                ps5.close();
            } catch (SQLException e) {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
        // Generate listAM, listDLC for adding/editing section

        /*
         * Adding or updating item to auto table
         */
        String AddUpdSuccessful = null;
        StringBuffer errorMsgBuffer = new StringBuffer( 1024 );
        if(request.getParameter("truck_id")!=null){
            //validating parameters
            int manufacturerID = Integer.parseInt(request.getParameter("manufacturerID"));
            String manufacturerName=null;
            String truckModel = request.getParameter("truckModel");
            String engineModel = request.getParameter("engineModel");
            int enginePower = Integer.parseInt(request.getParameter("enginePower"));
            int engineEco = Integer.parseInt(request.getParameter("engineEco"));
            String gearbox = request.getParameter("gearbox");
            String chassisType = request.getParameter("chassisType");
            int equippedWeight = Integer.parseInt(request.getParameter("equippedWeight"));
            int maxWeight = Integer.parseInt(request.getParameter("maxWeight"));
            int drivingLicenceCategoryID = Integer.parseInt(request.getParameter("drivingLicenceCategoryID"));
            String drivingLicenceCategoryName=null;
            String licencePlateNumber = request.getParameter("licencePlateNumber");
            String vinNumber = request.getParameter("vinNumber");
            String description = request.getParameter("description");
            if(manufacturerID<1){
                errorMsgBuffer.append("Please, select manufacturer of auto.<br>");
                manufacturerName="";
            }else{
                for (AutoManufacturer autoManufacturer : listAM) {
                    if(autoManufacturer.getId()==manufacturerID){
                        manufacturerName = autoManufacturer.getManufacturer_name();
                    }
                }
            }
            if(truckModel == null || truckModel.equals("") || truckModel.length()>64){
                errorMsgBuffer.append("Auto model can't be null or empty or more than 64 symbols.<br>");
            }
            if(engineModel == null || engineModel.equals("") || engineModel.length()>64){
                errorMsgBuffer.append("Engine model can't be null or empty or more than 64 symbols.<br>");
            }
            if(enginePower<10 || enginePower > 10000){
                errorMsgBuffer.append("Engine power can't be less than 10HP or more than 10_000HP.<br>");
            }
            if(engineEco<1 || engineEco>7){
                errorMsgBuffer.append("Engine eco can't be less than EURO1 or more than EURO7.<br>");
            }
            if(gearbox == null || gearbox.equals("") || gearbox.length()>64){
                errorMsgBuffer.append("Gearbox can't be null or empty or more than 64 symbols.<br>");
            }
            if(chassisType == null || chassisType.equals("") || chassisType.length()>64){
                errorMsgBuffer.append("Chassis type can't be null or empty or more than 64 symbols.<br>");
            }
            if(equippedWeight<100 || equippedWeight>400000){
                errorMsgBuffer.append("Equipped weight can't be less than 100kg or more than 400_000kg.<br>");
            }
            if(maxWeight<100 || maxWeight>1000000){
                errorMsgBuffer.append("Maximal weight can't be less than 100kg or more than 1_000_000kg");
            }
            if(drivingLicenceCategoryID<1 || drivingLicenceCategoryID >13){
                errorMsgBuffer.append("Please, select driving licence category.<br>");
                drivingLicenceCategoryName="";
            }else{
                for(DriverLicenceCategory driverLicenceCategory : listDLC){
                    if(driverLicenceCategory.getId()==drivingLicenceCategoryID){
                        drivingLicenceCategoryName = driverLicenceCategory.getCategory();
                    }
                }
            }
            if(licencePlateNumber == null || licencePlateNumber.equals("") || licencePlateNumber.length()>32){
                errorMsgBuffer.append("Licence plate number can't be null or empty or more than 32 symbols.<br>");
            }
            if(vinNumber == null || vinNumber.equals("") || vinNumber.length() > 32){
                errorMsgBuffer.append("Vin number can't be null or empty or more than 32 symbols.<br>");
            }
            if(description == null || description.equals("") || description.length()>255){
                errorMsgBuffer.append("Description can't be null or empty or more then 255 symbols.<br>");
            }
            if(errorMsgBuffer.length()>3){
                itemTruckToEdit = new Truck(
                        Integer.parseInt(request.getParameter("truck_id")),
                        manufacturerID,
                        truckModel,
                        vinNumber,
                        drivingLicenceCategoryID,
                        engineModel,
                        enginePower,
                        engineEco,
                        gearbox,
                        chassisType,
                        maxWeight,
                        equippedWeight,
                        licencePlateNumber,
                        description,
                        manufacturerName,
                        drivingLicenceCategoryName
                );
            }else{
                PreparedStatement ps2 = null;
                PreparedStatement ps6 = null;
                PreparedStatement ps7 = null;
                ResultSet rs6 = null;
                ResultSet rs7 = null;
                try{
                    int truck_id = Integer.parseInt(request.getParameter("truck_id"));
                    if(truck_id == -1){
                        Boolean cancelAdding = false; // if duplication found - cancel adding.
                        ps6 = con.prepareStatement("select id, vin_number from auto where vin_number=?");
                        ps6.setString(1, vinNumber);
                        rs6 = ps6.executeQuery();
                        if(rs6!=null && rs6.next()){
                            if(vinNumber.equals(rs6.getString("vin_number"))){
                                errorMsgBuffer.append("Automobile with VIN: " + vinNumber +
                                        ", already exists with ID: " + rs6.getInt("id") + ". See in table by ID.<br>");
                                cancelAdding=true;
                            }
                        }
                        ps7 = con.prepareStatement("select id, license_plate_number from auto where license_plate_number=?");
                        ps7.setString(1, licencePlateNumber);
                        rs7 = ps7.executeQuery();
                        if(rs7!=null && rs7.next()){
                            if(licencePlateNumber.equals(rs7.getString("license_plate_number"))){
                                errorMsgBuffer.append("Automobile with licence plate number: " + licencePlateNumber +
                                        ", already exists with ID: " + rs7.getInt("id") + ". See in table by ID.<br>");
                                cancelAdding=true;
                            }
                        }
                        if(cancelAdding){ // if duplicate found - return for editing
                            itemTruckToEdit = new Truck(
                                    Integer.parseInt(request.getParameter("truck_id")),
                                    manufacturerID,
                                    truckModel,
                                    vinNumber,
                                    drivingLicenceCategoryID,
                                    engineModel,
                                    enginePower,
                                    engineEco,
                                    gearbox,
                                    chassisType,
                                    maxWeight,
                                    equippedWeight,
                                    licencePlateNumber,
                                    description,
                                    manufacturerName,
                                    drivingLicenceCategoryName
                            );
                            errorMsgBuffer.append("You can edit existing automobiles or add other.<br>");
                        }
                        if(!cancelAdding){
                            ps2 = con.prepareStatement("INSERT INTO auto VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                            //INSERT INTO auto VALUES
                            //(null, 1, 'R730LA6X4MNA', 'YS2R6X400C2078249', 11, 'V8 DC16', 730, 6,
                            // 'Scania Opticruise 12+2','6x4 timber truck', 75000, 10000, 'BX6673BI', 'info');
                            ps2.setInt(1, manufacturerID);
                            ps2.setString(2, truckModel);
                            ps2.setString(3, vinNumber);
                            ps2.setInt(4, drivingLicenceCategoryID);
                            ps2.setString(5, engineModel);
                            ps2.setInt(6, enginePower);
                            ps2.setInt(7, engineEco);
                            ps2.setString(8, gearbox);
                            ps2.setString(9, chassisType);
                            ps2.setInt(10, maxWeight);
                            ps2.setInt(11, equippedWeight);
                            ps2.setString(12, licencePlateNumber);
                            ps2.setString(13, description);
                            ps2.execute();
                            AddUpdSuccessful = "Successful!";
                        }
                    }else{
                        ps2 = con.prepareStatement("UPDATE auto SET " +
                                "manufacturer=?, model=?, vin_number=?, driving_licence_category=?, " +
                                "engine_model=?, engine_power=?, engine_eco=?, gearbox=?, chassis_type=?, " +
                                "max_weight=?, equipped_weight=?, license_plate_number=?, description=? " +
                                "WHERE id=?");
                        ps2.setInt(1, manufacturerID);
                        ps2.setString(2, truckModel);
                        ps2.setString(3, vinNumber);
                        ps2.setInt(4, drivingLicenceCategoryID);
                        ps2.setString(5, engineModel);
                        ps2.setInt(6, enginePower);
                        ps2.setInt(7, engineEco);
                        ps2.setString(8, gearbox);
                        ps2.setString(9, chassisType);
                        ps2.setInt(10, maxWeight);
                        ps2.setInt(11, equippedWeight);
                        ps2.setString(12, licencePlateNumber);
                        ps2.setString(13, description);
                        ps2.setInt(14, truck_id);
                        ps2.execute();
                        AddUpdSuccessful = "Successful";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("Database connection problem");
                    throw new ServletException("DB Connection problem.");
                }finally{
                    try {
                        if(rs6!=null){rs6.close();}
                        if(ps6!=null){ps6.close();}
                        if(rs7!=null){rs7.close();}
                        if(ps7!=null){ps7.close();}
                        if(ps2!=null){ps2.close();}
                    } catch (SQLException e) {
                        logger.error("SQLException in closing PreparedStatement or ResultSet");
                    }
                }
            }
            // truck_id
        }
        // Adding or updating item to auto table

        /* list of columns of auto table
        id
        manufacturer
        model
        vin_number
        driving_licence_category
        engine_model
        engine_power
        engine_eco
        gearbox
        chassis_type
        max_weight
        equipped_weight
        license_plate_number
        description
         */

        /*
         * Generate list of trucks/cars for our jsp :)
         */
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Truck> listTrucks = new ArrayList<Truck>();
        try{
            ps = con.prepareStatement("select auto.*, auto_manufacturer.manufacturer_name, driving_licence_categories.licence_category from auto, auto_manufacturer, driving_licence_categories where auto.manufacturer = auto_manufacturer.id and auto.driving_licence_category = driving_licence_categories.id");
            // see in database.sql
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
        request.getSession().removeAttribute("listDLC");
        request.getSession().removeAttribute("listTrucks");
        request.getSession().removeAttribute("driversMap");
        request.getSession().removeAttribute("itemAMtoEdit");
        request.getSession().removeAttribute("itemTruckToEdit");
        request.getSession().removeAttribute("AddUpdSuccessful");
        request.getSession().removeAttribute("AddUpdFailed");

        // Needed items: itemTruckToEdit, listAM, listDLC
        if(errorItemIdToEdit!=null){
            errorMsgBuffer.append(errorItemIdToEdit);
        }

        if(itemTruckToEdit!=null){
            request.getSession().setAttribute("itemTruckToEdit", itemTruckToEdit);
        }
        if(AddUpdSuccessful!=null){
            request.getSession().setAttribute("AddUpdSuccessful", AddUpdSuccessful);
        }
        if(errorMsgBuffer.length() > 3){
            request.getSession().setAttribute("AddUpdFailed", errorMsgBuffer.toString());
        }
        request.getSession().setAttribute("listDLC", listDLC);
        request.getSession().setAttribute("listAM", listAM);
        request.getSession().setAttribute("listTrucks", listTrucks);
        request.getRequestDispatcher("automobiles.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
