package dev.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoundsTest {

    @Test
    void touches_boundsAligned_returnsTrue() {
        Bounds player = $$(10, 10, 20, 20);
        Bounds ball = $$(30, 10, 20, 20);

        assertThat(player.touches(ball)).isTrue();
    }

    @Test
    void touches_boundsNotTouching_returnsFalse() {
        Bounds player = $$(10, 10, 20, 20);
        Bounds ball = $$(31, 11, 20, 20);

        assertThat(player.touches(ball)).isFalse();
    }

    @Test
    void expand_positiveExpansion_returnsNewLargerInstance() {
        Bounds player = $$(10, 10, 20, 20);
        Bounds playerAfterLargeMeal = player.expand(2);

        assertThat(playerAfterLargeMeal).isEqualTo($$(9, 9, 22, 22));
    }

    @Test
    void resize_returnsNewUpdatedInstance() {
        Bounds bounds = $$(10, 10, 20, 20);
        Bounds updated = bounds.resize(4, 4);

        assertThat(updated).isEqualTo($$(18, 18, 4, 4));
    }

    @Test
    void expandTop_returnsNewLargerInstance() {
        Bounds player = $$(10, 10, 20, 20);
        Bounds playerAfterStretchingLegs = player.expandTop(2);

        assertThat(playerAfterStretchingLegs).isEqualTo($$(10, 8, 20, 22));
    }

    @Test
    void newInstance_negativeWidth_throwsException() {
        assertThatThrownBy(() -> $$(0, 0, -1, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("width must no be negative (actual value: -1.0)");
    }

    @Test
    void newInstance_negativeHeight_throwsException() {
        assertThatThrownBy(() -> $$(0, 0, 1, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("height must no be negative (actual value: -1.0)");
    }

    @Test
    void intersects_doesntIntersect_isFalse() {
        Bounds player = $$(0, 0, 10, 10);
        Bounds ball = $$(20, 20, 10, 10);

        assertThat(player.intersects(ball)).isFalse();
    }

    @Test
    void intersects_identical_isTrue() {
        Bounds player = $$(25, 25, 30, 10.1f);
        Bounds ball = $$(25, 25, 30, 10.1f);

        assertThat(player.intersects(ball)).isTrue();
    }

    @Test
    void intersects_touchesEdge_isFalse() {
        Bounds player = $$(0, 0, 1, 1);
        Bounds ball = $$(1, 1, 1, 1);

        assertThat(player.intersects(ball)).isFalse();
    }

    @Test
    void intersects_aligned_isFalse() {
        Bounds player = $$(0, 0, 1, 5);
        Bounds ball = $$(1, 1, 5, 8);

        assertThat(player.intersects(ball)).isFalse();
    }

    @Test
    void intersects_slightlyOverlaps_isTrue() {
        Bounds player = $$(0, 0, 1.1f, 5);
        Bounds ball = $$(1, 1, 5, 8);

        assertThat(player.intersects(ball)).isTrue();
    }

    @Test
    void intersects_negativeOriginButOverlaps_isTrue() {
        Bounds player = $$(-1, -1, 1, 1);
        Bounds ball = $$(-2, -3, 2, 3);

        assertThat(player.intersects(ball)).isTrue();
    }

    @Test
    void intersects_negativeOriginAndDoesntOverlap_isFalse() {
        Bounds player = $$(-1, -1, 1, 1);
        Bounds ball = $$(-4, -4, 2, 3);

        assertThat(player.intersects(ball)).isFalse();
    }

    @Test
    void position_returnsPosition() {
        Bounds player = $$(4, 6, 2, 2);

        assertThat(player.position()).isEqualTo(Vector.of(5, 7));
    }

    @Test
    void origin_returnsOrigin() {
        Bounds player = $$(4, 6, 2, 2);

        assertThat(player.origin()).isEqualTo(Vector.of(4, 6));
    }

    @Test
    void overlapArea_noOverlapping_isZero() {
        Bounds player = $$(-1, -1, 1, 1);
        Bounds ball = $$(-4, -4, 2, 3);

        assertThat(player.overlapArea(ball)).isZero();
    }

    @Test
    void overlapArea_someOverlapping_returnsValue() {
        Bounds player = $$(-1, -1, 1, 1);
        Bounds ball = $$(-2, -3, 2, 3);

        assertThat(player.overlapArea(ball)).isEqualTo(1);
    }

    @Test
    void overlapArea_identical_returnsValue() {
        Bounds player = $$(25, 25, 30, 10);
        Bounds ball = $$(25, 25, 30, 10);

        assertThat(player.overlapArea(ball)).isEqualTo(300);
    }

    @Test
    void extends_returnsHalfWidthAndHeight() {
        Bounds player = Bounds.atPosition(10, 10, 4, 2);

        assertThat(player.extents()).isEqualTo(Vector.of(2, 1));
    }

    @Test
    void width_returnsWidth() {
        Bounds player = Bounds.atPosition(10, 10, 4, 2);

        assertThat(player.width()).isEqualTo(4);
    }

    @Test
    void height_returnsHeight() {
        Bounds player = Bounds.atPosition(10, 10, 4, 2);

        assertThat(player.height()).isEqualTo(2);
    }

    @Test
    void moveBy_movesByXandYComponents() {
        Bounds player = Bounds.atPosition(10, 10, 4, 2);
        Vector movement = Vector.of(2, -1);

        Bounds result = player.moveBy(movement);

        assertThat(result).isEqualTo(Bounds.atPosition(12, 9, 4, 2));
    }

    @Test
    void moveBy_movesByXandY() {
        Bounds player = Bounds.atPosition(10, 10, 4, 2);

        Bounds result = player.moveBy(2, -1);

        assertThat(result).isEqualTo(Bounds.atPosition(12, 9, 4, 2));
    }

    @Test
    void moveTo_newPosition_movesPosition() {
        Bounds player = Bounds.atPosition(10, 10, 42, 2);

        Bounds result = player.moveTo(Vector.of(22, -2));

        assertThat(result).isEqualTo(Bounds.atPosition(22, -2, 42, 2));
    }

    @Test
    void max_returnsNewInstance() {
        Bounds bounds = Bounds.max();

        assertThat(bounds.position()).isEqualTo(Vector.zero());
        assertThat(bounds.width()).isEqualTo(Double.MAX_VALUE);
        assertThat(bounds.height()).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    void intersection_doesntIntersect_isEmpty() {
        Bounds first = $$(0, 0, 40, 100);
        Bounds onTheRight = $$(200, 0, 40, 100);
        Bounds onTop = $$(0, -400, 40, 100);

        assertThat(first.intersection(onTheRight)).isEmpty();
        assertThat(first.intersection(onTop)).isEmpty();
    }

    @Test
    void intersection_identical_isIdentical() {
        Bounds first = $$(0, 0, 40, 100);
        Bounds second = $$(0, 0, 40, 100);

        assertThat(first.intersection(second)).isEqualTo(Optional.of(first));
    }

    @Test
    void intersection_intersects_returnsIntersection() {
        Bounds first = $$(10, 0, 40, 80);
        Bounds second = $$(0, 0, 20, 100);

        assertThat(first.intersection(second)).isEqualTo(Optional.of($$(10, 0, 10, 80)));
    }

    @Test
    void allIntersecting_noneIntersects_emptyList() {
        Bounds first = $$(10, 0, 40, 80);
        Bounds second = $$(0, 0, 20, 100);
        Bounds bounds = $$(400, 400, 20, 20);

        List<Bounds> intersecting = bounds.allIntersecting(List.of(first, second));

        assertThat(intersecting).isEmpty();
    }

    @Test
    void allIntersecting_someIntersects_returnsOnlyIntersecting() {
        Bounds first = $$(10, 0, 40, 80);
        Bounds second = $$(0, 0, 20, 100);
        Bounds third = $$(0, 410, 20, 100);
        Bounds bounds = $$(5, 5, 20, 20);

        List<Bounds> intersecting = bounds.allIntersecting(List.of(first, second, third));

        assertThat(intersecting).containsExactly(first, second);
    }

    @Test
    void contains_nextToEachOther_isFalse() {
        Bounds first = $$(10, 0, 40, 80);
        Bounds second = $$(100, 0, 20, 100);

        assertThat(first.contains(second)).isFalse();
    }

    @Test
    void contains_secondInsideFirst_isTrue() {
        Bounds first = $$(10, 0, 40, 80);
        Bounds second = $$(15, 1, 20, 50);

        assertThat(first.contains(second)).isTrue();
    }

    @Test
    void toString_containsPositionAndSize() {
        assertThat($$(10, 0, 40, 80)).hasToString("Bounds [x=10.0, y=0.0, width=40.0, height=80.0]");
    }

    @Test
    void snap_outOfGrids_snapsToGrid() {
        Bounds bounds = $$(10, 12, 40, 80);

        assertThat(bounds.snap(4)).isEqualTo($$(8, 12, 40, 80));
    }

    @Test
    void snapExpand_matchesGrid_doesntChangeBounds() {
        Bounds bounds = $$(16, 16, 32, 32);

        assertThat(bounds.snapExpand(16)).isEqualTo(bounds);
    }

    @ParameterizedTest
    @CsvSource({
        "150, 163.231, 10, 20",
        "128, 143.2, 4.2, 0.1",
        "191.9, 140, 0.05, 0.1"})
    void snapExpand_withinGridCells_expandsToCell(double x, double y, double width, double height) {
        Bounds bounds = Bounds.atOrigin(x, y, width, height);

        assertThat(bounds.snapExpand(64)).isEqualTo(Bounds.atOrigin(128, 128, 64, 64));
    }

    @Test
    void snapExpand_withinTwoCells_expandsToTwoCells() {
        Bounds bounds = Bounds.atOrigin(191.9, 1, 2, 0.1);

        assertThat(bounds.snapExpand(64)).isEqualTo(Bounds.atOrigin(128, 0, 128, 64));
    }

    @Test
    void around_noPositions_throwsException() {
        List<Vector> noPositions = Collections.emptyList();
        assertThatThrownBy(() -> Bounds.around(noPositions))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("positions must not be empty");
    }

    @Test
    void around_somePositions_createsBoundsAroundPositions() {
        var bounds = Bounds.around(List.of($(4, 2), $(-100, 99), $(400, 3)));
        assertThat(bounds).isEqualTo(Bounds.atOrigin(-100, 2, 500, 97));
    }

    @Test
    void around_someNegativePositions_createsBoundsAroundPositions() {
        var bounds = Bounds.around(List.of($(-117.25, -52.5), $(-57.0, 13.75)));
        assertThat(bounds).isEqualTo(Bounds.atOrigin(-117.25, -52.5, 60.25, 66.25));
    }
}
