package dev.screwbox.core.navigation;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.internal.GridGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

class AStarAlgorithmTest {

    AStarAlgorithm<Offset> algorithm;

    @BeforeEach
    void beforeEach() {
        algorithm = new AStarAlgorithm<>();
    }

    @Test
    void findPath_noPath_returnsEmpty() {
        Grid<Boolean> grid = new Grid<>($$(0, 0, 5, 5), 1);
        grid.set($$(2, 0, 1, 5), true);

        Offset start = Offset.at(0, 0);
        Offset end = Offset.at(4, 4);

        List<Offset> path = algorithm.findPath(new GridGraph(grid, true), start, end);

        assertThat(path).isEmpty();
    }


    @Test
    void findPath_pathPresent_returnsShortestPath() {
        Grid<Boolean> grid = new Grid<>($$(0, 0, 5, 5), 1);
        grid.set($$(2, 2, 2, 2), true);
        grid.set($$(2, 1, 1, 1), true);

        Offset start = Offset.at(0, 0);
        Offset end = Offset.at(4, 4);

        List<Offset> path = algorithm.findPath(new GridGraph(grid, true), start, end);

        assertThat(path).containsExactly(
            Offset.at(1, 1),
            Offset.at(1, 2),
            Offset.at(1, 3),
            Offset.at(1, 4),
            Offset.at(2, 4),
            Offset.at(3, 4),
            Offset.at(4, 4));
    }
}
