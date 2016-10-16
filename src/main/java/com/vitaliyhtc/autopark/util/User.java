package com.vitaliyhtc.autopark.util;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 6297385302078200511L;

    private String username;
    private String email;
    private int id;
    private String firstname;
    private String lastname;

    public User(String username, String email, int id, String firstname, String lastname) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
