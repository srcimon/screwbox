package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GridTest {

    @Test
    void newInstance_areaNull_throwsException() {
        assertThatThrownBy(() -> new Grid(null, 4))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("grid bounds must not be null");
    }

    @Test
    void newInstance_cellSizeZero_throwsException() {
        Bounds area = Bounds.max();
        assertThatThrownBy(() -> new Grid(area, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cell size must be positive (actual value: 0)");
    }

    @Test
    void newInstance_invalidAreaOriginX_throwsException() {
        Bounds area = Bounds.atOrigin(1, 0, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("bounds should fit cell size");
    }

    @Test
    void newInstance_invalidAreaOriginY_throwsException() {
        Bounds area = Bounds.atOrigin(-32, 4, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("bounds should fit cell size");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        Bounds area = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = new Grid(area, 20);

        assertThat(grid.nodes())
                .hasSize(200)
                .allMatch(grid::isFree);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }

    @Test
    void freeAdjacentNodes_noneBlocked_returnsAdjacentNodes() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.freeAdjacentNodes(Offset.at(1, 1)))
                .hasSize(4)
                .contains(Offset.at(0, 1))
                .contains(Offset.at(2, 1))
                .contains(Offset.at(1, 0))
                .contains(Offset.at(1, 2));
    }

    @Test
    void freeSurroundingNodes_noneBlocked_returnsSurroundingNodes() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.freeSurroundingNodes(Offset.at(1, 1)))
                .hasSize(8)
                .contains(Offset.at(0, 1))
                .contains(Offset.at(2, 1))
                .contains(Offset.at(1, 0))
                .contains(Offset.at(1, 2))
                .contains(Offset.at(0, 0))
                .contains(Offset.at(0, 1))
                .contains(Offset.at(2, 0))
                .contains(Offset.at(2, 2));
    }

    @Test
    void freeSurroundingNodes_onEdge_returnsOnlyTheOnesThatResideInTheGrid() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.freeSurroundingNodes(Offset.at(0, 0)))
                .hasSize(3)
                .contains(Offset.at(0, 1))
                .contains(Offset.at(1, 1))
                .contains(Offset.at(1, 0));
    }

    @Test
    void toGrid_translatesVectorToOffset() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid(area, 16);

        Offset node = grid.toGrid($(192, -64));

        assertThat(node).isEqualTo(Offset.at(11, -2));
    }

    @Test
    void toGrid_translatesNodeFromGridToWorld() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid(area, 16);

        Offset node = grid.toGrid($(192, -64));
        Vector vector = grid.toWorld(node);

        assertThat(vector).isEqualTo($(200, -56));
    }

    @Test
    void blockArea_areaInGrid_blocksGridArea() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockArea($$(3, 2, 2, 3));

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
    void blockAt_positionInGrid_blocksNodeAtPosition() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockAt($(5, 5));

        assertThat(grid.isBlocked(1, 1)).isTrue();
    }

    @Test
    void freeAt_positionInGrid_freesPosition() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.blockArea(area);

        grid.freeAt($(5, 5));

        assertThat(grid.isFree(1, 1)).isTrue();
    }

    @Test
    void nodeBoundsnodeInGrid_returnsAreaInWorld() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        var result = grid.nodeBounds(Offset.at(3, 3));
        assertThat(result).isEqualTo($$(12, 12, 4, 4));
    }

    @Test
    void nodeBounds_nodeOutOfGrid_returnsAreaInWorld() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        var result = grid.nodeBounds(Offset.at(30, 30));
        assertThat(result).isEqualTo($$(120, 120, 4, 4));
    }

    @Test
    void freeArea_someBlocked_freesArea() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.block(0, 0);
        grid.block(0, 1);

        grid.freeArea($$(0, 0, 2, 2));

        assertThat(grid.isFree(0, 0)).isTrue();
        assertThat(grid.isFree(0, 1)).isFalse();
    }

    @Test
    void block_nodeInGrid_blocksNode() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        Offset node = Offset.at(1, 2);
        grid.block(node);

        assertThat(grid.isBlocked(node)).isTrue();
    }

    @Test
    void nodeCount_3x3area_returns9() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        assertThat(grid.nodeCount()).isEqualTo(9);
    }

    @Test
    void surroundingNodes_positionOutsideOfGrid_isEmpty() {
        var grid = new Grid($$(0, 0, 12, 12), 4);

        var surroundingNodes = grid.surroundingNodes(Offset.at(-4, -4));

        assertThat(surroundingNodes).isEmpty();
    }

    @Test
    void surroundingNodes_positionInsideOfGrid_returnsNeighbors() {
        var grid = new Grid($$(0, 0, 12, 12), 2);

        var surroundingNodes = grid.surroundingNodes(Offset.at(2, 2));

        assertThat(surroundingNodes).containsExactly(
                Offset.at(2, 3),
                Offset.at(2, 1),
                Offset.at(1, 2),
                Offset.at(3, 2),
                Offset.at(1, 3),
                Offset.at(3, 3),
                Offset.at(1, 1),
                Offset.at(3, 1));
    }

    @Test
    void isBlocked_notBlocked_isFalse() {
        var grid = new Grid($$(0, 0, 10, 16), 1);
        grid.block(0, 0);
        grid.block(8, 0);
        grid.block(1, 12);
        grid.block(1, 15);

        assertThat(grid.isBlocked(2, 5)).isFalse();
        assertThat(grid.isBlocked(2, 2)).isFalse();
    }

    @Test
    void isBlocked_blocked_isTrue() {
        var grid = new Grid($$(0, 0, 10, 16), 1);
        grid.block(0, 0);
        grid.block(8, 0);
        grid.block(1, 12);
        grid.block(1, 15);

        assertThat(grid.isBlocked(0, 0)).isTrue();
        assertThat(grid.isBlocked(8, 0)).isTrue();
        assertThat(grid.isBlocked(1, 12)).isTrue();
        assertThat(grid.isBlocked(1, 15)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 0",
            "-10, 10, -1, 0",
            "-17, 50.123, -2, 3",
    })
    void findCell_validPosition_returnsCell(double x, double y, int cellX, int cellY) {
        Offset cell = Grid.findCell($(x, y), 16);

        assertThat(cell).isEqualTo(Offset.at(cellX, cellY));
    }

    @Test
    void findCell_invalidCellSize_throwsException() {
        Vector vector = $(4, 1);

        assertThatThrownBy(() -> Grid.findCell(vector, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cell size must be positive (actual value: 0)");
    }

    @Test
    void adjacentNodes_centralNode_hasFourNodes() {
        Grid grid = new Grid(Bounds.atOrigin(0, 0, 128, 128), 16);

        var adjacentNodes = grid.adjacentNodes(Offset.at(3, 3));
        assertThat(adjacentNodes).containsExactly(Offset.at(3, 4), Offset.at(3, 2), Offset.at(2, 3), Offset.at(4, 3));
    }

    @Test
    void adjacentNodes_edgeNode_hasOnlyValidNodes() {
        Grid grid = new Grid(Bounds.atOrigin(0, 0, 128, 128), 16);

        var adjacentNodes = grid.adjacentNodes(Offset.at(1, 1));
        assertThat(adjacentNodes).containsExactly(Offset.at(1, 2), Offset.at(2, 1));
    }
}