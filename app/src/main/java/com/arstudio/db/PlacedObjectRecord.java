package com.arstudio.db;

/**
 * Entity representing an individual 3D object placed within an AR Scene.
 */
public class PlacedObjectRecord {
    private long id;
    private long sceneId;
    private String objectType;
    private float posX;
    private float posY;
    private float posZ;
    private float rotY;
    private float scale;

    public PlacedObjectRecord(long id, long sceneId, String objectType, float posX, float posY, float posZ, float rotY, float scale) {
        this.id = id;
        this.sceneId = sceneId;
        this.objectType = objectType;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotY = rotY;
        this.scale = scale;
    }

    public PlacedObjectRecord(long sceneId, String objectType, float posX, float posY, float posZ, float rotY, float scale) {
        this(-1, sceneId, objectType, posX, posY, posZ, rotY, scale);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getSceneId() { return sceneId; }
    public String getObjectType() { return objectType; }
    public float getPosX() { return posX; }
    public float getPosY() { return posY; }
    public float getPosZ() { return posZ; }
    public float getRotY() { return rotY; }
    public float getScale() { return scale; }
}
