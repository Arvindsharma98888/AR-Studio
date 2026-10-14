package com.arstudio;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Educational Activity explaining the engineering principles behind Augmented Reality:
 * SLAM & VIO motion tracking, horizontal surface detection, anchors, and PBR lighting.
 */
public class HowArWorksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_ar_works);

        ImageButton btnBack = findViewById(R.id.btnBackHowAr);
        btnBack.setOnClickListener(v -> finish());
    }
}
