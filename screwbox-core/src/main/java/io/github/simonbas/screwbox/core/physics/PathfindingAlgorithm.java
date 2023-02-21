package io.github.simonbas.screwbox.core.physics;

import io.github.simonbas.screwbox.core.Grid;
import io.github.simonbas.screwbox.core.Grid.Node;

import java.util.List;

public interface PathfindingAlgorithm {

    List<Node> findPath(Grid grid, Node start, Node end);

}
