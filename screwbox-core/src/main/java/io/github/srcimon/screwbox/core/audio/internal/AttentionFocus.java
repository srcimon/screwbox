package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Graphics;

public class AttentionFocus {

    private final Graphics graphics;

    public AttentionFocus(final Graphics graphics) {
        this.graphics = graphics;
    }

    public double distanceTo(final Vector position) {
        return graphics.camera().position().distanceTo(position);
    }

    public Vector direction(final Vector position) {
        return position.substract(graphics.camera().position()).length(1);
    }

}
