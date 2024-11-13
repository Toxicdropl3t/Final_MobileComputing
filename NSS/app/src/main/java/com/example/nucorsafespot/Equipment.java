package com.example.nucorsafespot;

// Equipment.java
public class Equipment {
    private String name, description, type;

    public Equipment(String name, String description,String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getType(){return type;}
}