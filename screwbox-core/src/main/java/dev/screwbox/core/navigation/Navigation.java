
package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Offset;

import java.util.List;
import java.util.Optional;

/**
 * Check for line of sight, raycast for {@link Entity entities} and find paths for enemy movement.
 */
public interface Navigation {

    /**
     * Enables or disables diagonal movement in pathfinding.
     *
     * @since 3.12.0
     */
    Navigation setDiagonalMovementAllowed(final boolean isDiagonalMovementAllowed);

    /**
     * Returns {@code true} if diagonal movement is used in pathfinding.
     *
     * @since 3.12.0
     */
    boolean isDiagonalMovementAllowed();

    /**
     * Sets the cell size of the pathfinding grid. Default value is 16. Higher numbers mean less accuracy, lower numbers
     * mean higher cost of pathfinding. Entities bigger than cell size might also get stuck because the path is too
     * narrow. It's recommended to use the same size as your tiles in tile based games.
     *
     * @since 3.12.0
     */
    Navigation setCellSize(int cellSize);

    /**
     * Returns the current cell size of the pathfinding grid.
     *
     * @since 3.12.0
     */
    int cellSize();

    /**
     * Set the currently used {@link PathfindingAlgorithm}. {@link AStarAlgorithm}
     * is the default value.
     *
     * @see #pathfindingAlgorithm()
     * @see DijkstraAlgorithm
     * @see AStarAlgorithm
     */
    Navigation setPathfindingAlgorithm(PathfindingAlgorithm<Offset> algorithm);

    Navigation setNavigationRegion(Bounds region, List<Bounds> obstacles);

    Navigation setGraphCachingNodeLimit(long nodeLimit);

    long graphCachingNodeLimit();

    Bounds navigationRegion();

    /**
     * Returns the currently used {@link PathfindingAlgorithm}.
     * {@link AStarAlgorithm} is the default algorithm used for pathfinding.
     *
     * @see #setPathfindingAlgorithm(PathfindingAlgorithm)
     */
    PathfindingAlgorithm<Offset> pathfindingAlgorithm();

    Optional<Path> findPath(Vector start, Vector end);

    Optional<Path> findPath(Vector start, Vector end, Graph<Offset> graph);

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);
}
