package io.github.simonbas.screwbox.core.physics;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AStarAlgorithmTest {

    AStarAlgorithm algorithm;

    @BeforeEach
    void beforeEach() {
        algorithm = new AStarAlgorithm();
    }

    @Test
    void findPath_noPath_returnsEmpty() {
        Grid grid = new Grid(Bounds.$$(0, 0, 5, 5), 1);
        grid.blockArea(Bounds.$$(2, 0, 1, 5));

        Grid.Node start = grid.nodeAt(0, 0);
        Grid.Node end = grid.nodeAt(4, 4);

        List<Grid.Node> path = algorithm.findPath(grid, start, end);

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_pathPresent_returnsShortestPath() {
        Grid grid = new Grid(Bounds.$$(0, 0, 5, 5), 1);
        grid.blockArea(Bounds.$$(2, 2, 2, 2));

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
