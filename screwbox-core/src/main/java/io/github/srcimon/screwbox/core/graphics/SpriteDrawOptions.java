package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

//TODO rectangle immutable
//TODO line immutable
//TODO circle immutable
//TODO: camerashakeoptions immutable
//TODO javadoc and test
public record SpriteDrawOptions(double scale, Percent opacity, Rotation rotation, Flip flip) {

    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Rotation.none(), Flip.NONE);
    }

    public static SpriteDrawOptions originalSize() {
        return scaled(1);
    }

    public static SpriteDrawOptions scaled(double scale) {
        return new SpriteDrawOptions(scale);
    }

    public SpriteDrawOptions scale(final double scale) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    public SpriteDrawOptions opacity(final Percent opacity) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    public SpriteDrawOptions rotation(final Rotation rotation) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    public SpriteDrawOptions flip(final Flip flip) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

}
