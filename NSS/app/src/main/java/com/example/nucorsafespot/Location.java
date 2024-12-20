package com.example.nucorsafespot;

import androidx.annotation.NonNull;

public class Location {
    private String location_name,location_description;

    public Location(String name, String description){
        this.location_name = name;
        this.location_description = description;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getLocation_description() {
        return location_description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Location{" +
                "location_name='" + location_name + '\'' +
                ", location_description='" + location_description + '\'' +
                '}';
    }
}
