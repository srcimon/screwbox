
package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.navigation.NavigationSystem;
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
     * Sets the maximum number of nodes for a pathfinding grid that will use caching. Default value is 40k.
     * Caching reduces the costs for pathfinding but increases the cost of updating the pathfinding region.
     * Caching will reduce cost of pathfinding by 10 to 30 percent.
     *
     * @since 3.12.0
     */
    Navigation setGraphCachingNodeLimit(long nodeLimit);

    /**
     * Returns the current maximum number of nodes for a pathfinding grid that will use caching.
     *
     * @see #setGraphCachingNodeLimit(long)
     * @since 3.12.0
     */
    long graphCachingNodeLimit();

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
     * Sets the region for pathfinding and all obstacles that should be avoided when finding paths. Will automatically
     * expand the region to match the {@link #cellSize()}. Can be automated by using the {@link NavigationSystem}.
     *
     * @see Environment#enableNavigation()
     * @since 3.12.0
     */
    Navigation setNavigationRegion(Bounds region, List<Bounds> obstacles);

    /**
     * Returns the current navigation region.
     *
     * @see #setNavigationRegion(Bounds, List)
     * @since 3.12.0
     */
    Bounds navigationRegion();

    /**
     * Searches a path within the {@link #navigationRegion()}. Avoids all obstacles within the region.
     * Will be empty when there is no path between the two point, or when one of the points ist outside the region.
     * Will also be empty when no {@link #navigationRegion()} has been set.
     */
    Optional<Path> findPath(Vector start, Vector end);

    /**
     * Searches a path within a custom graph using the specified algorithm. This allows other navigation systems than
     * using grids, e.g. your own waypoint system.
     *
     * @since 3.12.0
     */
    <T> Optional<Path> findPath(Vector start, Vector end, Graph<T> graph, PathfindingAlgorithm<T> algorithm);

    /**
     * Raycast within the game world. Raycasting checks for line intersections and can be useful for ground detection
     * and automating enemy ai.
     */
    RaycastBuilder raycastFrom(Vector position);

    /**
     * Quick api to search for {@link Entity entites} at the specified position.
     */
    SelectEntityBuilder searchAtPosition(Vector position);

    /**
     * Quick api to search for {@link Entity entites} at the specified bounds.
     */
    SelectEntityBuilder searchInArea(Bounds bounds);
}
