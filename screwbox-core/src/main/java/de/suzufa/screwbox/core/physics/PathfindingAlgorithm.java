package de.suzufa.screwbox.core.physics;

import java.util.List;

import de.suzufa.screwbox.core.physics.Grid.Node;

public interface PathfindingAlgorithm {

    List<Node> findPath(Grid grid, Node start, Node end);

}
