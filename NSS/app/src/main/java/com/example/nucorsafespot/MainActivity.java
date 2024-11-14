package com.example.nucorsafespot;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

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





    }

    public void openSubPage(View view) {
        Intent intent = new Intent(this, SubActivity.class);
        String area = ((Button) view).getText().toString();
        intent.putExtra("AREA_NAME", area);
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