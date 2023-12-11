package io.github.srcimon.screwbox.core.physics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.physics.AStarAlgorithm;
import io.github.srcimon.screwbox.core.physics.DijkstraAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                .hasMessage("no grid present");
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
    void findPath_startIsBlocked_noPath() {
        Grid grid = new Grid($$(0, 0, 10, 10), 1, false);
        Vector startPoint = $(0, 0);
        grid.block(grid.toGrid(startPoint));

        var path = physics.findPath(grid, startPoint, $(9, 9));

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_endIsBlocked_noPath() {
        Grid grid = new Grid($$(0, 0, 10, 10), 1, false);
        Vector endPoint = $(10, 10);
        grid.block(grid.toGrid(endPoint));

        var path = physics.findPath(grid, $(0, 0), endPoint);

        assertThat(path).isEmpty();
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
    void snapToGrid_vectorNoGrid_exception() {
        Vector position = $(3, 10);

        assertThatThrownBy(() -> physics.snapToGrid(position))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no grid present");
    }

    @Test
    void snapToGrid_boundsNoGrid_exception() {
        var bounds = $$(0, 0, 2, 2);

        assertThatThrownBy(() -> physics.snapToGrid(bounds))
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

    @Test
    void pathfindingAlgorithm_algorithmNotChanged_isAStar() {
        var pathfindingAlgorithm = physics.pathfindingAlgorithm();

        assertThat(pathfindingAlgorithm).isInstanceOf(AStarAlgorithm.class);
    }

    @Test
    void pathfindingAlgorithm_changedToDijkstra_isdijkstra() {
        physics.setPathfindingAlgorithm(new DijkstraAlgorithm());

        var pathfindingAlgorithm = physics.pathfindingAlgorithm();

        assertThat(pathfindingAlgorithm).isInstanceOf(DijkstraAlgorithm.class);
    }

}
