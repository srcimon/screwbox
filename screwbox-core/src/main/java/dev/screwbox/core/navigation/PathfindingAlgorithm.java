package dev.screwbox.core.navigation;

import dev.screwbox.core.graphics.Offset;

import java.util.List;

public interface PathfindingAlgorithm {

    List<Offset> findPath(Grid grid, Offset start, Offset end);

}
