package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaterSurface {

    private class Node {

        private Node left;
        private Node right;

        private double height;
        private double speed;


        void update(double delta) {
            // move
            height = height + delta * speed;
            updateFromRight(delta * speed);
            updateFromLet(delta * speed);

            // spring back
            speed = speed - (height * 10 * delta);

            // dump speed
            speed = speed - 1 * speed * delta;
        }

        public void interact(double strength) {
            speed += strength;
        }

        public void updateFromRight(double update) {
            double remainingUpdate = Math.max(update * lossFactor, 0);
            height += remainingUpdate;
            if (Objects.nonNull(left) && remainingUpdate > stopLossAt) {
                left.updateFromRight(remainingUpdate);
            }
        }

        public void updateFromLet(double update) {
            double remainingUpdate = Math.max(update * lossFactor, 0);
            height += remainingUpdate;
            if (Objects.nonNull(right) && remainingUpdate > stopLossAt) {
                right.updateFromRight(remainingUpdate);
            }
        }
    }

    private double lossFactor = 0.5;
    private double stopLossAt = 0.1;

    private final List<Node> nodes = new ArrayList<>();

    public WaterSurface(int nodeCount) {
        Validate.positive(nodeCount, "node count must be positive");

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node());
        }

        for (int i = 0; i < nodeCount; i++) {
            if (i > 0) {
                nodes.get(i).left = nodes.get(i - 1);
            }
            if (i < nodeCount - 1) {
                nodes.get(i).right = nodes.get(i + 1);
            }
        }
    }

    public void update(double delta) {
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
