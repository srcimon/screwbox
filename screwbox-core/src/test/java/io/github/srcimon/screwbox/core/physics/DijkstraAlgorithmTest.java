package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

class DijkstraAlgorithmTest {

    DijkstraAlgorithm algorithm;

    @BeforeEach
    void beforeEach() {
        algorithm = new DijkstraAlgorithm();
    }

    @Test
    void findPath_noPath_returnsEmpty() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 0, 1, 5));

        Grid.Node start = grid.nodeAt(0, 0);
        Grid.Node end = grid.nodeAt(4, 4);

        List<Grid.Node> path = algorithm.findPath(grid, start, end);

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_pathPresent_returnsShortestPath() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 2, 2, 2));

        Grid.Node start = grid.nodeAt(0, 0);
        Grid.Node end = grid.nodeAt(4, 4);

        List<Grid.Node> path = algorithm.findPath(grid, start, end);

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
