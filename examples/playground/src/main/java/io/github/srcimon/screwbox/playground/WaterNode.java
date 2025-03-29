package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Vector;

public class WaterNode {

    private Vector restingPosition;
    private double height;
    private double speed;

    public WaterNode(final Vector restingPosition) {
        this.restingPosition = restingPosition;
    }

    public Vector position() {
        return restingPosition.addY(height);
    }
}
