package de.suzufa.screwbox.core.physics.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static de.suzufa.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;

class DefaultPhysicsTest {

    DefaultPhysics physics;
    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        physics = new DefaultPhysics(null, executor);
    }

    @Test
    void findPath_noGrid_throwsException() {
        Vector start = $(0, 0);
        Vector end = $(2, 5);

        assertThatThrownBy(() -> physics.findPath(start, end))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no grid for pathfinding present");
    }

    @Test
    void findPath_gridPresent_addsStartEndEndPositions() {
        Grid grid = new Grid($$(0, 0, 10, 10), 2, false);
        physics.setGrid(grid);

        Path path = physics.findPath($(0, 0), $(9, 9)).get();

        assertThat(path.start()).isEqualTo($(0, 0));
        assertThat(path.end()).isEqualTo($(9, 9));
        assertThat(path.nodeCount()).isEqualTo(10);
    }

    @Test
    void snapToGrid_boundsNull_exception() {
        assertThatThrownBy(() -> physics.snapToGrid((Bounds) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("bounds must not be null");
    }

    @Test
    void snapToGrid_positionNull_exception() {
        assertThatThrownBy(() -> physics.snapToGrid((Vector) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("position must not be null");
    }

    @Test
    void snapToGrid_noGrid_exception() {
        Vector position = $(3, 10);

        assertThatThrownBy(() -> physics.snapToGrid(position))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no grid present");
    }

    @Test
    void snapToGrid_gridPresent_snapsVectorToGrid() {
        physics.setGrid(new Grid($$(0, 0, 16, 16), 16));

        var result = physics.snapToGrid($(3, 10));

        assertThat(result).isEqualTo($(8, 8));
    }

    @Test
    void snapToGrid_gridPresent_snapsBoundsToGrid() {
        physics.setGrid(new Grid($$(0, 0, 16, 16), 16));

        var result = physics.snapToGrid($$(3, 10, 8, 8));

        assertThat(result).isEqualTo($$(4, 4, 8, 8));
    }

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }
}
