package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Graphics;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

public class AudioListener {

    private final Graphics graphics;

    public AudioListener(final Graphics graphics) {
        this.graphics = graphics;
    }

    public double distanceTo(final Vector position) {
        return graphics.camera().position().distanceTo(position);
    }

    public double relativePosition(final Vector position) {
        return modifier(position.x() - graphics.camera().position().x());
    }
}
