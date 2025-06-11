package dev.screwbox.core.physics;

import java.util.List;

public interface PathfindingAlgorithm {

    List<Grid.Node> findPath(Grid grid, Grid.Node start, Grid.Node end);

}
