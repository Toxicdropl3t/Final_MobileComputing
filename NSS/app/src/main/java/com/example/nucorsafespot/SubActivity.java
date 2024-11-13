package com.example.nucorsafespot;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class SubActivity extends AppCompatActivity {
    private String areaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        areaName = getIntent().getStringExtra("AREA_NAME");
        TextView title = findViewById(R.id.areaTitle);
        title.setText(areaName);

        LinearLayout equipmentContainer = findViewById(R.id.equipmentContainer);

        // Assume fetchDataFromDatabase is a method that returns a list of equipment names for the given area
        List<Equipment> equipmentList = fetchDataFromDatabase(areaName);

        for (Equipment equipment : equipmentList) {
            Button equipmentButton = new Button(this);
            equipmentButton.setText(equipment.getName());
            equipmentButton.setOnClickListener(v -> showDescriptionPopup(equipment.getDescription()));
            equipmentContainer.addView(equipmentButton);
        }
    }

    private void showDescriptionPopup(String description) {
        new AlertDialog.Builder(this)
                .setTitle("Description")
                .setMessage(description)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * @deprecated
     * @param area
     * @return
     */
    private List<Equipment> fetchDataFromDatabase(String area) {
        // This is a placeholder. Implement database fetching logic here.
        return DBHelper.getEquipmentList(area);
    }
}
//not sure if we will need this class