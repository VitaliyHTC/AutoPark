package com.vitaliyhtc.autopark.objects;

import java.io.Serializable;

public class Truck implements Serializable {
    private static final long serialVersionUID = 6297385302078200403L;

    private int id;
    private int manufacturerID;
    private String model;
    private String vinNumber;
    private int drivingLicenceCategoryID;
    private String engineModel;
    private int enginePower;
    private int engineEco;
    private String gearbox;
    private String chassisType;
    private int maxWeight;
    private int equippedWeight;
    private String licencePlateNumber;
    private String description;

    private String manufacturerName;
    private String drivingLicenceCategoryName;


    public Truck(int id, int manufacturerID, String model, String vinNumber, int drivingLicenceCategoryID,
                 String engineModel, int enginePower, int engineEco, String gearbox, String chassisType,
                 int maxWeight, int equippedWeight, String licencePlateNumber, String description,
                 String manufacturerName, String drivingLicenceCategoryName) {
        this.id = id;
        this.manufacturerID = manufacturerID;
        this.model = model;
        this.vinNumber = vinNumber;
        this.drivingLicenceCategoryID = drivingLicenceCategoryID;
        this.engineModel = engineModel;
        this.enginePower = enginePower;
        this.engineEco = engineEco;
        this.gearbox = gearbox;
        this.chassisType = chassisType;
        this.maxWeight = maxWeight;
        this.equippedWeight = equippedWeight;
        this.licencePlateNumber = licencePlateNumber;
        this.description = description;
        this.manufacturerName = manufacturerName;
        this.drivingLicenceCategoryName = drivingLicenceCategoryName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getManufacturerID() {
        return manufacturerID;
    }
    public void setManufacturerID(int manufacturerID) {
        this.manufacturerID = manufacturerID;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getVinNumber() {
        return vinNumber;
    }
    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public int getDrivingLicenceCategoryID() {
        return drivingLicenceCategoryID;
    }
    public void setDrivingLicenceCategoryID(int drivingLicenceCategoryID) {
        this.drivingLicenceCategoryID = drivingLicenceCategoryID;
    }

    public String getEngineModel() {
        return engineModel;
    }
    public void setEngineModel(String engineModel) {
        this.engineModel = engineModel;
    }

    public int getEnginePower() {
        return enginePower;
    }
    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getEngineEco() {
        return engineEco;
    }
    public void setEngineEco(int engineEco) {
        this.engineEco = engineEco;
    }

    public String getGearbox() {
        return gearbox;
    }
    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getChassisType() {
        return chassisType;
    }
    public void setChassisType(String chassisType) {
        this.chassisType = chassisType;
    }

    public int getMaxWeight() {
        return maxWeight;
    }
    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getEquippedWeight() {
        return equippedWeight;
    }
    public void setEquippedWeight(int equippedWeight) {
        this.equippedWeight = equippedWeight;
    }

    public String getLicencePlateNumber() {
        return licencePlateNumber;
    }
    public void setLicencePlateNumber(String licencePlateNumber) {
        this.licencePlateNumber = licencePlateNumber;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getDrivingLicenceCategoryName() {
        return drivingLicenceCategoryName;
    }
    public void setDrivingLicenceCategoryName(String drivingLicenceCategoryName) {
        this.drivingLicenceCategoryName = drivingLicenceCategoryName;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "id=" + id +
                ", manufacturerID=" + manufacturerID +
                ", manufacturerName=" + manufacturerName +
                ", model='" + model + '\'' +
                ", vinNumber='" + vinNumber + '\'' +
                ", drivingLicenceCategoryID=" + drivingLicenceCategoryID +
                ", drivingLicenceCategoryName=" + drivingLicenceCategoryName +
                ", engineModel='" + engineModel + '\'' +
                ", enginePower=" + enginePower +
                ", engineEco=" + engineEco +
                ", gearbox='" + gearbox + '\'' +
                ", chassisType='" + chassisType + '\'' +
                ", maxWeight=" + maxWeight +
                ", equippedWeight=" + equippedWeight +
                ", licencePlateNumber='" + licencePlateNumber + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
