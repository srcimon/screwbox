package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class GridTest {

    @Test
    void newInstance_widthNegative_throwsException() {
        assertThatThrownBy(() -> new Grid(-5, 0, 4, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Width must have value above zero");
    }

    @Test
    void newInstance_heightZero_throwsException() {
        assertThatThrownBy(() -> new Grid(2, 0, 4, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Height must have value above zero");
    }

    @Test
    void newInstance_gridSizeZero_throwsException() {
        assertThatThrownBy(() -> new Grid(1, 4, 0, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GridSize must have value above zero");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        var grid = new Grid(400, 200, 20, false);

        assertThat(grid.allNodes())
                .hasSize(200)
                .allMatch(grid::isFree);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }
}
