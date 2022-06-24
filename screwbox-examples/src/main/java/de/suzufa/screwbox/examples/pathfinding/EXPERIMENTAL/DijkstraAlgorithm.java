package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<RasterPoint> findPath(Raster raster, RasterPoint start, RasterPoint end) {
        var points = FindPath(raster, start, end);
        if (points == null) {
            return Collections.emptyList();
        }
        return points;
    }

    private List<RasterPoint> FindPath(Raster map, RasterPoint start, RasterPoint end) {
        boolean finished = false;
        List<RasterPoint> used = new ArrayList<>();
        used.add(start);
        while (!finished) {
            List<RasterPoint> newOpen = new ArrayList<>();
            for (int i = 0; i < used.size(); ++i) {
                RasterPoint point = used.get(i);
                for (RasterPoint neighbor : map.FindNeighbors(point)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for (RasterPoint point : newOpen) {
                used.add(point);
                if (end.equals(point)) {
                    finished = true;
                    break;
                }
            }

            if (!finished && newOpen.isEmpty())
                return null;
        }

        List<RasterPoint> path = new ArrayList<>();
        RasterPoint point = used.get(used.size() - 1);
        while (point.previous != null) {
            path.add(0, point);
            point = point.previous;
        }
        return path;
    }

}
