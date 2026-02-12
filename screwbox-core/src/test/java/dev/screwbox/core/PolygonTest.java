package dev.screwbox.core;

import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static dev.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class PolygonTest {

    @Test
    void ofNodes_noNodes_throwsException() {
        List<Vector> noNodes = emptyList();

        assertThatThrownBy(() -> Polygon.ofNodes(noNodes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("polygon must have at least one node");
    }

    @Test
    void removeNode_noNodesLeft_throwsException() {
        Polygon polygon = Polygon.ofNodes(createNodes(1));

        assertThatThrownBy(() -> polygon.removeNode(0))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("polygon must have at least one node");
    }

    @Test
    void removeNode_polygonDoesntHaveNode_throwsException() {
        Polygon polygon = Polygon.ofNodes(createNodes(3));

        assertThatThrownBy(() -> polygon.removeNode(4))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("polygon does not contain node 4");
    }

    @Test
    void removeNode_nodesLeft_removesNode() {
        Polygon polygon = Polygon.ofNodes(createNodes(5));

        var newInstance = polygon.removeNode(0);

        assertThat(newInstance.definitionNotes())
            .hasSize(4)
            .doesNotContain($(0, 0));
    }

    @Test
    void nodeCount_returnsNodeCount() {
        Polygon polygon = Polygon.ofNodes(createNodes(5));

        assertThat(polygon.nodeCount()).isEqualTo(5);
    }

    @Test
    void segments_oneNode_isEmpty() {
        Polygon polygon = Polygon.ofNodes(createNodes(1));

        assertThat(polygon.segments()).isEmpty();
    }

    @Test
    void nodes_closedPolygon_doesNotReturnLastNode() {
        var polygon = createClosedPolygon();

        var nodes = polygon.nodes();

        assertThat(nodes.getFirst()).isNotEqualTo(nodes.getLast());
        assertThat(nodes).hasSize(4);
    }

    @Test
    void nodes_openPolygon_returnsLastNode() {
        var polygon = Polygon.ofNodes(createNodes(3));

        var nodes = polygon.nodes();

        assertThat(nodes).hasSize(3);
    }

    @Test
    void definitionNodes_closedPolygon_firstNodeIsSameAsLastOne() {
        var polygon = createClosedPolygon();

        var nodes = polygon.definitionNotes();

        assertThat(nodes.getFirst()).isEqualTo(nodes.getLast());
        assertThat(nodes).hasSize(5);
    }

    @Test
    void segments_fourNodes_threeSegments() {
        var polygon = Polygon.ofNodes(createNodes(4));

        assertThat(polygon.segments()).isUnmodifiable().containsExactly(
            Line.between($(0, 0), $(1, 1)),
            Line.between($(1, 1), $(2, 2)),
            Line.between($(2, 2), $(3, 3)));
    }

    @Test
    void toString_twoNodes_containsVectors() {
        assertThat(Polygon.ofNodes(createNodes(2))).hasToString("Polygon[nodes=[Vector [x=0.00, y=0.00], Vector [x=1.00, y=1.00]]]");
    }

    @Test
    void isClosed_notClosed_isFalse() {
        var polygon = Polygon.ofNodes(createNodes(4));

        assertThat(polygon.isClosed()).isFalse();
    }

    @Test
    void isClosed_closed_isTrue() {
        var polygon = Polygon.ofNodes(createNodes(4));
        var closedPolygon = polygon.addNode(polygon.definitionNotes().getFirst());

        assertThat(closedPolygon.isClosed()).isTrue();
    }

    @Test
    void addNode_twoNodesPresent_hasThreeNodes() {
        var polygon = Polygon.ofNodes(createNodes(2));
        var extendedPolygon = polygon.addNode($(2, 1));

        assertThat(extendedPolygon.definitionNotes()).containsExactly($(0, 0), $(1, 1), $(2, 1));
    }

    @Test
    void closestPoint_closeToStart_returnsStart() {
        var polygon = Polygon.ofNodes(createNodes(2));
        assertThat(polygon.closestPoint($(-1, 0))).isEqualTo(polygon.firstNode());
    }

    @Test
    void closestPoint_closeToMiddleSegment_returnsStart() {
        var polygon = Polygon.ofNodes(createNodes(3));
        assertThat(polygon.closestPoint($(1.5, 1.5))).isEqualTo($(1.5, 1.5));
    }

    @Test
    void isInside_notClosed_isFalse() {
        var polygon = Polygon.ofNodes(createNodes(4));

        assertThat(polygon.contains($(0, 0))).isFalse();
    }

    @Test
    void isInside_inside_isTrue() {
        var polygon = createClosedPolygon();

        assertThat(polygon.contains($(4, 4))).isTrue();
    }

    @Test
    void isInside_outside_isFalse() {
        var polygon = createClosedPolygon();

        assertThat(polygon.contains($(40, -4))).isFalse();
    }

    @Test
    void isInside_outsideEdgeCase_isFalse() {
        var polygon = Polygon.ofNodes(List.of($(124.77, 144.45), $(140.67, 145.27), $(156.57, 144.45), $(157.38, 158.00), $(140.67, 158.00), $(123.95, 158.00), $(124.77, 144.45)));

        assertThat(polygon.contains($(71.54, 126.86))).isFalse();
    }

    @Test
    void previousNode_secondInClosedPolygon_isFirst() {
        var polygon = createClosedPolygon();
        assertThat(polygon.previousNode(1)).isEqualTo(polygon.firstNode());
    }

    @Test
    void previousNode_firstInClosedPolygon_isLast() {
        var polygon = createClosedPolygon();
        assertThat(polygon.previousNode(0)).isEqualTo($(0, 10));
    }

    @Test
    void previousNode_firstInOpenPolygon_throwsException() {
        var polygon = Polygon.ofNodes(createNodes(4));
        assertThatThrownBy(() -> assertThat(polygon.previousNode(0))
            .isInstanceOf(IllegalArgumentException.class))
            .hasMessage("node number not in valid range (actual value: -1)");
    }

    @Test
    void nextNode_secondInClosedPolygon_isThird() {
        var polygon = createClosedPolygon();
        assertThat(polygon.nextNode(1)).isEqualTo(polygon.node(2));
    }

    @Test
    void nextNode_lastInClosedPolygon_isFirst() {
        var polygon = createClosedPolygon();
        assertThat(polygon.nextNode(3)).isEqualTo(polygon.firstNode());
    }

    @Test
    void nextNode_lastInOpenPolygon_throwsException() {
        var polygon = Polygon.ofNodes(createNodes(4));
        assertThatThrownBy(() -> assertThat(polygon.nextNode(3))
            .isInstanceOf(IllegalArgumentException.class))
            .hasMessage("node number not in valid range (actual value: 4)");
    }

    @Test
    void center_openPolygon_returnsCenter() {
        var polygon = Polygon.ofNodes(createNodes(4));
        assertThat(polygon.center()).isEqualTo($(1.5, 1.5));
    }

    @Test
    void center_closedPolygon_returnsCenter() {
        var polygon = createClosedPolygon();
        assertThat(polygon.center()).isEqualTo($(5, 5));
    }

    @Test
    void isClockwise_isClosedClockwise_isTrue() {
        var polygon = createClosedPolygon();

        assertThat(polygon.isClockwise()).isTrue();
    }

    @Test
    void isClockwise_isClosedCounterClockwise_isFalse() {
        List<Vector> nodesInReverseOrder = createClosedPolygon().definitionNotes().reversed();
        var polygon = Polygon.ofNodes(nodesInReverseOrder);

        assertThat(polygon.isClockwise()).isFalse();
    }

    @Test
    void isClockwise_twoNodesOnly_isFalse() {
        var polygon = Polygon.ofNodes(createNodes(2));

        assertThat(polygon.isClockwise()).isFalse();
    }

    @Test
    void isOpen_closedPolygon_isFalse() {
        var polygon = createClosedPolygon();
        assertThat(polygon.isOpen()).isFalse();
    }

    @Test
    void isOpen_oneNode_isTrue() {
        var polygon = Polygon.ofNodes(createNodes(1));
        assertThat(polygon.isOpen()).isTrue();
    }

    @Test
    void testSerialization() {
        var polygon = Polygon.ofNodes(createNodes(2));
        var nodesBefore = polygon.nodes();
        var definitionNodesBefore = polygon.definitionNotes();
        var isClosedBefore = polygon.isClosed();
        var centerBefore = polygon.center();

        var afterSerialization = TestUtil.roundTripSerialization(polygon);

        assertThat(afterSerialization.nodes()).isEqualTo(nodesBefore);
        assertThat(afterSerialization.definitionNotes()).isEqualTo(definitionNodesBefore);
        assertThat(afterSerialization.isClosed()).isEqualTo(isClosedBefore);
        assertThat(afterSerialization.center()).isEqualTo(centerBefore);
    }

    @Test
    void bisectorRay_firstNodeOfOpenPolygon_returnsRay() {
        var polygon = Polygon.ofNodes(List.of(Vector.zero(), $(10, 10), $(20, 20), $(30, -50)));

        Optional<Line> ray = polygon.bisectorRay(0);
        assertThat(ray).isNotEmpty();
        assertThat(ray.orElseThrow().start()).isEqualTo(Vector.zero());
        assertThat(ray.orElseThrow().end().y()).isEqualTo(-26.67, offset(0.01));
        assertThat(ray.orElseThrow().end().y()).isEqualTo(-26.67, offset(0.01));
    }

    @Test
    void bisectorRay_lastNodeOfOpenPolygon_returnsRay() {
        var polygon = Polygon.ofNodes(List.of($(200, 400), $(200, 50), $(-100, -50), Vector.zero()));

        Optional<Line> ray = polygon.bisectorRay(3);
        assertThat(ray).isNotEmpty();
        assertThat(ray.orElseThrow().start()).isEqualTo(Vector.zero());
        assertThat(ray.orElseThrow().end().y()).isEqualTo(-14.28, offset(0.01));
        assertThat(ray.orElseThrow().end().y()).isEqualTo(-14.28, offset(0.01));
    }

    @Test
    void bisectorRay_uForm_findsRaysHittingTheNearestSegment() {
        var uFormPolygon = Polygon.ofNodes(List.of(
            $(188.81, 113.96),
            $(168.13, 126.75),
            $(188.48, 195.81),
            $(336.29, 186.51),
            $(356.18, 95.59),
            $(316.95, 111.72),
            $(302.38, 189.35),
            $(234.44, 178.89),
            $(188.81, 113.96)));

        final var bisectorRays = IntStream.range(0, uFormPolygon.nodeCount())
            .mapToObj(uFormPolygon::bisectorRay)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        assertThat(bisectorRays).hasSize(7);

        // toString avoids double imprecision
        assertThat(bisectorRays.getFirst()).hasToString("Line [start=Vector [x=188.81, y=113.96], end=Vector [x=178.78, y=162.90]]");
        assertThat(bisectorRays.get(1)).hasToString("Line [start=Vector [x=168.13, y=126.75], end=Vector [x=208.69, y=142.25]]");
        assertThat(bisectorRays.get(4)).hasToString("Line [start=Vector [x=356.18, y=95.59], end=Vector [x=309.54, y=151.19]]");
    }


    @Test
    void bisectorRay_noBisectorRay_isEmpty() {
        var polygon = Polygon.ofNodes(createNodes(8));

        assertThat(polygon.bisectorRay(1)).isEmpty();
    }

    @Test
    void bisectorRay_bisectorRayHasHit_returnsRay() {
        var polygon = createClosedPolygon();

        Optional<Line> ray = polygon.bisectorRay(1);

        assertThat(ray).isNotEmpty();
        assertThat(ray.orElseThrow().start()).isEqualTo($(10, 0));
        assertThat(ray.orElseThrow().end().x()).isEqualTo(0, offset(0.01));
        assertThat(ray.orElseThrow().end().y()).isEqualTo(10, offset(0.01));
    }


    @ParameterizedTest
    @CsvSource({
        "8.0, 0.0, 1",
        "0.0, 0.0, 0"
    })
    void nearestIndex_positionNearPolygon_returnsIndex(double x, double y, int index) {
        var polygon = createClosedPolygon();
        assertThat(polygon.nearestIndex($(x, y))).isEqualTo(index);
    }

    @Test
    void opposingIndex_closedPolygon_returnsIndex() {
        var polygon = createClosedPolygon();
        assertThat(polygon.opposingIndex(0)).contains(2);
    }

    @Test
    void opposingIndex_OpenPolygon_isEmpty() {
        var polygon = Polygon.ofNodes(createNodes(12));
        assertThat(polygon.opposingIndex(0)).isEmpty();
    }

    @Test
    void alignTemplate_differentNodeCount_throwsException() {
        var first = createClosedPolygon();
        var second = Polygon.ofNodes(createNodes(2));

        assertThatThrownBy(() -> first.alignTemplate(second, false, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("both polygons must have same node count for alignment");
    }

    @Test
    void alignTemplate_sameAsTemplate_noChange() {
        var first = createClosedPolygon();
        var second = createClosedPolygon();

        assertThat(first.alignTemplate(second, true, true)).isEqualTo(first);
    }

    @Test
    void alignTemplate_rotationNotAllowed_returnsAlignedTemplate() {
        var first = createClosedPolygon(5);
        var second = createClosedPolygon(10);

        Polygon aligned = first.alignTemplate(second, false, true);

        assertThat(aligned.nodeCount()).isEqualTo(first.nodeCount());
        assertThat(aligned.firstNode()).isEqualTo(Vector.$(-2.5, -2.5));
    }

    @Test
    void alignTemplate_rotationAllowed_returnsAlignedTemplate() {
        var first = createClosedPolygon();
        var second = Polygon.ofNodes(List.of($(0, 0), $(10, 0), $(20, 0), $(30, 0), $(0, 0)));

        Polygon alignedWithoutRotation = first.alignTemplate(second, false, true);
        Polygon aligned = first.alignTemplate(second, true, true);

        assertThat(alignedWithoutRotation).isNotEqualTo(aligned);
        assertThat(aligned.firstNode().x()).isEqualTo(5, offset(0.01));
        assertThat(aligned.firstNode().y()).isEqualTo(-10, offset(0.01));
    }

    @Test
    void close_alreadyClosed_returnsSameInstance() {
        var polygon = createClosedPolygon();
        assertThat(polygon.close()).isEqualTo(polygon);
    }

    @Test
    void close_isOpen_returnsClosedInstance() {
        var polygon = Polygon.ofNodes(createNodes(4));

        var closedPolygon = polygon.close();

        assertThat(closedPolygon.nodeCount()).isEqualTo(4);
        assertThat(closedPolygon.definitionNotes()).hasSize(5);
        assertThat(closedPolygon.isClosed()).isTrue();
    }

    @Test
    void area_notClosed_isZero() {
        var polygon = Polygon.ofNodes(createNodes(4));
        assertThat(polygon.area()).isZero();
    }

    @Test
    void area_onlyTwoNodes_isZero() {
        var polygon = Polygon.ofNodes(createNodes(2)).close();
        assertThat(polygon.area()).isZero();
    }

    @Test
    void area_closedSimplePolygon_calculatesArea() {
        var polygon = createClosedPolygon();

        assertThat(polygon.area()).isEqualTo(100.0);
    }

    @Test
    void stroked_negativeWidth_throwsException() {
        var polygon = Polygon.ofNodes($(10, 2), $(4, 2));

        assertThatThrownBy(() -> polygon.stroked(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("stroke width must be positive (actual value: -1.0)");
    }

    @Test
    void stroked_closedPolygon_throwsException() {
        var polygon = createClosedPolygon();

        assertThatThrownBy(() -> polygon.stroked(4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("polygon must be open to create stroked polygon");
    }

    private static Polygon createClosedPolygon() {
        return createClosedPolygon(10);
    }

    private static Polygon createClosedPolygon(int size) {
        return Polygon.ofNodes(List.of($(0, 0), $(size, 0), $(size, size), $(0, size), $(0, 0)));
    }

    private List<Vector> createNodes(int count) {
        List<Vector> nodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            nodes.add($(i, i));
        }
        return nodes;
    }
}
