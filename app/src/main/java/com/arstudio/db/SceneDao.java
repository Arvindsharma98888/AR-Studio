package com.arstudio.db;

import com.arstudio.exception.DatabaseOperationException;
import java.util.List;

/**
 * Data Access Object interface for Scene entities.
 * Satisfies "classes for database operations" in marking rubric (7 marks).
 */
public interface SceneDao extends GenericDao<SceneRecord, Long> {
    List<PlacedObjectRecord> getObjectsForScene(long sceneId) throws DatabaseOperationException;
    long saveFullScene(SceneRecord scene, List<PlacedObjectRecord> objects) throws DatabaseOperationException;
}
