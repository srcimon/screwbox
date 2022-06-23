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

    // TODO: findNeighbours is part of the raster!!!!
    // TODO: Sub Algorithm wihtout diagonal movements
    private List<RasterPoint> FindNeighbors(Raster map, RasterPoint point) {
        List<RasterPoint> neighbors = new ArrayList<>();
        RasterPoint downLeft = point.offset(-1, 1);
        RasterPoint downRight = point.offset(1, 1);
        RasterPoint upLeft = point.offset(-1, -1);
        RasterPoint upRight = point.offset(1, -1);
        RasterPoint down = point.offset(0, 1);
        RasterPoint up = point.offset(0, -1);
        RasterPoint left = point.offset(-1, 0);
        RasterPoint right = point.offset(1, 0);
        if (map.isFree(down))
            neighbors.add(down);
        if (map.isFree(up))
            neighbors.add(up);
        if (map.isFree(left))
            neighbors.add(left);
        if (map.isFree(right))
            neighbors.add(right);
        if (map.isFree(downRight) && map.isFree(down) && map.isFree(right))
            neighbors.add(downRight);
        if (map.isFree(downLeft) && map.isFree(down) && map.isFree(left))
            neighbors.add(downLeft);
        if (map.isFree(upLeft) && map.isFree(up) && map.isFree(left))
            neighbors.add(upLeft);
        if (map.isFree(upRight) && map.isFree(up) && map.isFree(right))
            neighbors.add(upRight);
        return neighbors;
    }

    private List<RasterPoint> FindPath(Raster map, RasterPoint start, RasterPoint end) {
        boolean finished = false;
        List<RasterPoint> used = new ArrayList<>();
        used.add(start);
        while (!finished) {
            List<RasterPoint> newOpen = new ArrayList<>();
            for (int i = 0; i < used.size(); ++i) {
                RasterPoint point = used.get(i);
                for (RasterPoint neighbor : FindNeighbors(map, point)) {
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
