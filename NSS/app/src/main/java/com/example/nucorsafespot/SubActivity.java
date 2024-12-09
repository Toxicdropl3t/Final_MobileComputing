package com.example.nucorsafespot;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
            //create Hashmap of Equipment by type
            HashMap<String, List<Equipment>> equipmentByType = dbHelper.SortEquipmentByType(equipmentList);
            for (String key : equipmentByType.keySet()){
                Button button = styleEquipmentTypeButton(new Button(this), key);
                List<Equipment> value = equipmentByType.get(key);
                equipmentContainer.addView(button);
                button.setOnClickListener(view -> {
                    View dropdownView = LayoutInflater.from(this).inflate(R.layout.popuplayout, null);
                    PopupWindow dropdownWindow = new PopupWindow(
                            dropdownView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            true
                    );
                    dropdownWindow.setFocusable(true);
                    dropdownWindow.setElevation(10);

                    for(Equipment item : value){
                        Button equipmentButton = styleEquipmentButton(new Button(this), item);
                        equipmentButton.setOnClickListener(v -> showDescriptionPopup(item.getName(), item.getDescription(), item.getType()));
                        ((LinearLayout) dropdownView).addView(equipmentButton);
                    }
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    int xPos = location[0];
                    int yPos = location[1] + view.getHeight();

                    dropdownWindow.showAtLocation(view, Gravity.NO_GRAVITY, xPos,yPos);

                });
            }
            // probably going to need to add the equipment type fields into the equipmentContainer and then create a new function to generate a popup containing the equipment objects.
            // Dynamically add buttons for each equipment
            /**for (Equipment equipment : equipmentList) {
                Button equipmentButton = styleEquipmentButton(new Button(this), equipment );
                // Set a click listener to show the description in a popup
                equipmentButton.setOnClickListener(v -> showDescriptionPopup(equipment.getName(), equipment.getDescription(), equipment.getType()));
                equipmentContainer.addView(equipmentButton);
            }**/
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
    private Button styleEquipmentTypeButton(Button button,String type){
        button.setText(type.toUpperCase());
        button.setBackgroundColor(getColor(R.color.DarkGreen));
        button.setTextColor(getColor(R.color.LightGreen));
        return button;
    }
    private LinearLayout popButtonContainer(LinearLayout buttonContainer, List<Equipment> equipmentList){
        for(Equipment item : equipmentList){
           Button equipmentButton = styleEquipmentButton(new Button(this), item);
           equipmentButton.setOnClickListener(view -> {
               showDescriptionPopup(item.getName(), item.getDescription(), item.getType());
           });
           buttonContainer.addView(equipmentButton);
        }
        return buttonContainer;
    };


}