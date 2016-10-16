package com.vitaliyhtc.autopark.objects;

import java.io.Serializable;

public class DriverLicenceCategory implements Serializable {
    private static final long serialVersionUID = 6297385302078200400L;

    private int id;
    private String category;
    private String description;

    public DriverLicenceCategory(int id, String category, String description) {
        this.id = id;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DriverLicenceCategory{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
