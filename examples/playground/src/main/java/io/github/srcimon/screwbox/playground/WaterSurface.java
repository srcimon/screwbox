package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

public class WaterSurface {

    private class Node {

        private double height;
        private double speed;
        private double deltaHeightLeft;
        private double deltaHeightRight;

        void update(double delta) {
            // move
            height = height + delta * speed;
            speed -= deltaHeightLeft * delta * 10;
            speed -= deltaHeightRight * delta * 10;

            // spring back
            speed = speed - (height * pullBack * delta);

            // dump speed
            speed = speed - dampening * speed * delta;
        }

        public void interact(double strength) {
            speed += strength;
        }

    }

    private double dampening = 1.5;
    private double pullBack = 10;

    private final List<Node> nodes = new ArrayList<>();

    public WaterSurface(int nodeCount) {
        Validate.positive(nodeCount, "node count must be positive");

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node());
        }
    }

    public void update(double delta, double width) {
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
            path.add(start.addX(i++ * length / nodes.size()).addY(node.height));
        }
        return Path.withNodes(path);
    }
}
