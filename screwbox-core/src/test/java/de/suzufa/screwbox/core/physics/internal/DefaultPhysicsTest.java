package de.suzufa.screwbox.core.physics.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.Grid;

class DefaultPhysicsTest {

    DefaultPhysics physics;

    @BeforeEach
    void beforeEach() {
        physics = new DefaultPhysics(null);
    }

    @Test
    void findPath_noGrid_throwsException() {
        Vector start = $(0, 0);
        Vector end = $(2, 5);

        assertThatThrownBy(() -> physics.findPath(start, end))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No grid for pathfinding present.");
    }

    @Test
    void findPath_gridPresent_replacesStartEndEndPositions() {
        Grid grid = new Grid($$(0, 0, 10, 10), 2, false);

        physics.updatePathfindingGrid(grid);

        Path path = physics.findPath($(0, 0), $(9, 9)).get();

        assertThat(path.start()).isEqualTo($(0, 0));
        assertThat(path.end()).isEqualTo($(9, 9));

        assertThat(path.nodeCount()).isEqualTo(8);
    }
}
