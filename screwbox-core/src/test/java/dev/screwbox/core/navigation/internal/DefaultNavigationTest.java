package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.AStarAlgorithm;
import dev.screwbox.core.navigation.DijkstraAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
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

    @Test
    void findPath_toValidPointsWithinNavigationRegion_findsPath() {
        navigation.setCellSize(40);
        navigation.setNavigationRegion(Bounds.atOrigin(0, 0, 200, 200), List.of(Bounds.atOrigin(50, 50, 50, 50)));
        Vector start = $(10, 10);
        Vector end = $(190, 190);

        final var path = navigation.findPath(start, end);

        assertThat(path).isPresent();
        assertThat(path.get().start()).isEqualTo(start);
        assertThat(path.get().end()).isEqualTo(end);
        assertThat(path.get().nodeCount()).isEqualTo(9);
    }

    @Test
    void findPath_startIsBlocked_noPath() {
        navigation.setNavigationRegion(Bounds.atOrigin(0, 0, 200, 200), List.of(Bounds.atOrigin(0, 0, 50, 50)));

        final var path = navigation.findPath($(10, 10), $(190, 190));

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_startOutOfNavigationRegion_noPath() {
        navigation.setNavigationRegion(Bounds.atOrigin(0, 0, 200, 200), emptyList());

        final var path = navigation.findPath($(-10, -10), $(190, 190));

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_endIsBlocked_noPath() {
        navigation.setNavigationRegion(Bounds.atOrigin(0, 0, 200, 200), List.of(Bounds.atOrigin(0, 0, 50, 50)));

        final var path = navigation.findPath($(190, 190), $(10, 10));

        assertThat(path).isEmpty();
    }

    @Test
    void findPath_endOutOfNavigationRegion_noPath() {
        navigation.setNavigationRegion(Bounds.atOrigin(0, 0, 200, 200), emptyList());

        final var path = navigation.findPath($(10, 10), $(1900, 1900));

        assertThat(path).isEmpty();
    }

    @Test
    void setDiagonalMovementAllowed_false_setsMovementAllowedFalse() {
        assertThat(navigation.isDiagonalMovementAllowed()).isTrue();

        navigation.setDiagonalMovementAllowed(false);

        assertThat(navigation.isDiagonalMovementAllowed()).isFalse();
    }

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
