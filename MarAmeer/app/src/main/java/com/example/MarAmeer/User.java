package com.example.MarAmeer;

public class User {
    private String username;
    private String fname;
    private String lname;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }


    public String getUsername() {
        return username;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }


    public User(String username, String fname, String lname) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
    }
}
