
package dev.screwbox.core.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Path;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;

import java.util.Optional;

/**
 * Advanced searching for {@link Entity entities}, pathfinding, raycasting and adjusting {@link Entity entities} to a {@link Grid}.
 */
public interface Physics {

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);

    /**
     * Finds a {@link Path} between specified start and end position. Will be empty if there is no {@link Path}.
     * Requires a {@link Grid}. Searches only within the {@link Grid}.
     *
     * @see Environment#enablePhysics()
     * @see #setGrid(Grid)
     * @see #findPath(Vector, Vector, Grid)
     */
    Optional<Path> findPath(Vector start, Vector end);

    /**
     * Finds a {@link Path} between specified start and end position. Will be empty if there is no {@link Path}.
     *
     * @see #setGrid(Grid)
     * @see #findPath(Vector, Vector)
     */
    Optional<Path> findPath(Vector start, Vector end, Grid grid);

    Bounds snapToGrid(Bounds bounds);

    Vector snapToGrid(Vector position);

    /**
     * Sets the {@link Grid} that is currently used to snap objects and find paths.
     *
     * @see #snapToGrid(Bounds)
     * @see #snapToGrid(Vector)
     * @see #findPath(Vector, Vector)
     * @see #grid()
     */
    Physics setGrid(Grid grid);

    /**
     * Returns the {@link Grid} that is currently used to snap objects and find paths.
     *
     * @see #snapToGrid(Bounds)
     * @see #snapToGrid(Vector)
     * @see #findPath(Vector, Vector)
     * @see #setGrid(Grid)
     */
    Optional<Grid> grid();

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
