package com.example.nucorsafespot;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@author Gage}
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Location> locationList;

        //initialize DBHelper object for db transactions
        DBHelper dbHelper = new DBHelper(this);
        Button button = findViewById(R.id.nssdescriptionbutton);

        try {
            dbHelper.initializeDB();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
             locationList = dbHelper.getLocations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //iterate through locationsList then print locations if list != null (debugging)
        if (locationList != null){
            for (Location location: locationList
            ) {

                String info = location.toString();
                System.out.println(info);


            }
        }else {
            System.out.println("Location List empty");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNssPopup(v);
            }
        });






        ListView locationListView = findViewById(R.id.locationListView);
        dbHelper = new DBHelper(this);

        // Load locations from the database and set up the ListView
        try {
            List<Location> locations = dbHelper.getLocations();
            if (locations != null && !locations.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        getLocationNames(locations) // List of location names to display
                );
                locationListView.setAdapter(adapter);

                // Set an item click listener for the ListView
                locationListView.setOnItemClickListener((parent, view, position, id) -> {
                    Location selectedLocation = locations.get(position);
                    openSubPage(selectedLocation.getLocation_name());
                });
            } else {
                Toast.makeText(this, "No locations found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading locations", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to extract location names from Location objects
    private List<String> getLocationNames(List<Location> locations) {
        List<String> names = new ArrayList<>();
        for (Location location : locations) {
            names.add(location.getLocation_name());
        }
        return names;
    }

    // Method to open the subpage with the selected location name
    public void openSubPage(String areaName) {
        Intent intent = new Intent(this, SubActivity.class);
        intent.putExtra("AREA_NAME", areaName);
        startActivity(intent);
    }
    public void showNssPopup(View anchorview){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupview = inflater.inflate(R.layout.nssdescriptionpopup, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupview,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchorview, Gravity.CENTER, 0,1);

    }

}