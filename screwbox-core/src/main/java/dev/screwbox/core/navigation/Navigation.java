
package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Offset;

import java.util.List;
import java.util.Optional;

/**
 * Advanced searching for {@link Entity entities}, pathfinding, raycasting and adjusting {@link Entity entities} to a {@link Grid}.
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

    Navigation setCellSize(int cellSize);

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
