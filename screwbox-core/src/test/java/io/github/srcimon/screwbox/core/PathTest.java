package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.github.srcimon.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {

    @Test
    void withNodes_noNodes_throwsException() {
        List<Vector> noNodes = emptyList();

        assertThatThrownBy(() -> Path.withNodes(noNodes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Path must have at least one node.");
    }

    @Test
    void removeNode_noNodesLeft_throwsException() {
        Path path = Path.withNodes(createNodes(1));

        assertThatThrownBy(() -> path.removeNode(0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot drop last node.");
    }

    @Test
    void removeNode_pathDoesntHaveNode_throwsException() {
        Path path = Path.withNodes(createNodes(3));

        assertThatThrownBy(() -> path.removeNode(4))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Path doesnt have node: 4");
    }

    @Test
    void removeNode_nodesLeft_removesNode() {
        Path path = Path.withNodes(createNodes(5));

        path.removeNode(0);

        assertThat(path.nodes())
                .hasSize(4)
                .doesNotContain($(0, 0));
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
                Line.between($(0, 0), $(1, 1)),
                Line.between($(1, 1), $(2, 2)),
                Line.between($(2, 2), $(3, 3)));
    }

    private List<Vector> createNodes(int count) {
        List<Vector> nodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            nodes.add($(i, i));
        }
        return nodes;
    }
}
