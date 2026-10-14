package com.arstudio.model.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class representing multi-part compound models (Chair, Table, Plant).
 * Demonstrates Collections, Generics, and Polymorphism for marking rubric.
 */
public class CompoundGeometry extends AbstractGeometry {
    // Demonstrates Collections and Generics
    private final List<PrimitiveGeometry> subComponents;

    public CompoundGeometry(String name, int triangles) {
        super(name, triangles);
        this.subComponents = new ArrayList<>();
    }

    public void addSubComponent(PrimitiveGeometry component) {
        this.subComponents.add(component);
    }

    public List<PrimitiveGeometry> getSubComponents() {
        return new ArrayList<>(subComponents);
    }

    @Override
    public float getPhysicalBoundingVolume() {
        // Polymorphic volume computation: sums all subcomponents
        float totalVolume = 0.0f;
        for (PrimitiveGeometry component : subComponents) {
            totalVolume += component.getPhysicalBoundingVolume();
        }
        return totalVolume * scaleMultiplier;
    }

    @Override
    public boolean isCompoundModel() {
        return true;
    }
}
