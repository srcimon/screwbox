package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

public class WaterSurface {

    class Node {

        private Vector restingPosition;
        private double height;
        private double speed;

        Node(final Vector restingPosition) {
            this.restingPosition = restingPosition;
        }

        Vector position() {
            return restingPosition.addY(height);
        }

        public void update(double delta) {
            height = height + delta * speed;
        }
    }


    private final List<Node> nodes = new ArrayList<>();

    public WaterSurface(Line waterLine, int nodeCount) {
        Validate.isTrue(() -> waterLine.from().y() == waterLine.to().y(), "water line must be horizontal");
        Validate.positive(nodeCount, "node count must be positive");
        double x = 0;
        double distance = waterLine.length() / nodeCount;

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(waterLine.from().addX(x)));
            x += distance;
        }
    }

    public void update(double delta) {
        nodes().forEach(node -> node.update(delta));
    }

    public List<Node> nodes() {
        return nodes;
    }

    public Path surface() {
        return Path.withNodes(nodes().stream().map(Node::position).toList());
    }
}
