
package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Offset;

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

    /**
     * Set the currently used {@link PathfindingAlgorithm}. {@link AStarAlgorithm}
     * is the default value.
     *
     * @see #pathfindingAlgorithm()
     * @see DijkstraAlgorithm
     * @see AStarAlgorithm
     */
    Navigation setPathfindingAlgorithm(PathfindingAlgorithm<Offset> algorithm);

    /**
     * Returns the currently used {@link PathfindingAlgorithm}.
     * {@link AStarAlgorithm} is the default algorithm used for pathfinding.
     *
     * @see #setPathfindingAlgorithm(PathfindingAlgorithm)
     */
    PathfindingAlgorithm<Offset> pathfindingAlgorithm();

    /**
     * Finds a {@link Path} between specified start and end position. Will be empty if there is no {@link Path}.
     * Requires a {@link Grid}. Searches only within the {@link Grid}.
     *
     * @see Environment#enablePhysics()
     * @see #setGrid(Grid)
     * @see #findPath(Vector, Vector, Grid)
     */
    Optional<Path> findPath(Vector start, Vector end);

    Optional<Path> findPath(Vector start, Vector end, Graph<Offset> graph);

    /**
     * Sets the {@link Grid} that is currently used to snap objects and find paths.
     *
     * @see #findPath(Vector, Vector)
     * @see #grid()
     */
    Navigation setGrid(Grid grid);

    /**
     * Returns the {@link Grid} that is currently used to snap objects and find paths.
     *
     * @see #findPath(Vector, Vector)
     * @see #setGrid(Grid)
     */
    Optional<Grid> grid();

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);
}
