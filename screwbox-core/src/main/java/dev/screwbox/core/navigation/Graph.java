package dev.screwbox.core.navigation;

import dev.screwbox.core.Vector;

import java.util.List;

/**
 * Graph used for pathfinding using a {@link PathfindingAlgorithm}.
 *
 * @since 3.12.0
 */
public interface Graph<T> {

    /**
     * Returns a list of all adjacent nodes to specified node.
     */
    List<T> adjacentNodes(T node);

    /**
     * Returns the relative cost of traversal from start to end. Cost should always be returned by implementations,
     * even when there is no connection between start and end. Can
     */
    double traversalCost(T start, T end);

    /**
     * Translates world position to graph node.
     */
    T toGraph(Vector position);

    /**
     * Translates graph node to world position.
     */
    Vector toPosition(T node);

    /**
     * Returns {@code true} if the specified node exists within the graph.
     */
    boolean nodeExists(T node);
}
