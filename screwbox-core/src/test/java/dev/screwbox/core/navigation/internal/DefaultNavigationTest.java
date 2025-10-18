package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.AStarAlgorithm;
import dev.screwbox.core.navigation.DijkstraAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultNavigationTest {

    DefaultNavigation navigation;

    @BeforeEach
    void beforeEach() {
        navigation = new DefaultNavigation(null);
    }

    @Test
    void setCellSize_negativeSize_throwsException() {
        assertThatThrownBy(() -> navigation.setCellSize(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cell size must be in range from 1 to 256 (actual value: -1)");
    }

    @Test
    void setCellSize_size10_setsCellSizeTen() {
        assertThat(navigation.cellSize()).isEqualTo(16);
        navigation.setCellSize(10);
        assertThat(navigation.cellSize()).isEqualTo(10);
    }

    @Test
    void findPath_noNavigationRegion_noPath() {
        assertThat(navigation.findPath($(0, 0), $(2, 5))).isEmpty();
    }

//    @Test
//    void findPath_gridPresent_addsStartEndEndPositions() {
//        Grid grid = new Grid($$(0, 0, 10, 10), 2);
//        navigation.setGrid(grid);
//
//        Path path = navigation.findPath($(0, 0), $(9, 9)).orElseThrow();
//
//        assertThat(path.start()).isEqualTo($(0, 0));
//        assertThat(path.end()).isEqualTo($(9, 9));
//        assertThat(path.nodeCount()).isEqualTo(6);
//    }

    @Test
    void setDiagonalMovementAllowed_false_setsMovementAllowedFalse() {
        assertThat(navigation.isDiagonalMovementAllowed()).isTrue();

        navigation.setDiagonalMovementAllowed(false);

        assertThat(navigation.isDiagonalMovementAllowed()).isFalse();
    }

//    @Test
//    void findPath_startIsBlocked_noPath() {
//        Grid grid = new Grid($$(0, 0, 10, 10), 1);
//        Vector startPoint = $(0, 0);
//        grid.block(grid.toGrid(startPoint));
//
//        var path = navigation.findPath(startPoint, $(9, 9), grid);
//
//        assertThat(path).isEmpty();
//    }

//    @Test
//    void findPath_endIsBlocked_noPath() {
//        Grid grid = new Grid($$(0, 0, 10, 10), 1);
//        Vector endPoint = $(10, 10);
//        grid.block(grid.toGrid(endPoint));
//
//        var path = navigation.findPath($(0, 0), endPoint, grid);
//
//        assertThat(path).isEmpty();
//    }

    @Test
    void pathfindingAlgorithm_algorithmNotChanged_isAStar() {
        var pathfindingAlgorithm = navigation.pathfindingAlgorithm();

        assertThat(pathfindingAlgorithm).isInstanceOf(AStarAlgorithm.class);
    }

    @Test
    void pathfindingAlgorithm_changedToDijkstra_isDijkstra() {
        navigation.setPathfindingAlgorithm(new DijkstraAlgorithm<>());

        var pathfindingAlgorithm = navigation.pathfindingAlgorithm();

        assertThat(pathfindingAlgorithm).isInstanceOf(DijkstraAlgorithm.class);
    }
}
