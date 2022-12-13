package de.suzufa.screwbox.core.physics;

import static de.suzufa.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;

class AStarAlgorithmTest {

    AStarAlgorithm algorithm;

    @BeforeEach
    void beforeEach() {
        algorithm = new AStarAlgorithm();
    }

    @Test
    void findPath_noPath_returnsEmpty() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 0, 1, 5));

        Node start = grid.nodeAt(0, 0);
        Node end = grid.nodeAt(4, 4);

        List<Node> path = algorithm.findPath(grid, start, end);

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_pathPresent_returnsShortestPath() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 2, 2, 2));

        Node start = grid.nodeAt(0, 0);
        Node end = grid.nodeAt(4, 4);

        List<Node> path = algorithm.findPath(grid, start, end);

        assertThat(path).containsExactly(
                grid.nodeAt(1, 1),
                grid.nodeAt(2, 1),
                grid.nodeAt(3, 1),
                grid.nodeAt(4, 1),
                grid.nodeAt(4, 2),
                grid.nodeAt(4, 3),
                grid.nodeAt(4, 4));
    }
}
