package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.List;

public interface PathfindingAlgorithm {

    List<GridNode> findPath(Grid raster, GridNode start, GridNode end);

}
