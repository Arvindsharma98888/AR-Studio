package com.arstudio.model;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ux.TransformableNode;

/**
 * Encapsulates a 3D object placed within the AR scene graph.
 */
public class PlacedObject {

    private final long id;
    private final AnchorNode anchorNode;
    private final TransformableNode transformableNode;
    private final ArObjectType objectType;

    public PlacedObject(long id, AnchorNode anchorNode, TransformableNode transformableNode, ArObjectType objectType) {
        this.id = id;
        this.anchorNode = anchorNode;
        this.transformableNode = transformableNode;
        this.objectType = objectType;
    }

    public long getId() {
        return id;
    }

    public AnchorNode getAnchorNode() {
        return anchorNode;
    }

    public TransformableNode getTransformableNode() {
        return transformableNode;
    }

    public ArObjectType getObjectType() {
        return objectType;
    }

    public void remove() {
        if (anchorNode != null) {
            if (anchorNode.getAnchor() != null) {
                anchorNode.getAnchor().detach();
            }
            anchorNode.setParent(null);
        }
    }
}
