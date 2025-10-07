package dev.screwbox.core.physics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

class AStarAlgorithmTest {

    AStarAlgorithm algorithm;
//TODO add ascii tilemap for better test
    @BeforeEach
    void beforeEach() {
        algorithm = new AStarAlgorithm();
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
        grid.blockArea($$(2, 1, 1, 1));

        Grid.Node start = grid.nodeAt(0, 0);
        Grid.Node end = grid.nodeAt(4, 4);

        List<Grid.Node> path = algorithm.findPath(grid, start, end);

        assertThat(path).containsExactly(
                grid.nodeAt(1, 1),
                grid.nodeAt(1, 2),
                grid.nodeAt(1, 3),
                grid.nodeAt(1, 4),
                grid.nodeAt(2, 4),
                grid.nodeAt(3, 4),
                grid.nodeAt(4, 4));
    }
}
