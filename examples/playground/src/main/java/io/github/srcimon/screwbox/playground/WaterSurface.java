package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class WaterSurface {

    private class Node {

        private Node left;
        private Node right;

        private double height;
        private double speed;

        void update(double delta, double singleDistance) {
            // move
            height = height + delta * speed;
            updateFromRight(delta * speed, singleDistance, 1);
            updateFromLeft(delta * speed, singleDistance);

            // spring back
            speed = speed - (height * springBackFactor * delta);

            // dump speed
            speed = speed - lossSpeedFactor * speed * delta;
        }

        public void interact(double strength) {
            speed += strength;
        }

        public void updateFromRight(double update, double singleDistance, int distCount) {
            double remainingUpdate = Math.max(update * transmissionDistance / (singleDistance * distCount), 0);
            height += remainingUpdate;
            if (nonNull(left) && remainingUpdate > stopLossAt) {
                left.updateFromRight(update, singleDistance, distCount+1);
            }
        }

        public void updateFromLeft(double update, double totalDistance) {
//            double remainingUpdate = Math.max(update * transmissionFactor/ totalDistance, 0);
//            height += remainingUpdate;
//            if (Objects.nonNull(right) && remainingUpdate > stopLossAt) {
//                right.updateFromLeft(remainingUpdate, totalDistance);
//            }
        }
    }

    private double lossSpeedFactor = 1.5;
    private double springBackFactor = 10;
    private double transmissionDistance = 40;
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

    public void update(double delta, double width) {
        nodes.forEach(node -> node.update(delta, width / nodes.size()));
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
