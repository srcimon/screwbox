package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.ArrayList;
import java.util.List;

public class STOLENBACKUP {

    public static boolean IsWalkable(int[][] map, RasterPoint point) {
        if (point.y < 0 || point.y > map[0].length - 1)
            return false;
        if (point.x < 0 || point.x > map.length - 1)
            return false;
        return map[point.x][point.y] == 0;
    }

    public static List<RasterPoint> FindNeighbors(int[][] map, RasterPoint point) {
        List<RasterPoint> neighbors = new ArrayList<>();
        RasterPoint downLeft = point.offset(-1, 1);
        RasterPoint downRight = point.offset(1, 1);
        RasterPoint upLeft = point.offset(-1, -1);
        RasterPoint upRight = point.offset(1, -1);
        RasterPoint down = point.offset(0, 1);
        RasterPoint up = point.offset(0, -1);
        RasterPoint left = point.offset(-1, 0);
        RasterPoint right = point.offset(1, 0);
        if (IsWalkable(map, down))
            neighbors.add(down);
        if (IsWalkable(map, up))
            neighbors.add(up);
        if (IsWalkable(map, left))
            neighbors.add(left);
        if (IsWalkable(map, right))
            neighbors.add(right);
        if (IsWalkable(map, downRight) && IsWalkable(map, down) && IsWalkable(map, right))
            neighbors.add(downRight);
        if (IsWalkable(map, downLeft) && IsWalkable(map, down) && IsWalkable(map, left))
            neighbors.add(downLeft);
        if (IsWalkable(map, upLeft) && IsWalkable(map, up) && IsWalkable(map, left))
            neighbors.add(upLeft);
        if (IsWalkable(map, upRight) && IsWalkable(map, up) && IsWalkable(map, right))
            neighbors.add(upRight);
        return neighbors;
    }

    public static List<RasterPoint> FindPath(int[][] map, RasterPoint start, RasterPoint end) {
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
