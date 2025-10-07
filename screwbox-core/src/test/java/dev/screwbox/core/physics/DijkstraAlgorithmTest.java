package dev.screwbox.core.physics;

import dev.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

class DijkstraAlgorithmTest {

    DijkstraAlgorithm algorithm;
    //TODO add ascii tilemap for better test
    @BeforeEach
    void beforeEach() {
        algorithm = new DijkstraAlgorithm();
    }

    @Test
    void findPath_noPath_returnsEmpty() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 0, 1, 5));

        Offset start = grid.nodeAt(0, 0);
        Offset end = grid.nodeAt(4, 4);

        List<Offset> path = algorithm.findPath(grid, start, end);

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_pathPresent_returnsShortestPath() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 2, 2, 2));

        Offset start = grid.nodeAt(0, 0);
        Offset end = grid.nodeAt(4, 4);

        List<Offset> path = algorithm.findPath(grid, start, end);

        assertThat(path).containsExactly(
                grid.nodeAt(0, 1),
                grid.nodeAt(0, 2),
                grid.nodeAt(0, 3),
                grid.nodeAt(1, 4),
                grid.nodeAt(2, 4),
                grid.nodeAt(3, 4),
                grid.nodeAt(4, 4));
    }
}
