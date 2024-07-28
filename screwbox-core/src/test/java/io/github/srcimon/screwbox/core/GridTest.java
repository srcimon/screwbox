package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

class GridTest {

    @Test
    void clearedInstance_someFieldsBlocked_returnsEmptyGrid() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        Grid grid = new Grid(area, 2);
        grid.block(1, 2);

        Grid result = grid.clearedInstance();

        assertThat(result.isBlocked(1, 2)).isFalse();
        assertThat(result.area()).isEqualTo(area);
    }

    @Test
    void newInstance_areaNull_throwsException() {
        assertThatThrownBy(() -> new Grid(null, 4))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("grid area must not be null");
    }

    @Test
    void newInstance_gridSizeZero_throwsException() {
        Bounds area = Bounds.max();
        assertThatThrownBy(() -> new Grid(area, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("grid size must be positive");
    }

    @Test
    void newInstance_invalidAreaOriginX_throwsException() {
        Bounds area = Bounds.atOrigin(1, 0, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("area origin x should be dividable by grid size.");
    }

    @Test
    void newInstance_invalidAreaOriginY_throwsException() {
        Bounds area = Bounds.atOrigin(-32, 4, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("area origin y should be dividable by grid size.");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        Bounds area = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = new Grid(area, 20, false);

        assertThat(grid.nodes())
                .hasSize(200)
                .allMatch(grid::isFree);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }

    @Test
    void reachableNeighbors_noDiagonalMovement_returnsNeighbours() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16, false);

        assertThat(grid.reachableNeighbors(grid.nodeAt(1, 1)))
                .hasSize(4)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(1, 0))
                .contains(grid.nodeAt(1, 2));
    }

    @Test
    void reachableNeighbors_diagonalMovement_returnsNeighbours() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.reachableNeighbors(grid.nodeAt(1, 1)))
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
    void reachableNeighbors_onEdge_returnsNeighboursInGrid() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.reachableNeighbors(grid.nodeAt(0, 0)))
                .hasSize(3)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(1, 0));
    }

    @Test
    void backtrack_noParent_returnsNoNodes() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = new Grid(area, 16);
        Grid.Node node = grid.nodeAt(0, 0);

        assertThat(grid.backtrack(node)).isEmpty();
    }

    @Test
    void backtrack_parentPresent_returnsNodesPath() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = new Grid(area, 16);
        Grid.Node first = grid.nodeAt(0, 0);

        List<Grid.Node> secondGeneration = grid.reachableNeighbors(first);
        Grid.Node second = secondGeneration.getFirst();

        List<Grid.Node> thirdGeneration = grid.reachableNeighbors(second);
        Grid.Node third = thirdGeneration.getFirst();

        List<Grid.Node> path = grid.backtrack(third);

        assertThat(path).containsExactlyInAnyOrder(second, third);
    }

    @Test
    void toGrid_translatesVectorToGrid() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid(area, 16);

        Grid.Node node = grid.toGrid(Vector.$(192, -64));

        assertThat(node).isEqualTo(grid.nodeAt(11, -2));
    }

    @Test
    void worldPosition_translatesNodeFromGridToWorld() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid(area, 16);

        Grid.Node node = grid.toGrid(Vector.$(192, -64));
        Vector vector = grid.worldPosition(node);

        assertThat(vector).isEqualTo(Vector.$(200, -56));
    }

    @Test
    void blockArea_areaInGrid_blocksGridArea() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockArea(Bounds.$$(3, 2, 2, 3));

        assertThat(grid.isFree(0, 0)).isFalse();
        assertThat(grid.isFree(0, 1)).isFalse();
        assertThat(grid.isFree(1, 0)).isFalse();
        assertThat(grid.isFree(1, 1)).isFalse();
        assertThat(grid.isFree(2, 0)).isTrue();
        assertThat(grid.isFree(2, 1)).isTrue();
        assertThat(grid.isFree(0, 2)).isTrue();
        assertThat(grid.isFree(1, 2)).isTrue();
        assertThat(grid.isFree(2, 2)).isTrue();
    }

    @Test
    void blockedNeighbors_someNeighborsBlocked_returnsBlockedOnes() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockArea(Bounds.$$(3, 2, 8, 8));

        assertThat(grid.blockedNeighbors(grid.nodeAt(1, 2)))
                .hasSize(3)
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(2, 2));
    }

    @Test
    void blockedNeighbors_noDiagonalSearch_returnsBlockedOnes() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4, false);

        grid.blockArea(Bounds.$$(3, 2, 8, 8));

        assertThat(grid.blockedNeighbors(grid.nodeAt(1, 2)))
                .hasSize(2)
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(2, 2));
    }

    @Test
    void blockAt_positionInGrid_blocksNodeAtPosition() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockAt(Vector.$(5, 5));

        assertThat(grid.isBlocked(1, 1)).isTrue();
        assertThat(grid.blockedCount()).isEqualTo(1);
    }

    @Test
    void freeAt_positionInGrid_freesPosition() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.blockArea(area);

        grid.freeAt(Vector.$(5, 5));

        assertThat(grid.isFree(1, 1)).isTrue();
        assertThat(grid.blockedCount()).isEqualTo(8);
    }

    @Test
    void nodeAt_returnsNodeAtPosition() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        Grid.Node node = grid.nodeAt(3, 4);

        assertThat(node.x()).isEqualTo(3);
        assertThat(node.y()).isEqualTo(4);
        assertThat(node).hasToString("Node [x=3, y=4]");
    }

    @Test
    void worldArea_nodeInGrid_returnsAreaInWorld() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        var result = grid.worldArea(grid.nodeAt(3, 3));
        assertThat(result).isEqualTo(Bounds.$$(12, 12, 4, 4));
    }

    @Test
    void worldArea_nodeOutOfGrid_returnsAreaInWorld() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        var result = grid.worldArea(grid.nodeAt(30, 30));
        assertThat(result).isEqualTo(Bounds.$$(120, 120, 4, 4));
    }

    @Test
    void blockedCount_someBlocked_returnsCount() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.block(1, 1);
        grid.block(2, 1);

        assertThat(grid.blockedCount()).isEqualTo(2);
    }

    @Test
    void freeCount_someBlocked_returnsCount() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.block(1, 1);
        grid.block(2, 1);

        assertThat(grid.freeCount()).isEqualTo(7);
    }

    @Test
    void freeArea_someBlocked_freesArea() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.block(0, 0);
        grid.block(0, 1);

        grid.freeArea(Bounds.$$(0, 0, 2, 2));

        assertThat(grid.isFree(0, 0)).isTrue();
        assertThat(grid.isFree(0, 1)).isFalse();
    }

    @Test
    void block_nodeNotInGrid_doesntBlock() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        Grid.Node node = grid.nodeAt(6, 6);
        grid.block(node);

        assertThat(grid.blockedCount()).isZero();
    }

    @Test
    void block_nodeInGrid_blocksNode() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        Grid.Node node = grid.nodeAt(1, 2);
        grid.block(node);

        assertThat(grid.isBlocked(node)).isTrue();
    }

    @Test
    void nodeCount_3x3area_returns9() {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        assertThat(grid.nodeCount()).isEqualTo(9);
    }

    @Test
    void neighbors_positionOutsideOfGrid_isEmpty() {
        var grid = new Grid(Bounds.$$(0, 0, 12, 12), 4);

        var neightbors = grid.neighbors(grid.nodeAt(-4, -4));

        assertThat(neightbors).isEmpty();
    }

    @Test
    void neighbors_positionInsideOfGrid_returnsNeighbors() {
        var grid = new Grid(Bounds.$$(0, 0, 12, 12), 2);

        var neightbors = grid.neighbors(grid.nodeAt(2, 2));

        assertThat(neightbors).containsExactly(
                grid.nodeAt(2, 3),
                grid.nodeAt(2, 1),
                grid.nodeAt(1, 2),
                grid.nodeAt(3, 2),
                grid.nodeAt(1, 3),
                grid.nodeAt(3, 3),
                grid.nodeAt(1, 1),
                grid.nodeAt(3, 1));
    }

    @ParameterizedTest
    @CsvSource({
            "4, 2, 3.0",
            "1, 2, 0.0",
            "2, 3, 1.41"})
    void distance_returnsDistanceBetweenNodes(int x, int y, double distance) {
        Bounds area = Bounds.$$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        Grid.Node node = grid.nodeAt(1, 2);

        Grid.Node other = grid.nodeAt(x, y);

        assertThat(node.distance(other)).isEqualTo(distance, offset(0.01));
    }
}