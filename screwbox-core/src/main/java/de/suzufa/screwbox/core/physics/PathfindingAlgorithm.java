package de.suzufa.screwbox.core.physics;

import java.util.List;

import de.suzufa.screwbox.core.physics.PathfindingGrid.Node;

public interface PathfindingAlgorithm {

    List<Node> findPath(PathfindingGrid grid, Node start, Node end);

}
