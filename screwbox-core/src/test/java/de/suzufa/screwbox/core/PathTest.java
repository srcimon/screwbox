package de.suzufa.screwbox.core;

import static de.suzufa.screwbox.core.Segment.between;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    void withNodes_noNodes_throwsException() {
        assertThatThrownBy(() -> Path.withNodes(emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Path must have at least one node.");
    }

    @Test
    void dropStart_noNodeLeft_throwsException() {
        Path path = Path.withNodes(createNodes(1));

        assertThatThrownBy(() -> path.dropStart())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot drop last node.");
    }

    @Test
    void dropStart_nodesLeft_dropsFirstNode() {
        Path path = Path.withNodes(createNodes(5));

        path.dropStart();

        assertThat(path.nodes())
                .hasSize(4)
                .doesNotContain(Vector.of(0, 0));
    }

    @Test
    void nodeCount_returnsNodeCount() {
        Path path = Path.withNodes(createNodes(5));

        assertThat(path.nodeCount()).isEqualTo(5);
    }

    @Test
    void segments_oneNode_isEmpty() {
        Path path = Path.withNodes(createNodes(1));

        assertThat(path.segments()).isEmpty();
    }

    @Test
    void segments_fourNodes_threeSegments() {
        Path path = Path.withNodes(createNodes(4));

        assertThat(path.segments()).containsExactly(
                between(Vector.of(0, 0), Vector.of(1, 1)),
                between(Vector.of(1, 1), Vector.of(2, 2)),
                between(Vector.of(2, 2), Vector.of(3, 3)));
    }

    private List<Vector> createNodes(int count) {
        List<Vector> nodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            nodes.add(Vector.of(i, i));
        }
        return nodes;
    }
}
