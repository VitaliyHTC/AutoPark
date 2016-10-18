package com.vitaliyhtc.autopark.objects;

import java.io.Serializable;

public class Driver_Truck implements Serializable {
    private static final long serialVersionUID = 6297385302078200406L;

    private int id;
    private String manufacturer;
    private String model;
    private String licencePlateNumber;
    private int categoryID;

    public Driver_Truck(int id, String manufacturer, String model, String licencePlateNumber, int categoryID) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.licencePlateNumber = licencePlateNumber;
        this.categoryID = categoryID;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getLicencePlateNumber() {
        return licencePlateNumber;
    }
    public void setLicencePlateNumber(String licencePlateNumber) {
        this.licencePlateNumber = licencePlateNumber;
    }

    public int getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return "Driver_Truck{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", licencePlateNumber='" + licencePlateNumber + '\'' +
                ", categoryID=" + categoryID +
                '}';
    }
}
