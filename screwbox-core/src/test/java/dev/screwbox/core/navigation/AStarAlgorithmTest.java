package dev.screwbox.core.navigation;

import dev.screwbox.core.graphics.Offset;
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
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 0, 1, 5));

        Offset start = Offset.at(0, 0);
        Offset end = Offset.at(4, 4);

        final var graph = graphForGrid(grid);
        List<Offset> path = algorithm.findPath(graph, start, end);

        assertThat(path).isEmpty();
    }

    //TODO fixup tests
    private static Graph<Offset> graphForGrid(Grid grid) {
        final var graph = new Graph<Offset>() {

            @Override
            public List<Offset> adjacentNodes(Offset node) {
                return grid.reachableNeighbors(node);
            }

            @Override
            public double traversalCost(Offset start, Offset end) {
                return start.distanceTo(end);
            }
        };
        return graph;
    }

    @Test
    void findPath_pathPresent_returnsShortestPath() {
        Grid grid = new Grid($$(0, 0, 5, 5), 1);
        grid.blockArea($$(2, 2, 2, 2));
        grid.blockArea($$(2, 1, 1, 1));

        Offset start = Offset.at(0, 0);
        Offset end = Offset.at(4, 4);

        final var graph = graphForGrid(grid);
        List<Offset> path = algorithm.findPath(graph, start, end);

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
