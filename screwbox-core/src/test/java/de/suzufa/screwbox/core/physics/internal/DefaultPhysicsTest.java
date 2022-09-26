package de.suzufa.screwbox.core.physics.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
                .hasMessage("No grid for pathfinding present.");
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

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
