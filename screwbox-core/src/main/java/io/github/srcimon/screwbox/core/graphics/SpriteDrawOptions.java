package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

//TODO: camerashakeoptions immutable
//TODO javadoc and test
public class SpriteDrawOptions {

    private final double scale;
    private final Percent opacity ;
    private final Rotation rotation ;
    private final  Flip flip;

    private SpriteDrawOptions(double scale, Percent opacity, Rotation rotation, Flip flip) {
        this.scale = scale;
        this.opacity = opacity;
        this.rotation = rotation;
        this.flip = flip;
    }
    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Rotation.none(), Flip.NONE);
    }

    public static SpriteDrawOptions originalSize() {
        return scaled(1);
    }

    public static SpriteDrawOptions scaled(double scale) {
        return new SpriteDrawOptions(scale);
    }

    //TODO validation
    public SpriteDrawOptions scale(final double scale) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    public SpriteDrawOptions opacity(final Percent opacity) {
        //TODO validation
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    public SpriteDrawOptions rotation(final Rotation rotation) {
        //TODO validation
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    public SpriteDrawOptions flip(final Flip flip) {
        //TODO validation
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
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
