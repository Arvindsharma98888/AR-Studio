package com.arstudio.ar;

import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ux.ArFragment;

/**
 * Custom ArFragment that hardens session configuration and error handling.
 * Prevents unhandled ARCore exceptions and ensures horizontal plane tracking.
 */
public class CustomArFragment extends ArFragment {

    private static final String TAG = "CustomArFragment";

    @Override
    protected Config onCreateSessionConfig(Session session) {
        Config config = super.onCreateSessionConfig(session);
        // Explicitly focus on horizontal plane detection (tables, floors, desks)
        config.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL);
        config.setLightEstimationMode(Config.LightEstimationMode.AMBIENT_INTENSITY);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        return config;
    }

    @Override
    protected void onArUnavailableException(UnavailableException exception) {
        Log.e(TAG, "ARCore unavailable exception caught safely: ", exception);
        if (getActivity() != null && !getActivity().isFinishing()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("AR Session Notification")
                    .setMessage("AR tracking notice: " + exception.getLocalizedMessage())
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}
