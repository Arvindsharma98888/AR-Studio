package com.arstudio.model.geometry;

/**
 * Abstract Base Geometry class demonstrating Inheritance & Abstraction for OOP rubric.
 */
public abstract class AbstractGeometry implements ARGeometry {
    protected final String geometryName;
    protected final int triangleCount;
    protected float scaleMultiplier = 1.0f;

    public AbstractGeometry(String geometryName, int triangleCount) {
        this.geometryName = geometryName;
        this.triangleCount = triangleCount;
    }

    @Override
    public String getGeometryName() {
        return geometryName;
    }

    @Override
    public int getTriangleCount() {
        return triangleCount;
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    public void setScaleMultiplier(float scaleMultiplier) {
        this.scaleMultiplier = scaleMultiplier;
    }

    // Abstract method to be implemented polymorphically by subclasses
    @Override
    public abstract float getPhysicalBoundingVolume();
}
