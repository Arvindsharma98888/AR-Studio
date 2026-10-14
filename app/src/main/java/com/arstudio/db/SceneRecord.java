package com.arstudio.db;

/**
 * Entity representing a persistent AR Scene record.
 * Demonstrates Model architecture for database persistence.
 */
public class SceneRecord {
    private long id;
    private String sceneName;
    private long timestamp;
    private int objectCount;

    public SceneRecord(long id, String sceneName, long timestamp, int objectCount) {
        this.id = id;
        this.sceneName = sceneName;
        this.timestamp = timestamp;
        this.objectCount = objectCount;
    }

    public SceneRecord(String sceneName, long timestamp, int objectCount) {
        this(-1, sceneName, timestamp, objectCount);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }
}
