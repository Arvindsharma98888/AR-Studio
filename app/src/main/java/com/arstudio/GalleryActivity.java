package com.arstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.arstudio.model.ArObjectType;
import com.google.android.material.button.MaterialButton;

/**
 * 3D Object Gallery Activity.
 * Displays available 3D procedural models with physical specifications,
 * allowing users to select and launch directly into AR mode.
 */
public class GalleryActivity extends AppCompatActivity {

    private ImageButton btnBackGallery;
    private LinearLayout layoutGalleryCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        btnBackGallery = findViewById(R.id.btnBackGallery);
        layoutGalleryCards = findViewById(R.id.layoutGalleryCards);

        btnBackGallery.setOnClickListener(v -> finish());

        populateGallery();
    }

    private void populateGallery() {
        layoutGalleryCards.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (ArObjectType type : ArObjectType.values()) {
            View cardView = inflater.inflate(R.layout.item_gallery_card, layoutGalleryCards, false);

            ImageView ivIcon = cardView.findViewById(R.id.ivGalleryIcon);
            TextView tvTitle = cardView.findViewById(R.id.tvGalleryTitle);
            TextView tvCategory = cardView.findViewById(R.id.tvGalleryCategory);
            TextView tvDesc = cardView.findViewById(R.id.tvGalleryDesc);
            TextView tvSpecs = cardView.findViewById(R.id.tvGallerySpecs);
            MaterialButton btnPlace = cardView.findViewById(R.id.btnPlaceInAr);

            ivIcon.setImageResource(type.getIconResId());
            tvTitle.setText(type.getDisplayName());
            tvDesc.setText(type.getDescription());

            switch (type) {
                case CUBE:
                    tvCategory.setText("Procedural Primitive");
                    tvSpecs.setText("Dimensions: 0.16m³ • Shading: PBR Opaque • Material: Sky Blue");
                    break;
                case SPHERE:
                    tvCategory.setText("Procedural Primitive");
                    tvSpecs.setText("Radius: 0.10m • Shading: Specular Highlights • Material: Ruby");
                    break;
                case CYLINDER:
                    tvCategory.setText("Procedural Primitive");
                    tvSpecs.setText("Radius: 0.08m • Height: 0.20m • Material: Emerald Green");
                    break;
                case CHAIR:
                    tvCategory.setText("Compound 3D Furniture");
                    tvSpecs.setText("Components: 4 Turned Legs + Seat + Backrest • Material: Charcoal");
                    break;
                case TABLE:
                    tvCategory.setText("Compound 3D Furniture");
                    tvSpecs.setText("Components: 4 Cylindrical Posts + Surface Deck • Material: Walnut");
                    break;
                case PLANT:
                    tvCategory.setText("Organic Compound 3D");
                    tvSpecs.setText("Components: Terracotta Pot + Soil + Stem + Foliage Canopy");
                    break;
            }

            btnPlace.setOnClickListener(v -> {
                Intent intent = new Intent(GalleryActivity.this, ArActivity.class);
                intent.putExtra(ArActivity.EXTRA_OBJECT_TYPE, type.name());
                startActivity(intent);
            });

            layoutGalleryCards.addView(cardView);
        }
    }
}
