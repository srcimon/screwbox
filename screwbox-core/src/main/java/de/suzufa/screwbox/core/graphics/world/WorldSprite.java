package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Sprite;

public record WorldSprite(Sprite sprite, Vector position, Percentage opacity, Rotation rotation) {

    public static WorldSprite sprite(final Sprite sprite, final Vector position, final Rotation rotation) {
        return sprite(sprite, position, Percentage.max(), rotation);
    }

    public static WorldSprite sprite(final Sprite sprite, final Vector position) {
        return sprite(sprite, position, Percentage.max());
    }

    public static WorldSprite sprite(final Sprite sprite, final Vector position, final Percentage opacity) {
        return new WorldSprite(sprite, position, opacity, Rotation.none());
    }

    public static WorldSprite sprite(final Sprite sprite, final Vector position, final Percentage opacity,
            final Rotation rotation) {
        return new WorldSprite(sprite, position, opacity, rotation);
    }
}
