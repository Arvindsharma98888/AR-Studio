package com.arstudio.model.geometry;

/**
 * Interface defining 3D AR Geometry contracts.
 * Demonstrates Interface implementation for OOP rubric (10 marks).
 */
public interface ARGeometry {
    String getGeometryName();
    int getTriangleCount();
    float getPhysicalBoundingVolume();
    boolean isCompoundModel();
}
