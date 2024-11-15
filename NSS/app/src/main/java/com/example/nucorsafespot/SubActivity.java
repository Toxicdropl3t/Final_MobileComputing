package com.example.nucorsafespot;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.List;

public class SubActivity extends AppCompatActivity {
    private String areaName;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        areaName = getIntent().getStringExtra("AREA_NAME");
        TextView title = findViewById(R.id.areaTitle);
        title.setText(areaName.toUpperCase());

        LinearLayout equipmentContainer = findViewById(R.id.equipmentContainer);
        dbHelper = new DBHelper(this);

        try {
            // Fetch equipment for the selected area
            List<Equipment> equipmentList = dbHelper.getEquipmentByLocation(areaName);

            // Dynamically add buttons for each equipment
            for (Equipment equipment : equipmentList) {
                Button equipmentButton = styleEquipmentButton(new Button(this), equipment );
                // Set a click listener to show the description in a popup
                equipmentButton.setOnClickListener(v -> showDescriptionPopup(equipment.getName(), equipment.getDescription(), equipment.getType()));
                equipmentContainer.addView(equipmentButton);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Back button
        Button backToMainMenuButton = findViewById(R.id.backToMainMenuButton);
        backToMainMenuButton.setOnClickListener(v -> finish());

    }

    // Popup functionality to show more info for each piece of equipment
    private void showDescriptionPopup(String name, String description, String type) {
        new AlertDialog.Builder(this)
                .setTitle(name + " (" + type + ")")
                .setMessage(description)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Applies styling to the button object
     * @param button
     * @param equipment
     * @return styled button
     */
    private Button styleEquipmentButton(Button button, Equipment equipment){
        button.setText(equipment.getName());
        button.setBackgroundColor(getColor(R.color.DarkGreen));
        button.setTextColor(getColor(R.color.LightGreen));
        return button;
    }
}