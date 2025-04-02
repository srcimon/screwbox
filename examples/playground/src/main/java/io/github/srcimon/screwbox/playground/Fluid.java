package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fluid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final FluidOptions options;

    private class Node implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private double height;
        private double speed;
        private double deltaLeft;
        private double deltaRight;

        void update(final double delta) {
            // move
            height = height + delta * speed;

            // side pull
            speed -= deltaLeft * delta * options.transmission();
            speed -= deltaRight * delta * options.transmission();

            // retract
            speed = speed - (height * options.retract() * delta);

            // dampen
            speed = speed - options.dampening() * speed * delta;
        }
    }

    private final List<Node> nodes = new ArrayList<>();

    public Fluid(final FluidOptions options) {
        this.options = options;
        for (int i = 0; i < options.nodeCount(); i++) {
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
                nodes.get(i).deltaLeft = nodes.get(i).height - nodes.get(i - 1).height;
            }
            if (i < nodes.size() - 1) {
                nodes.get(i).deltaRight = nodes.get(i).height - nodes.get(i + 1).height;
            }
        }
        nodes.forEach(node -> node.update(delta));
    }

    public void interact(final Bounds projection, final Bounds interaction, final double strength) {
        var nodePositions = surfaceNodes(projection);

        for (int i = 0; i < nodes.size(); i++) {
            final Vector nodePosition = nodePositions.get(i);
            if (interaction.contains(nodePosition)) {
                nodes.get(i).speed += strength;
            }
        }
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

    public List<Vector> surfaceNodes(final Bounds bounds) {
        final var gap = bounds.width() / (nodes.size() - 1);
        int i = 0;
        final List<Vector> path = new ArrayList<>();
        for (var node : nodes) {
            path.add(bounds.origin().addX(i++ * gap).addY(node.height));
        }
        return path;
    }
}
