package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

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
}
