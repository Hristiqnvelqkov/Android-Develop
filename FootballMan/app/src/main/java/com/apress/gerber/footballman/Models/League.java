package com.apress.gerber.footballman.Models;


import java.io.Serializable;
import java.util.UUID;


public class League implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String name;
    public League() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }


}
