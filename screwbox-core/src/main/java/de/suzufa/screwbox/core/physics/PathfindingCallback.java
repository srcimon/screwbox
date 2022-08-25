package de.suzufa.screwbox.core.physics;

import de.suzufa.screwbox.core.Path;

/**
 * Callback for asynchronous pathfinding requests.
 * 
 * @see Physics#findPathAsync(Vector, Vector, PathfindingCallback)
 */
@FunctionalInterface
public interface PathfindingCallback {

    /**
     * Is called when a path has been found. Path is never null.
     */
    void onPathFound(Path path);

    /**
     * Is called when no path was found.
     */
    default void onPathNotFound() {

    }
}
