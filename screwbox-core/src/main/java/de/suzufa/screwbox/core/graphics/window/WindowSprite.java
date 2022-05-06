package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;

@Deprecated
public record WindowSprite(Offset offset, Sprite sprite, double scale, Percentage opacity, Rotation rotation) {

    public static WindowSprite sprite(final Sprite sprite, final Offset offset) {
        return sprite(sprite, offset, 1);
    }

    public static WindowSprite sprite(final Sprite sprite, final Offset offset, final double scale) {
        return sprite(sprite, offset, scale, Percentage.max());
    }

    public static WindowSprite sprite(final Sprite sprite, final Offset offset, final Percentage opacity) {
        return sprite(sprite, offset, 1, opacity);
    }

    public static WindowSprite sprite(final Sprite sprite, final Offset offset, final double scale,
            final Percentage opacity) {
        return new WindowSprite(offset, sprite, scale, opacity, Rotation.none());
    }

    public static WindowSprite sprite(final Sprite sprite, final Offset offset, final double scale,
            final Percentage opacity,
            final Rotation rotation) {
        return new WindowSprite(offset, sprite, scale, opacity, rotation);
    }
}
