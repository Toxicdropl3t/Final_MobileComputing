package com.example.nucorsafespot;

// Equipment.java
public class Equipment {
    private String name;
    private String description;

    public Equipment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}