package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

//TODO javadoc and test
public class SpriteDrawOptions {

    private double scale = 1;
    private Percent opacity = Percent.max();
    private Rotation rotation = Rotation.none();
    private Flip flip = Flip.NONE;

    public static SpriteDrawOptions options() {
        return new SpriteDrawOptions();
    }

    //TODO validation
    public SpriteDrawOptions scale(final double scale) {
        this.scale = scale;
        return this;
    }

    public SpriteDrawOptions opacity(final Percent opacity) {
        //TODO validation
        this.opacity = opacity;
        return this;
    }

    public SpriteDrawOptions rotation(final Rotation rotation) {
        //TODO validation
        this.rotation = rotation;
        return this;
    }

    public SpriteDrawOptions flip(final Flip flip) {
        //TODO validation
        this.flip = flip;
        return this;
    }

    public double scale() {
        return scale;
    }

    public Percent opacity() {
        return opacity;
    }

    public Rotation rotation() {
        return rotation;
    }

    public Flip flip() {
        return flip;
    }
}
