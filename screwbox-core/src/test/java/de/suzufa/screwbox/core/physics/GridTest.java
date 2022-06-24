package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

class GridTest {

    @Test
    void newInstance_boundsNull_throwsException() {
        assertThatThrownBy(() -> new Grid(null, 4, true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Grid bounds must not be null.");
    }

    @Test
    void newInstance_gridSizeZero_throwsException() {
        var bounds = Bounds.atOrigin(Vector.zero(), 0, 0);

        assertThatThrownBy(() -> new Grid(bounds, 0, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GridSize must have a positive value");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        var bounds = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = new Grid(bounds, 20, false);

        assertThat(grid.allNodes())
                .hasSize(200)
                .allMatch(grid::isFree);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }
}
