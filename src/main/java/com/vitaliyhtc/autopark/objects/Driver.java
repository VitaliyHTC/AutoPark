package com.vitaliyhtc.autopark.objects;

import java.io.Serializable;
import java.util.HashMap;

public class Driver implements Serializable {
    private static final long serialVersionUID = 6297385302078200404L;

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String phone1;
    private String phone2;
    private String phoneRelatives;
    private String email;

    private HashMap<Integer, Driver_Category> driverCategoriesMap;
    private HashMap<Integer, Driver_Truck> driverTrucksMap;

    public Driver(int id, String username, String firstname, String lastname, String phone1, String phone2,
                  String phoneRelatives, String email, HashMap<Integer, Driver_Category> driverCategoriesMap,
                  HashMap<Integer, Driver_Truck> driverTrucksMap) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phoneRelatives = phoneRelatives;
        this.email = email;
        this.driverCategoriesMap = driverCategoriesMap;
        this.driverTrucksMap = driverTrucksMap;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone1() {
        return phone1;
    }
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhoneRelatives() {
        return phoneRelatives;
    }
    public void setPhoneRelatives(String phoneRelatives) {
        this.phoneRelatives = phoneRelatives;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<Integer, Driver_Category> getDriverCategoriesMap() {
        return driverCategoriesMap;
    }
    public void setDriverCategoriesMap(HashMap<Integer, Driver_Category> driverCategoriesMap) {
        this.driverCategoriesMap = driverCategoriesMap;
    }

    public HashMap<Integer, Driver_Truck> getDriverTrucksMap() {
        return driverTrucksMap;
    }
    public void setDriverTrucksMap(HashMap<Integer, Driver_Truck> driverTrucksMap) {
        this.driverTrucksMap = driverTrucksMap;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", phoneRelatives='" + phoneRelatives + '\'' +
                ", email='" + email + '\'' +
                ", driverCategoriesMap=" + driverCategoriesMap +
                ", driverTrucksMap=" + driverTrucksMap +
                '}';
    }
}
