package com.arstudio.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.ar.core.ArCoreApk;

/**
 * Utility helper for Google ARCore device compatibility and runtime permission checks.
 * Prevents crashes caused by unsupported hardware or missing Google Play Services for AR.
 */
public class ARCoreHelper {

    private static final String TAG = "ARCoreHelper";
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    /**
     * Verifies OpenGL ES 3.0+ and ARCore compatibility before starting an AR session.
     * Shows a user-friendly dialog if the device is unsupported, rather than crashing.
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android 7.0 (API 24) or later");
            showCompatibilityDialog(activity, "Android Version Incompatible",
                    "Sceneform and ARCore require Android 7.0 (Nougat, API 24) or later.");
            return false;
        }

        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            String openGlVersionString = activityManager.getDeviceConfigurationInfo().getGlEsVersion();
            try {
                double openGlVersion = Double.parseDouble(openGlVersionString);
                if (openGlVersion < 3.0) {
                    Log.e(TAG, "Sceneform requires OpenGL ES 3.0 or later, got: " + openGlVersionString);
                    showCompatibilityDialog(activity, "OpenGL ES 3.0 Required",
                            "This device does not support OpenGL ES 3.0, which is required for 3D AR rendering.");
                    return false;
                }
            } catch (NumberFormatException e) {
                Log.w(TAG, "Unable to parse OpenGL ES version: " + openGlVersionString, e);
            }
        }

        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(activity);
        if (availability.isTransient()) {
            Log.d(TAG, "ARCore availability is transient: " + availability);
        } else if (!availability.isSupported()) {
            Log.e(TAG, "Device is not compatible with Google ARCore: " + availability);
            showCompatibilityDialog(activity, "ARCore Unsupported",
                    "This device does not support Google Play Services for AR (ARCore). AR features cannot run on this hardware.");
            return false;
        }

        return true;
    }

    /**
     * Checks if camera permission has been granted.
     */
    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests camera permission with the default request code.
     */
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    /**
     * Checks if permission rationale should be shown.
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA);
    }

    /**
     * Opens app settings so the user can manually enable camera permission.
     */
    public static void openAppSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    private static void showCompatibilityDialog(final Activity activity, String title, String message) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                })
                .show();
    }
}
