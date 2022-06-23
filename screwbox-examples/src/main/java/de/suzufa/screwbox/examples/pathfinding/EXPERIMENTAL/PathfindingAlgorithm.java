package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.List;

public interface PathfindingAlgorithm {

    List<RasterPoint> findPath(Raster raster, RasterPoint start, RasterPoint end);

}
