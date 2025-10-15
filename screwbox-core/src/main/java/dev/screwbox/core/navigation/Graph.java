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

    //TODO document
    T toGraph(Vector position);

    //TODO document
    Vector toPosition(T node);

    //TODO document
    boolean nodeExists(T node);
}
