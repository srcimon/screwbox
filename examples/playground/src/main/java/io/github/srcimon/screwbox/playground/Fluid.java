package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

public class Fluid {

    private class Node {

        private double height;
        private double speed;
        private double deltaHeightLeft;
        private double deltaHeightRight;

        void update(double delta) {
            // move
            height = height + delta * speed;
            speed -= deltaHeightLeft * delta * transmissionFactor;
            speed -= deltaHeightRight * delta * transmissionFactor;

            // spring back
            speed = speed - (height * pullBack * delta);

            // dump speed
            speed = speed - dampening * speed * delta;
        }

        public void interact(double strength) {
            speed += strength;
        }

    }

    //TODO FluidOptions?
    private double dampening = 1.5;
    private double pullBack = 15;
    private double transmissionFactor = 20;

    private final List<Node> nodes = new ArrayList<>();

    public Fluid(int nodeCount) {
        Validate.positive(nodeCount, "node count must be positive");

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node());
        }
    }

    public double maxHeight() {
        double maxHeight = 0;
        for (final var node : nodes) {
            if (node.height > maxHeight) {
                maxHeight = node.height;
            }
        }
        return maxHeight;
    }

    public void update(final double delta) {
        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) {
                nodes.get(i).deltaHeightLeft = nodes.get(i).height - nodes.get(i - 1).height;
            }
            if (i < nodes.size() - 1) {
                nodes.get(i).deltaHeightRight = nodes.get(i).height - nodes.get(i + 1).height;
            }
        }
        nodes.forEach(node -> node.update(delta));
    }

    public void interact(int nodeNumber, double strength) {
        Validate.range(nodeNumber, 0, nodes.size(), "node number is out of range");
        nodes.get(nodeNumber).interact(strength);
    }

    public Path surface(final Bounds bounds) {
        final List<Vector> path = surfaceNodes(bounds);
        return Path.withNodes(path);
    }

    public Path outline(final Bounds bounds) {
        final List<Vector> surfaceNodes = surfaceNodes(bounds);
        surfaceNodes.add(bounds.bottomRight());
        surfaceNodes.add(bounds.bottomLeft());
        return Path.withNodes(surfaceNodes);
    }

    private List<Vector> surfaceNodes(final Bounds bounds) {
        final var gap = bounds.width() / (nodes.size() - 1);
        int i = 0;
        final List<Vector> path = new ArrayList<>();
        for (var node : nodes) {
            path.add(bounds.origin().addX(i++ * gap).addY(node.height));
        }
        return path;
    }
}
