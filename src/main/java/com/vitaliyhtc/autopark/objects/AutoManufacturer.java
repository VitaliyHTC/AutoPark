package com.vitaliyhtc.autopark.objects;

import java.io.Serializable;

public class AutoManufacturer implements Serializable {
    private static final long serialVersionUID = 6297385302078200401L;

    private int id;
    private String manufacturer_name;
    private String description;

    public AutoManufacturer(int id, String manufacturer_name, String description) {
        this.id = id;
        this.manufacturer_name = manufacturer_name;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }
    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AutoManufacturer{" +
                "id=" + id +
                ", manufacturer_name='" + manufacturer_name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
