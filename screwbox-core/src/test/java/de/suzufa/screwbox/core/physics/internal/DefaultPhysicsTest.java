package de.suzufa.screwbox.core.physics.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.PathfindingCallback;

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
        updateGrid();

        Path path = physics.findPath($(0, 0), $(9, 9)).get();

        assertThat(path.start()).isEqualTo($(0, 0));
        assertThat(path.end()).isEqualTo($(9, 9));

        assertThat(path.nodeCount()).isEqualTo(8);
    }

    @Test
    void findPathAsync_noPathFound_callsCallback() {
        updateGrid();

        var callback = mock(PathfindingCallback.class);
        physics.findPathAsync($(-5, -5), $(9, 9), callback);

        verify(callback, timeout(1000)).onPathNotFound();

    }

    @Test
    void findPathAsync_pathFound_callsCallback() {
        updateGrid();

        var callback = mock(PathfindingCallback.class);
        physics.findPathAsync($(1, 1), $(9, 9), callback);

        verify(callback, timeout(1000)).onPathFound(any());

    }

    private void updateGrid() {
        Grid grid = new Grid($$(0, 0, 10, 10), 2, false);

        physics.updatePathfindingGrid(grid);
    }
}
