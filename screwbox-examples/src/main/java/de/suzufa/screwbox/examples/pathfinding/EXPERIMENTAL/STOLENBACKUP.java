package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.ArrayList;
import java.util.List;

public class STOLENBACKUP {

    public static boolean IsWalkable(int[][] map, RasterPoint point) {
        if (point.y < 0 || point.y > map.length - 1)
            return false;
        if (point.x < 0 || point.x > map[0].length - 1)
            return false;
        return map[point.y][point.x] == 0;
    }

    public static List<RasterPoint> FindNeighbors(int[][] map, RasterPoint point) {
        List<RasterPoint> neighbors = new ArrayList<>();
        RasterPoint up = point.offset(0, 1);
        RasterPoint down = point.offset(0, -1);
        RasterPoint left = point.offset(-1, 0);
        RasterPoint right = point.offset(1, 0);
        if (IsWalkable(map, up))
            neighbors.add(up);
        if (IsWalkable(map, down))
            neighbors.add(down);
        if (IsWalkable(map, left))
            neighbors.add(left);
        if (IsWalkable(map, right))
            neighbors.add(right);
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

    public static void main(String[] args) {
        int[][] map = {
                { 0, 0, 0, 0, 0 },
                { 0, 0, 1, 0, 1 },
                { 1, 0, 0, 1, 1 },
                { 0, 0, 0, 1, 0 },
                { 1, 1, 0, 0, 1 }
        };

        RasterPoint start = new RasterPoint(0, 0, null);
        RasterPoint end = new RasterPoint(3, 4, null);
        List<RasterPoint> path = FindPath(map, start, end);
        if (path != null) {
            for (RasterPoint point : path) {
                System.out.println(point);
            }
        } else
            System.out.println("No path found");
    }
}
