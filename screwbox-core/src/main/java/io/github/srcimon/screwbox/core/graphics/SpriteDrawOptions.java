package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

//TODO javadoc and test
public class SpriteDrawOptions {

    private double scale = 1;
    private Percent opacity = Percent.max();
    private Rotation rotation = Rotation.none();
    private Flip flip = Flip.NONE;
    private ScreenBounds clip = null;

    public SpriteDrawOptions() {
    }

    private SpriteDrawOptions(final double scale) {
        this.scale = scale;
    }

    public static SpriteDrawOptions originalSize() {
        return scaled(1);
    }

    public static SpriteDrawOptions scaled(double scale) {
        return new SpriteDrawOptions(scale);
    }

    //TODO remove from options?
    public SpriteDrawOptions clip(final ScreenBounds clip) {
        this.clip = clip;
        return this;
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

    public ScreenBounds clip() {
        return clip;
    }
}
