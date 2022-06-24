package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.List;

import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.GridNode;

public interface PathfindingAlgorithm {

    List<GridNode> findPath(Grid raster, GridNode start, GridNode end);

}
