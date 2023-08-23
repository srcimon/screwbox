package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;

import java.util.Optional;

/**
 * Advanced searching for entities, pathfinding, raycasting and adjusting Entites to a {@link Grid}.
 */
public interface Physics {

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);

    Optional<Path> findPath(Grid grid, Vector start, Vector end);

    Optional<Path> findPath(Vector start, Vector end);

    Physics setGrid(Grid grid);

    Grid grid();

    Bounds snapToGrid(Bounds bounds);

    Vector snapToGrid(Vector position);

    /**
     * Set the currently used {@link PathfindingAlgorithm}. {@link AStarAlgorithm}
     * is the default value.
     *
     * @see #pathfindingAlgorithm()
     * @see DijkstraAlgorithm
     * @see AStarAlgorithm
     */
    Physics setPathfindingAlgorithm(PathfindingAlgorithm algorithm);

    /**
     * Returns the currently used {@link PathfindingAlgorithm}.
     * {@link AStarAlgorithm} is the default value.
     *
     * @see #setPathfindingAlgorithm(PathfindingAlgorithm)
     */
    PathfindingAlgorithm pathfindingAlgorithm();

}
