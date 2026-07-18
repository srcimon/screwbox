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

//TODO double check test names
class GridTest {

    @Test
    void newInstance_areaNull_throwsException() {
        assertThatThrownBy(() -> new Grid<>(null, 4))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("grid bounds must not be null");
    }

    @Test
    void newInstance_cellSizeZero_throwsException() {
        Bounds area = Bounds.max();
        assertThatThrownBy(() -> new Grid<>(area, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be positive (actual value: 0)");
    }

    @Test
    void newInstance_invalidAreaOriginX_throwsException() {
        Bounds area = Bounds.atOrigin(1, 0, 10, 10);
        assertThatThrownBy(() -> new Grid<>(area, 16))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("bounds should fit cell size");
    }

    @Test
    void newInstance_invalidAreaOriginY_throwsException() {
        Bounds area = Bounds.atOrigin(-32, 4, 10, 10);
        assertThatThrownBy(() -> new Grid<>(area, 16))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("bounds should fit cell size");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        Bounds area = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = new Grid<Boolean>(area, 20);

        assertThat(grid.cells())
            .hasSize(200)
            .noneMatch(grid::hasValue);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }


    @Test
    void toCell_translatesVectorToOffset() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid<Boolean>(area, 16);

        Offset node = grid.toCell($(192, -64));

        assertThat(node).isEqualTo(Offset.at(11, -2));
    }

    @Test
    void toCell_translatesNodeFromGridToWorld() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid<>(area, 16);

        Offset node = grid.toCell($(192, -64));
        Vector vector = grid.cellPosition(node);

        assertThat(vector).isEqualTo($(200, -56));
    }

    @Test
    void cellSize_returnsCellSize() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = new Grid<>(area, 16);

        assertThat(grid.cellSize()).isEqualTo(16);
    }

    @Test
    void set_areaInGrid_setsValuesWithinArea() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid<Boolean>(area, 4);

        grid.set($$(3, 2, 2, 3), true);

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
        var grid = new Grid<>(area, 4);

        var result = grid.cellBounds(Offset.at(30, 30));
        assertThat(result).isEqualTo($$(120, 120, 4, 4));
    }


    @Test
    void cellCount_3X3Area_returns9() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

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
}