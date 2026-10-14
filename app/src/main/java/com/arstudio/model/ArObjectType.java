package com.arstudio.model;

import com.arstudio.R;

/**
 * Enumeration of available 3D objects in AR Studio.
 */
public enum ArObjectType {
    CUBE("3D Cube", "Geometric precision cube with uniform face lighting (0.16m)", R.drawable.ic_cube),
    SPHERE("3D Sphere", "Smooth reflective sphere with specular highlights (0.20m)", R.drawable.ic_sphere),
    CYLINDER("3D Cylinder", "Rotational symmetry cylinder with planar end caps (0.20m)", R.drawable.ic_cylinder),
    CHAIR("Modern Chair", "Architectural chair with 4 structural legs, seat, and backrest", R.drawable.ic_chair),
    TABLE("Study Table", "Sturdy surface table with 4 cylindrical posts and top deck", R.drawable.ic_table),
    PLANT("Potted Plant", "Terracotta ceramic pot with clustered spherical foliage canopy", R.drawable.ic_plant);

    private final String displayName;
    private final String description;
    private final int iconResId;

    ArObjectType(String displayName, String description, int iconResId) {
        this.displayName = displayName;
        this.description = description;
        this.iconResId = iconResId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }
}
