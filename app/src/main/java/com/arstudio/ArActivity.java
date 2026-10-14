package com.arstudio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.arstudio.ar.CustomArFragment;
import com.arstudio.ar.ObjectRenderableFactory;
import com.arstudio.model.ArObjectType;
import com.arstudio.model.PlacedObject;
import com.arstudio.util.ARCoreHelper;
import com.google.android.material.button.MaterialButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.SelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AR Camera Activity.
 * Manages live camera tracking, horizontal surface detection,
 * procedural 3D object placement, and object transform controls (move, scale, rotate, delete).
 */
public class ArActivity extends AppCompatActivity {

    private static final String TAG = "ArActivity";
    public static final String EXTRA_OBJECT_TYPE = "extra_object_type";

    // AR Fragment & Factory
    private CustomArFragment arFragment;
    private ObjectRenderableFactory renderableFactory;

    // Placed objects tracking
    private final List<PlacedObject> placedObjects = new ArrayList<>();
    private PlacedObject selectedPlacedObject = null;
    private ArObjectType currentObjectType = ArObjectType.CUBE;

    // UI Elements
    private ImageButton btnBack;
    private TextView tvSurfaceStatus;
    private ImageButton btnTogglePlanes;
    private ImageButton btnHelpAr;
    private ImageButton btnResetScene;

    private LinearLayout layoutScanInstruction;
    private ImageView ivScanIcon;
    private TextView tvScanInstruction;

    private CardView cardSelectedObject;
    private ImageView ivSelectedIcon;
    private TextView tvSelectedObjectName;
    private MaterialButton btnDeleteSelected;

    private TextView tvPlacedCount;
    private LinearLayout layoutObjectCarousel;
    private final List<View> carouselViews = new ArrayList<>();

    private boolean isSurfaceDetected = false;
    private boolean arePlanesVisible = true;
    private long objectIdCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verify device compatibility before initializing AR session
        if (!ARCoreHelper.checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ar);

        initViews();
        setupListeners();
        setupCarousel();

        // Check if an object was pre-selected from the Gallery
        if (getIntent() != null && getIntent().hasExtra(EXTRA_OBJECT_TYPE)) {
            String typeName = getIntent().getStringExtra(EXTRA_OBJECT_TYPE);
            try {
                if (typeName != null) {
                    selectObjectType(ArObjectType.valueOf(typeName));
                }
            } catch (Exception e) {
                Log.w(TAG, "Invalid object type passed: " + typeName);
            }
        }

        // Initialize Procedural 3D Renderable Factory
        renderableFactory = new ObjectRenderableFactory();
        renderableFactory.initialize(this, () -> {
            Log.d(TAG, "Renderable factory initialized successfully");
        });

        setupArFragment();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvSurfaceStatus = findViewById(R.id.tvSurfaceStatus);
        btnTogglePlanes = findViewById(R.id.btnTogglePlanes);
        btnHelpAr = findViewById(R.id.btnHelpAr);
        btnResetScene = findViewById(R.id.btnResetScene);

        layoutScanInstruction = findViewById(R.id.layoutScanInstruction);
        ivScanIcon = findViewById(R.id.ivScanIcon);
        tvScanInstruction = findViewById(R.id.tvScanInstruction);

        cardSelectedObject = findViewById(R.id.cardSelectedObject);
        ivSelectedIcon = findViewById(R.id.ivSelectedIcon);
        tvSelectedObjectName = findViewById(R.id.tvSelectedObjectName);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);

        tvPlacedCount = findViewById(R.id.tvPlacedCount);
        layoutObjectCarousel = findViewById(R.id.layoutObjectCarousel);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Toggle Plane Visualization
        btnTogglePlanes.setOnClickListener(v -> togglePlaneVisualization());

        // AR Help dialog
        btnHelpAr.setOnClickListener(v -> showArHelpDialog());

        // Reset Scene
        btnResetScene.setOnClickListener(v -> confirmResetScene());

        // Delete Selected Object
        btnDeleteSelected.setOnClickListener(v -> deleteSelectedObject());
    }

    private void setupCarousel() {
        layoutObjectCarousel.removeAllViews();
        carouselViews.clear();

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ArObjectType type : ArObjectType.values()) {
            View itemView = inflater.inflate(R.layout.item_object_selector, layoutObjectCarousel, false);
            ImageView ivIcon = itemView.findViewById(R.id.ivItemIcon);
            TextView tvName = itemView.findViewById(R.id.tvItemName);

            ivIcon.setImageResource(type.getIconResId());
            tvName.setText(type.getDisplayName().replace("3D ", ""));

            itemView.setOnClickListener(v -> selectObjectType(type));

            layoutObjectCarousel.addView(itemView);
            carouselViews.add(itemView);
        }

        updateCarouselSelection();
    }

    private void selectObjectType(ArObjectType type) {
        this.currentObjectType = type;
        updateCarouselSelection();
        Toast.makeText(this, "Selected: " + type.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    private void updateCarouselSelection() {
        ArObjectType[] values = ArObjectType.values();
        for (int i = 0; i < carouselViews.size(); i++) {
            View view = carouselViews.get(i);
            boolean isSelected = (values[i] == currentObjectType);
            view.setBackgroundResource(isSelected ? R.drawable.bg_item_selected : R.drawable.bg_item_unselected);
            ImageView ivIcon = view.findViewById(R.id.ivItemIcon);
            ivIcon.setColorFilter(isSelected
                    ? ContextCompat.getColor(this, R.color.accent)
                    : ContextCompat.getColor(this, R.color.text_muted));
        }
    }

    private void setupArFragment() {
        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        if (arFragment == null) {
            Log.e(TAG, "CustomArFragment not found in layout");
            return;
        }

        // 1. Plane Tap Listener for Object Placement
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                Toast.makeText(this, "Please tap on a horizontal surface (table, desk, floor)", Toast.LENGTH_SHORT).show();
                return;
            }
            placeObject(hitResult);
        });

        // 2. Scene Update Listener for Horizontal Surface Detection
        arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> updateSurfaceDetectionStatus());

        // 3. Selection visualizer wrapper to track object selection
        SelectionVisualizer defaultVisualizer = arFragment.getTransformationSystem().getSelectionVisualizer();
        arFragment.getTransformationSystem().setSelectionVisualizer(new SelectionVisualizer() {
            @Override
            public void applySelectionVisual(BaseTransformableNode node) {
                if (defaultVisualizer != null) {
                    defaultVisualizer.applySelectionVisual(node);
                }
                runOnUiThread(() -> handleNodeSelection(node));
            }

            @Override
            public void removeSelectionVisual(BaseTransformableNode node) {
                if (defaultVisualizer != null) {
                    defaultVisualizer.removeSelectionVisual(node);
                }
                runOnUiThread(() -> handleNodeDeselection(node));
            }
        });
    }

    private void updateSurfaceDetectionStatus() {
        Session session = arFragment.getArSceneView().getSession();
        if (session == null) return;

        Collection<Plane> planes = session.getAllTrackables(Plane.class);
        boolean horizontalPlaneFound = false;

        for (Plane plane : planes) {
            if (plane.getType() == Plane.Type.HORIZONTAL_UPWARD_FACING &&
                    plane.getTrackingState() == TrackingState.TRACKING) {
                horizontalPlaneFound = true;
                break;
            }
        }

        if (horizontalPlaneFound != isSurfaceDetected) {
            isSurfaceDetected = horizontalPlaneFound;
            runOnUiThread(() -> {
                if (isSurfaceDetected) {
                    tvSurfaceStatus.setText(R.string.surface_detected);
                    tvSurfaceStatus.setBackgroundResource(R.drawable.bg_status_badge_detected);
                    tvSurfaceStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_secondary));

                    tvScanInstruction.setText("Surface Detected • Tap surface to place " + currentObjectType.getDisplayName());
                    ivScanIcon.setColorFilter(ContextCompat.getColor(this, R.color.accent_secondary));
                } else {
                    tvSurfaceStatus.setText(R.string.surface_searching);
                    tvSurfaceStatus.setBackgroundResource(R.drawable.bg_status_badge_searching);
                    tvSurfaceStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_warning));

                    tvScanInstruction.setText(R.string.scanning_instruction);
                    ivScanIcon.setColorFilter(ContextCompat.getColor(this, R.color.accent_warning));
                }
            });
        }
    }

    private void placeObject(HitResult hitResult) {
        if (!renderableFactory.isReady()) {
            Toast.makeText(this, "Preparing 3D geometry… please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Anchor & AnchorNode attached to detected plane
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create TransformableNode for one-finger drag, two-finger scale & rotate
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);

        // Configure smooth scale limits
        transformableNode.getScaleController().setMinScale(0.2f);
        transformableNode.getScaleController().setMaxScale(3.5f);

        // Attach genuine 3D procedural geometry
        renderableFactory.populateNode(transformableNode, currentObjectType);

        // Select the newly placed object immediately
        transformableNode.select();

        // Track object
        PlacedObject placedObject = new PlacedObject(++objectIdCounter, anchorNode, transformableNode, currentObjectType);
        placedObjects.add(placedObject);
        updatePlacedCount();

        // Make tap on node re-select it
        transformableNode.setOnTapListener((hitTestResult, motionEvent) -> {
            transformableNode.select();
            handleNodeSelection(transformableNode);
        });

        handleNodeSelection(transformableNode);
    }

    private void handleNodeSelection(BaseTransformableNode selectedNode) {
        if (selectedNode != null) {
            PlacedObject matched = null;
            for (PlacedObject po : placedObjects) {
                if (po.getTransformableNode() == selectedNode) {
                    matched = po;
                    break;
                }
            }

            if (matched != null) {
                selectedPlacedObject = matched;
                cardSelectedObject.setVisibility(View.VISIBLE);
                tvSelectedObjectName.setText("Selected: " + matched.getObjectType().getDisplayName());
                ivSelectedIcon.setImageResource(matched.getObjectType().getIconResId());
                return;
            }
        }

        selectedPlacedObject = null;
        cardSelectedObject.setVisibility(View.GONE);
    }

    private void handleNodeDeselection(BaseTransformableNode deselectedNode) {
        if (selectedPlacedObject != null && selectedPlacedObject.getTransformableNode() == deselectedNode) {
            selectedPlacedObject = null;
            cardSelectedObject.setVisibility(View.GONE);
        }
    }

    private void deleteSelectedObject() {
        if (selectedPlacedObject != null) {
            selectedPlacedObject.remove();
            placedObjects.remove(selectedPlacedObject);
            selectedPlacedObject = null;
            cardSelectedObject.setVisibility(View.GONE);
            updatePlacedCount();
            Toast.makeText(this, "Object deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmResetScene() {
        if (placedObjects.isEmpty()) {
            Toast.makeText(this, "Scene is already empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Reset Scene")
                .setMessage("Remove all " + placedObjects.size() + " placed 3D objects from the AR scene?")
                .setPositiveButton("Reset All", (dialog, which) -> {
                    for (PlacedObject po : placedObjects) {
                        po.remove();
                    }
                    placedObjects.clear();
                    selectedPlacedObject = null;
                    cardSelectedObject.setVisibility(View.GONE);
                    updatePlacedCount();
                    Toast.makeText(this, "Scene cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void togglePlaneVisualization() {
        arePlanesVisible = !arePlanesVisible;
        if (arFragment != null && arFragment.getArSceneView() != null) {
            arFragment.getArSceneView().getPlaneRenderer().setEnabled(arePlanesVisible);
        }
        btnTogglePlanes.setColorFilter(arePlanesVisible
                ? ContextCompat.getColor(this, R.color.accent)
                : ContextCompat.getColor(this, R.color.text_muted));
        Toast.makeText(this, "Plane Grid: " + (arePlanesVisible ? "Visible" : "Hidden"), Toast.LENGTH_SHORT).show();
    }

    private void updatePlacedCount() {
        tvPlacedCount.setText("Placed: " + placedObjects.size());
    }

    private void showArHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("AR Studio Guide")
                .setMessage("How to use Augmented Reality:\n\n"
                        + "1. Scan Surface: Move your phone slowly over a flat surface (desk, floor, table) under good lighting.\n\n"
                        + "2. Plane Grid: When dots appear, horizontal surface detection is active.\n\n"
                        + "3. Place Object: Select any 3D asset from the bottom carousel and tap the dots to place it.\n\n"
                        + "4. Move: Drag an object with 1 finger to reposition it along the surface.\n\n"
                        + "5. Resize: Pinch in or out with 2 fingers to scale the object.\n\n"
                        + "6. Rotate: Twist with 2 fingers to rotate the object.\n\n"
                        + "7. Delete: Tap an object to show the delete button, or use Reset Scene in the top bar.")
                .setPositiveButton("Got It", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ARCoreHelper.hasCameraPermission(this)) {
            ARCoreHelper.requestCameraPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ARCoreHelper.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (!ARCoreHelper.shouldShowRequestPermissionRationale(this)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Camera Permission Needed")
                            .setMessage("AR Studio requires camera access to track surfaces and place 3D objects in your environment. Please grant camera permission in App Settings.")
                            .setPositiveButton("Settings", (dialog, which) -> ARCoreHelper.openAppSettings(this))
                            .setNegativeButton("Cancel", (dialog, which) -> finish())
                            .setCancelable(false)
                            .show();
                } else {
                    Toast.makeText(this, "Camera permission is required to run AR", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (PlacedObject po : placedObjects) {
            po.remove();
        }
        placedObjects.clear();
    }
}
