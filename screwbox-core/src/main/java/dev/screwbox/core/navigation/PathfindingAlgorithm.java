package dev.screwbox.core.navigation;

import java.util.List;

/**
 * Algorithm used to find a path within a {@link Graph}.
 *
 * @since 3.12.0
 */
public interface PathfindingAlgorithm<T> {

    /**
     * Returns a path of all nodes from start to end. Will be empty when no path was found.
     */
    List<T> findPath(Graph<T> graph, T start, T end);

}
