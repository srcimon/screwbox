package dev.screwbox.core.physics;

import dev.screwbox.core.graphics.Offset;

import java.util.List;

public interface PathfindingAlgorithm {

    List<Offset> findPath(Grid grid, Offset start, Offset end);

}
