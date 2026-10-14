package com.arstudio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.arstudio.util.ARCoreHelper;
import com.google.android.material.button.MaterialButton;
import com.google.ar.core.ArCoreApk;

/**
 * Main Home Activity for AR Studio.
 * Provides entry points to the AR Camera, 3D Object Gallery, How AR Works, and About screens.
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvArStatusBadge;
    private MaterialButton btnExploreAr;
    private CardView cardExploreAr;
    private CardView cardGallery;
    private CardView cardHowArWorks;
    private CardView cardAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tvArStatusBadge = findViewById(R.id.tvArStatusBadge);
        btnExploreAr = findViewById(R.id.btnExploreAr);
        cardExploreAr = findViewById(R.id.cardExploreAr);
        cardGallery = findViewById(R.id.cardGallery);
        cardHowArWorks = findViewById(R.id.cardHowArWorks);
        cardAbout = findViewById(R.id.cardAbout);
    }

    private void setupListeners() {
        // Explore in AR button
        btnExploreAr.setOnClickListener(v -> launchArActivity());
        cardExploreAr.setOnClickListener(v -> launchArActivity());

        // 3D Object Gallery
        cardGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        // How AR Works
        cardHowArWorks.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HowArWorksActivity.class);
            startActivity(intent);
        });

        // About screen
        cardAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkArStatus();
    }

    private void checkArStatus() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            tvArStatusBadge.setText("Checking ARCore support…");
            tvArStatusBadge.setBackgroundResource(R.drawable.bg_status_badge_searching);
            tvArStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.accent_warning));
            tvArStatusBadge.postDelayed(this::checkArStatus, 300);
            return;
        }

        if (availability.isSupported()) {
            tvArStatusBadge.setText("ARCore Supported • Ready");
            tvArStatusBadge.setBackgroundResource(R.drawable.bg_status_badge_detected);
            tvArStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.accent_secondary));
        } else {
            tvArStatusBadge.setText("ARCore Unavailable on Device");
            tvArStatusBadge.setBackgroundResource(R.drawable.bg_status_badge_searching);
            tvArStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.accent_warning));
        }
    }

    private void launchArActivity() {
        Intent intent = new Intent(MainActivity.this, ArActivity.class);
        startActivity(intent);
    }
}
