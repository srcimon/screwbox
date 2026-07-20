package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GridTest {

    @Test
    void createByGridSize_areaNull_throwsException() {
        var size = Size.square(1);
        assertThatThrownBy(() -> Grid.createByGridSize(size, null, Boolean.class))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("grid bounds must not be null");
    }

    @Test
    void createByGridSize_cellSizeZero_throwsException() {
        Bounds area = Bounds.max();
        Size size = Size.none();
        assertThatThrownBy(() -> Grid.createByGridSize(size, area, Boolean.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("grid size must be valid");
    }

    @Test
    void createByCellSize_validArguments_createsEmptyGrid() {
        Bounds area = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = Grid.createByCellSize(Size.square(10), area, Boolean.class);

        assertThat(grid.cells())
            .hasSize(800)
            .noneMatch(grid::hasValue);

        assertThat(grid.size()).isEqualTo(Size.of(40, 20));
        assertThat(grid.cellWidth()).isEqualTo(10.0);
        assertThat(grid.cellHeight()).isEqualTo(10.0);
    }


    @Test
    void toCell_translatesVectorToOffset() {
        Bounds area = Bounds.atOrigin(32, 32, 64, 64);
        var grid = Grid.createByGridSize(Size.square(16), area, Boolean.class);

        Offset node = grid.toCell($(0, -8));

        assertThat(node).isEqualTo(Offset.at(-8, -10));
    }

    @Test
    void toCell_translatesNodeFromGridToWorld() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = Grid.createByCellSize(Size.square(16), area, Boolean.class);

        Offset node = grid.toCell($(192, -64));
        Vector vector = grid.cellPosition(node);

        assertThat(vector).isEqualTo($(200, -56));
    }

    @Test
    void cellWidth_returnsCellWidth() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = Grid.createByCellSize(Size.of(16, 8), area, Boolean.class);

        assertThat(grid.cellWidth()).isEqualTo(16.0);
    }

    @Test
    void cellHeight_returnsCellHeight() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = Grid.createByCellSize(Size.of(16, 8), area, Boolean.class);

        assertThat(grid.cellHeight()).isEqualTo(8.0);
    }

    @Test
    void fill_areaInGrid_setsValuesWithinArea() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = Grid.createByCellSize(Size.square(4), area, Boolean.class);

        grid.fill($$(3, 2, 2, 3), true);

        assertThat(grid.get(0, 0)).isTrue();
        assertThat(grid.get(0, 1)).isTrue();
        assertThat(grid.get(1, 0)).isTrue();
        assertThat(grid.get(1, 1)).isTrue();
        assertThat(grid.get(1, 1)).isTrue();
        assertThat(grid.hasValue(2, 0)).isFalse();
        assertThat(grid.hasValue(2, 1)).isFalse();
        assertThat(grid.hasValue(0, 2)).isFalse();
        assertThat(grid.hasValue(1, 2)).isFalse();
        assertThat(grid.hasValue(2, 2)).isFalse();
    }

    @Test
    void cellBounds_cellOutOfGrid_returnsBoundsInWorld() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = Grid.createByCellSize(Size.square(4), area, Boolean.class);

        var result = grid.cellBounds(Offset.at(30, 30));
        assertThat(result).isEqualTo($$(120, 120, 4, 4));
    }

    @Test
    void cellCount_3X3Area_returns9() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = Grid.createByCellSize(Size.square(4), area, Boolean.class);

        assertThat(grid.cellCount()).isEqualTo(9);
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
    void fill_emptyGrid_fillsAllsCells() {
        var grid = Grid.createByCellSize(Size.square(4), Bounds.atOrigin(4, 4, 4, 4), String.class);
        grid.fill("test");

        assertThat(grid.cells()).allMatch(cell -> grid.get(cell).equals("test"));
    }

    @Test
    void clear_cellWithinGrid_clearsCellData() {
        var grid = Grid.createByCellSize(Size.square(1), Bounds.atOrigin(4, 4, 4, 4), String.class);
        var cell = Offset.at(2, 0);
        grid.set(cell, "test");

        grid.clear(cell);

        assertThat(grid.hasValue(cell)).isFalse();
    }

    @Test
    void createByCellSize_notQuiteMatching_coversWholeArea() {
        var grid = Grid.createByCellSize(Size.square(2), Bounds.atOrigin(4, 4, 5, 4), String.class);

        assertThat(grid.toCell($(8.9, 4.1))).isEqualTo(Offset.at(2, 0));
        assertThat(grid.size()).isEqualTo(Size.of(3, 2));
    }

    @Test
    void updateBounds_validPosition_updatesBoundsAndCellSizes() {
        var grid = Grid.createByGridSize(Size.square(2), Bounds.atOrigin(4, 4, 8, 8), String.class);
        assertThat(grid.size()).isEqualTo(Size.square(2));
        assertThat(grid.cellWidth()).isEqualTo(4.0);
        assertThat(grid.cellHeight()).isEqualTo(4.0);
        assertThat(grid.bounds()).isEqualTo(Bounds.atOrigin(4, 4, 8, 8));

        grid.updateBounds(Bounds.atOrigin(10, 10, 4, 6));

        assertThat(grid.size()).isEqualTo(Size.square(2));
        assertThat(grid.cellWidth()).isEqualTo(2.0);
        assertThat(grid.cellHeight()).isEqualTo(3.0);
        assertThat(grid.bounds()).isEqualTo(Bounds.atOrigin(10, 10, 4, 6));
    }
}