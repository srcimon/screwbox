package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<RasterPoint> findPath(final Raster raster, final RasterPoint start, final RasterPoint end) {
        final List<RasterPoint> used = new ArrayList<>();
        used.add(start);

        while (true) {
            final List<RasterPoint> newOpen = new ArrayList<>();
            for (var use : used) {
                for (final RasterPoint neighbor : raster.findNeighbors(use)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for (final RasterPoint point : newOpen) {
                used.add(point);
                if (end.equals(point)) {
                    return constructPath(used);
                }
            }

            if (newOpen.isEmpty()) {
                return emptyList();
            }
        }
    }

    private List<RasterPoint> constructPath(final List<RasterPoint> used) {
        final List<RasterPoint> path = new ArrayList<>();
        RasterPoint point = used.get(used.size() - 1);
        while (nonNull(point.previous())) {
            path.add(0, point);
            point = point.previous();
        }
        return path;
    }

}
