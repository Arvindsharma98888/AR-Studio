package com.arstudio.ar;

import android.content.Context;
import android.util.Log;
import com.arstudio.model.ArObjectType;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.TransformableNode;
import java.util.concurrent.CompletableFuture;

/**
 * Procedural 3D Geometry Factory.
 * Generates genuine 3D meshes (Cubes, Spheres, Cylinders, Chairs, Tables, Plants)
 * without external network dependencies.
 */
public class ObjectRenderableFactory {

    private static final String TAG = "RenderableFactory";

    // Single shape renderables
    private ModelRenderable cubeRenderable;
    private ModelRenderable sphereRenderable;
    private ModelRenderable cylinderRenderable;

    // Chair components
    private ModelRenderable chairSeatRenderable;
    private ModelRenderable chairBackRenderable;
    private ModelRenderable chairLegRenderable;

    // Table components
    private ModelRenderable tableTopRenderable;
    private ModelRenderable tableLegRenderable;

    // Plant components
    private ModelRenderable plantPotRenderable;
    private ModelRenderable plantSoilRenderable;
    private ModelRenderable plantStemRenderable;
    private ModelRenderable plantLeavesRenderable;

    private boolean isReady = false;

    public void initialize(Context context, Runnable onReadyCallback) {
        // Colors
        Color blueColor = new Color(0.01f, 0.52f, 0.78f);       // #0284C7 Sky Blue
        Color redColor = new Color(0.88f, 0.11f, 0.28f);        // #E11D48 Ruby
        Color tealColor = new Color(0.02f, 0.59f, 0.41f);       // #059669 Emerald
        Color darkSlateColor = new Color(0.12f, 0.16f, 0.23f);  // #1E293B Slate
        Color walnutColor = new Color(0.57f, 0.25f, 0.05f);     // #92400E Walnut Wood
        Color woodLegColor = new Color(0.25f, 0.15f, 0.08f);    // Dark Wood
        Color terracottaColor = new Color(0.92f, 0.35f, 0.05f); // #EA580C Terracotta
        Color soilColor = new Color(0.27f, 0.10f, 0.02f);       // #451A03 Dark Soil
        Color stemColor = new Color(0.08f, 0.50f, 0.24f);       // #15803D Olive Green
        Color leafColor = new Color(0.09f, 0.64f, 0.29f);       // #16A34A Vibrant Leaf

        CompletableFuture<Material> blueMatFuture = MaterialFactory.makeOpaqueWithColor(context, blueColor);
        CompletableFuture<Material> redMatFuture = MaterialFactory.makeOpaqueWithColor(context, redColor);
        CompletableFuture<Material> tealMatFuture = MaterialFactory.makeOpaqueWithColor(context, tealColor);
        CompletableFuture<Material> darkSlateMatFuture = MaterialFactory.makeOpaqueWithColor(context, darkSlateColor);
        CompletableFuture<Material> walnutMatFuture = MaterialFactory.makeOpaqueWithColor(context, walnutColor);
        CompletableFuture<Material> woodLegMatFuture = MaterialFactory.makeOpaqueWithColor(context, woodLegColor);
        CompletableFuture<Material> terracottaMatFuture = MaterialFactory.makeOpaqueWithColor(context, terracottaColor);
        CompletableFuture<Material> soilMatFuture = MaterialFactory.makeOpaqueWithColor(context, soilColor);
        CompletableFuture<Material> stemMatFuture = MaterialFactory.makeOpaqueWithColor(context, stemColor);
        CompletableFuture<Material> leafMatFuture = MaterialFactory.makeOpaqueWithColor(context, leafColor);

        CompletableFuture.allOf(
                blueMatFuture, redMatFuture, tealMatFuture, darkSlateMatFuture,
                walnutMatFuture, woodLegMatFuture, terracottaMatFuture,
                soilMatFuture, stemMatFuture, leafMatFuture
        ).thenAccept(voidResult -> {
            try {
                Material blueMat = blueMatFuture.get();
                Material redMat = redMatFuture.get();
                Material tealMat = tealMatFuture.get();
                Material darkSlateMat = darkSlateMatFuture.get();
                Material walnutMat = walnutMatFuture.get();
                Material woodLegMat = woodLegMatFuture.get();
                Material terracottaMat = terracottaMatFuture.get();
                Material soilMat = soilMatFuture.get();
                Material stemMat = stemMatFuture.get();
                Material leafMat = leafMatFuture.get();

                // 1. Cube: 0.16m x 0.16m x 0.16m, centered at y = 0.08m
                cubeRenderable = ShapeFactory.makeCube(
                        new Vector3(0.16f, 0.16f, 0.16f),
                        new Vector3(0.0f, 0.08f, 0.0f),
                        blueMat);

                // 2. Sphere: radius 0.10m, centered at y = 0.10m
                sphereRenderable = ShapeFactory.makeSphere(
                        0.10f,
                        new Vector3(0.0f, 0.10f, 0.0f),
                        redMat);

                // 3. Cylinder: radius 0.08m, height 0.20m, centered at y = 0.10m
                cylinderRenderable = ShapeFactory.makeCylinder(
                        0.08f, 0.20f,
                        new Vector3(0.0f, 0.10f, 0.0f),
                        tealMat);

                // 4. Chair Components
                chairSeatRenderable = ShapeFactory.makeCube(
                        new Vector3(0.24f, 0.03f, 0.24f),
                        new Vector3(0.0f, 0.20f, 0.0f),
                        darkSlateMat);
                chairBackRenderable = ShapeFactory.makeCube(
                        new Vector3(0.24f, 0.22f, 0.03f),
                        new Vector3(0.0f, 0.325f, -0.105f),
                        darkSlateMat);
                chairLegRenderable = ShapeFactory.makeCylinder(
                        0.015f, 0.20f,
                        new Vector3(0.0f, 0.10f, 0.0f),
                        woodLegMat);

                // 5. Table Components
                tableTopRenderable = ShapeFactory.makeCube(
                        new Vector3(0.44f, 0.03f, 0.32f),
                        new Vector3(0.0f, 0.26f, 0.0f),
                        walnutMat);
                tableLegRenderable = ShapeFactory.makeCylinder(
                        0.02f, 0.26f,
                        new Vector3(0.0f, 0.13f, 0.0f),
                        woodLegMat);

                // 6. Plant Components
                plantPotRenderable = ShapeFactory.makeCylinder(
                        0.09f, 0.14f,
                        new Vector3(0.0f, 0.07f, 0.0f),
                        terracottaMat);
                plantSoilRenderable = ShapeFactory.makeCylinder(
                        0.085f, 0.02f,
                        new Vector3(0.0f, 0.135f, 0.0f),
                        soilMat);
                plantStemRenderable = ShapeFactory.makeCylinder(
                        0.014f, 0.14f,
                        new Vector3(0.0f, 0.20f, 0.0f),
                        stemMat);
                plantLeavesRenderable = ShapeFactory.makeSphere(
                        0.09f,
                        new Vector3(0.0f, 0.30f, 0.0f),
                        leafMat);

                isReady = true;
                Log.d(TAG, "All 3D procedural renderables successfully compiled and cached.");
                if (onReadyCallback != null) {
                    onReadyCallback.run();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error compiling procedural 3D materials: ", e);
            }
        });
    }

    public boolean isReady() {
        return isReady;
    }

    /**
     * Attaches genuine 3D geometry to the given TransformableNode based on object type.
     */
    public void populateNode(TransformableNode transformableNode, ArObjectType objectType) {
        if (!isReady) {
            Log.w(TAG, "Factory not yet ready when populating node");
            return;
        }

        switch (objectType) {
            case CUBE:
                transformableNode.setRenderable(cubeRenderable);
                break;

            case SPHERE:
                transformableNode.setRenderable(sphereRenderable);
                break;

            case CYLINDER:
                transformableNode.setRenderable(cylinderRenderable);
                break;

            case CHAIR:
                transformableNode.setRenderable(chairSeatRenderable);

                // Backrest
                Node backrest = new Node();
                backrest.setParent(transformableNode);
                backrest.setRenderable(chairBackRenderable);

                // 4 Legs
                createSubNode(transformableNode, chairLegRenderable, new Vector3(-0.095f, 0.0f, -0.095f));
                createSubNode(transformableNode, chairLegRenderable, new Vector3(0.095f, 0.0f, -0.095f));
                createSubNode(transformableNode, chairLegRenderable, new Vector3(-0.095f, 0.0f, 0.095f));
                createSubNode(transformableNode, chairLegRenderable, new Vector3(0.095f, 0.0f, 0.095f));
                break;

            case TABLE:
                transformableNode.setRenderable(tableTopRenderable);

                // 4 Table Legs
                createSubNode(transformableNode, tableLegRenderable, new Vector3(-0.19f, 0.0f, -0.13f));
                createSubNode(transformableNode, tableLegRenderable, new Vector3(0.19f, 0.0f, -0.13f));
                createSubNode(transformableNode, tableLegRenderable, new Vector3(-0.19f, 0.0f, 0.13f));
                createSubNode(transformableNode, tableLegRenderable, new Vector3(0.19f, 0.0f, 0.13f));
                break;

            case PLANT:
                transformableNode.setRenderable(plantPotRenderable);

                // Soil
                createSubNode(transformableNode, plantSoilRenderable, Vector3.zero());

                // Stem
                createSubNode(transformableNode, plantStemRenderable, Vector3.zero());

                // Foliage canopy
                createSubNode(transformableNode, plantLeavesRenderable, Vector3.zero());
                break;
        }
    }

    private void createSubNode(Node parent, ModelRenderable renderable, Vector3 localPosition) {
        Node child = new Node();
        child.setParent(parent);
        child.setLocalPosition(localPosition);
        child.setRenderable(renderable);
    }
}
