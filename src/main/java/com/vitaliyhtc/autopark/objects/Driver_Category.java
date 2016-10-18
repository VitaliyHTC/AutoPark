package com.vitaliyhtc.autopark.objects;

import java.io.Serializable;

public class Driver_Category implements Serializable{
    private static final long serialVersionUID = 6297385302078200405L;

    private int id;
    private String category;

    public Driver_Category(int id, String category) {
        this.id = id;
        this.category = category;
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

    @Override
    public String toString() {
        return "Driver_Category{" +
                "id=" + id +
                ", category='" + category + '\'' +
                '}';
    }
}
