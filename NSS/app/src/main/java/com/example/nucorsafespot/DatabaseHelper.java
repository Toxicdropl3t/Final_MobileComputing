package com.example.nucorsafespot;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper {
    public static List<Equipment> getEquipmentForArea(String area) {
        // Fetch from database or use a sample list for testing
        List<Equipment> equipmentList = new ArrayList<>();
        // Add sample equipment data
        equipmentList.add(new Equipment("Equipment 1", "Description of Equipment 1"));
        equipmentList.add(new Equipment("Equipment 2", "Description of Equipment 2"));
        return equipmentList;
    }
}
