package com.arstudio.exception;

/**
 * Exception thrown when raycasting fails to hit a valid tracked horizontal plane.
 */
public class SurfaceNotDetectedException extends ARCustomException {
    public SurfaceNotDetectedException(String message) {
        super("ERR_SURFACE_NOT_DETECTED", message);
    }
}
