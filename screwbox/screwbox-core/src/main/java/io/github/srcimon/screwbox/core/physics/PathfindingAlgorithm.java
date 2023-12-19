package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Grid;

import java.util.List;

public interface PathfindingAlgorithm {

    List<Grid.Node> findPath(Grid grid, Grid.Node start, Grid.Node end);

}
