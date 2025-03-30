package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

public class FluidSurface {

    public double maxHeight() {
        double maxHeight = 0;
        for(var node : nodes) {
            if(node.height > maxHeight) {
                maxHeight = node.height;
            }
        }
        return maxHeight;

    }

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

    private double dampening = 1.2;
    private double pullBack = 5;
    private double transmissionFactor = 20;

    private final List<Node> nodes = new ArrayList<>();

    public FluidSurface(int nodeCount) {
        Validate.positive(nodeCount, "node count must be positive");

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node());
        }
    }

    public void update(double delta) {
        for(int i = 0; i < nodes.size(); i++) {
            if(i > 0) {
                nodes.get(i).deltaHeightLeft = nodes.get(i).height - nodes.get(i-1).height;
            }
            if(i < nodes.size()-1) {
                nodes.get(i).deltaHeightRight = nodes.get(i).height - nodes.get(i+1).height;
            }
        }
        nodes.forEach(node -> node.update(delta));
    }

    public void interact(int nodeNumber, double strength) {
        //TODO validate
        nodes.get(nodeNumber).interact(strength);
    }

    public Path surface(Vector start, double length) {
        int i = 0;
        List<Vector> path = new ArrayList<>();
        for (var node : nodes) {
            path.add(start.addX(i++ * length / (nodes.size()-1)).addY(node.height));
        }
        return Path.withNodes(path);
    }
}
