package dev.screwbox.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    private static Polygon createClosedPolygon() {
        return Polygon.ofNodes(List.of($(0, 0), $(10, 0), $(10, 10), $(0, 10), $(0, 0)));
    }

    private List<Vector> createNodes(int count) {
        List<Vector> nodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            nodes.add($(i, i));
        }
        return nodes;
    }
}
