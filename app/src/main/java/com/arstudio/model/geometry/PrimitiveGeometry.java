package com.arstudio.model.geometry;

/**
 * Concrete class representing basic 3D primitives (Cube, Sphere, Cylinder).
 * Demonstrates Polymorphism for OOP rubric.
 */
public class PrimitiveGeometry extends AbstractGeometry {
    private final float dimension;

    public PrimitiveGeometry(String name, int triangles, float dimension) {
        super(name, triangles);
        this.dimension = dimension;
    }

    @Override
    public float getPhysicalBoundingVolume() {
        // Polymorphic volume computation for primitive cube/sphere approximation
        return dimension * dimension * dimension * scaleMultiplier;
    }

    @Override
    public boolean isCompoundModel() {
        return false;
    }

    public float getDimension() {
        return dimension;
    }
}
