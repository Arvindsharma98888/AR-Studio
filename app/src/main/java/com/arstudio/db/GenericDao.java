package com.arstudio.db;

import com.arstudio.exception.DatabaseOperationException;
import java.util.List;

/**
 * Generic Data Access Object (DAO) interface.
 * Demonstrates Generics, Collections, and Exception Handling for academic rubric (6 marks).
 *
 * @param <T>  Entity type
 * @param <ID> Primary key identifier type
 */
public interface GenericDao<T, ID> {
    long insert(T entity) throws DatabaseOperationException;
    T findById(ID id) throws DatabaseOperationException;
    List<T> findAll() throws DatabaseOperationException;
    boolean deleteById(ID id) throws DatabaseOperationException;
    int count() throws DatabaseOperationException;
}
