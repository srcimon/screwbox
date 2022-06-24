package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.Grid.Node;

class GridTest {

    @Test
    void newInstance_widthNegative_throwsException() {
        assertThatThrownBy(() -> new Grid(null, 4, true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Grid area must not be null");
    }

    @Test
    void newInstance_gridSizeZero_throwsException() {
        Bounds area = Bounds.max();
        assertThatThrownBy(() -> new Grid(area, 0, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GridSize must have value above zero");
    }

    @Test
    void newInstance_invalidAreaOriginX_throwsException() {
        Bounds area = Bounds.atOrigin(1, 0, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Area origin x should be dividable by grid size.");
    }

    @Test
    void newInstance_invalidAreaOriginY_throwsException() {
        Bounds area = Bounds.atOrigin(-32, 4, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Area origin y should be dividable by grid size.");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        Bounds area = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = new Grid(area, 20, false);

        assertThat(grid.allNodes())
                .hasSize(200)
                .allMatch(grid::isFree);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }

    @Test
    void blockArea_areaInGrid_blocksGridArea() {
        Bounds area = Bounds.atOrigin(-32, -32, 64, 64);

        var grid = new Grid(area, 16, false);

        grid.blockArea(Bounds.atOrigin(-16, -16, 32, 32));

        assertThat(grid.isFree(0, 0)).isTrue();
        assertThat(grid.isFree(1, 1)).isFalse();
        assertThat(grid.isFree(2, 2)).isFalse();
        assertThat(grid.isFree(3, 3)).isTrue();
    }

    @Test
    void findNeighbors_noDiagonalMovement_returnsNeighbours() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16, false);

        assertThat(grid.findNeighbors(grid.nodeAt(1, 1)))
                .hasSize(4)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(1, 0))
                .contains(grid.nodeAt(1, 2));
    }

    @Test
    void findNeighbors_diagonalMovement_returnsNeighbours() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16, true);

        assertThat(grid.findNeighbors(grid.nodeAt(1, 1)))
                .hasSize(8)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(1, 0))
                .contains(grid.nodeAt(1, 2))
                .contains(grid.nodeAt(0, 0))
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 0))
                .contains(grid.nodeAt(2, 2));
    }

    @Test
    void findNeighbors_onEdge_returnsNeighboursInGrid() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16, true);

        assertThat(grid.findNeighbors(grid.nodeAt(0, 0)))
                .hasSize(3)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(1, 0));
    }

    @Test
    void backtrackPath_returnsNodesPath() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = new Grid(area, 16, true);
        Node first = grid.nodeAt(0, 0);
        List<Node> secondGeneration = grid.findNeighbors(first);

        Node second = secondGeneration.get(0);
        List<Node> thirdGeneration = grid.findNeighbors(second);
        Node third = thirdGeneration.get(0);

        List<Node> path = third.backtrackPath();

        assertThat(path).containsExactlyInAnyOrder(second, third);
    }
}
