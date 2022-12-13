package de.suzufa.screwbox.core.physics;

import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;

public interface Physics {

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);

    Optional<Path> findPath(Grid grid, Vector start, Vector end);

    Optional<Path> findPath(Vector start, Vector end);

    Physics setGrid(Grid grid);

    Physics setPathfindingAlgorithm(PathfindingAlgorithm algorithm);

    Optional<Grid> grid();

    Bounds snapToGrid(Bounds bounds);

    Vector snapToGrid(Vector position);

    /**
     * Returns the currently used {@link PathfindingAlgorithm}.
     * {@link AStarAlgorithm} is the default value.
     * 
     * @see #setPathfindingAlgorithm(PathfindingAlgorithm)
     */
    PathfindingAlgorithm pathfindingAlgorithm();

}
