package com.example.nucorsafespot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSubPage(View view) {
        Intent intent = new Intent(this, SubActivity.class);
        String area = ((Button) view).getText().toString();
        intent.putExtra("AREA_NAME", area);
        startActivity(intent);
    }
}