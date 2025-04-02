package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//TODO convert to simple component
public class Fluid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final FluidOptions options;

    public double getHeight(int nodeNr) {
        return nodes.get(nodeNr).height;
    }

    public int nodeCount() {
        return nodes.size();
    }

    private class Node implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private double height;
        private double speed;
    }

    private final List<Node> nodes = new ArrayList<>();

    public Fluid(final FluidOptions options) {
        this.options = options;
        for (int i = 0; i < options.nodeCount(); i++) {
            nodes.add(new Node());
        }
    }

    public void update(final double delta) {
        for (int i = 0; i < nodes.size(); i++) {
            var node = nodes.get(i);
            final double deltaLeft = i > 0 ? node.height - nodes.get(i - 1).height : 0;
            final double deltaRight = i < nodes.size() - 1 ? node.height - nodes.get(i + 1).height : 0;

            // move
            node.height = node.height + delta * node.speed;

            final double sidePull = deltaLeft * delta * options.transmission() + deltaRight * delta * options.transmission();
            final double retract = node.height * options.retract() * delta;
            final double dampen = options.dampening() * node.speed * delta;
            node.speed += -sidePull -retract -dampen;
        }

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

    //TODO This code is kind of duplicate in FluidRenderSystem and FluidInteractionSystem
    private List<Vector> surfaceNodes(final Bounds bounds) {
        final var gap = gapSize(bounds);
        int i = 0;
        final List<Vector> path = new ArrayList<>();
        for (var node : nodes) {
            path.add(bounds.origin().addX(i++ * gap).addY(node.height));
        }
        return path;
    }

    public double gapSize(Bounds bounds) {
        return bounds.width() / (nodes.size() - 1);
    }
}
