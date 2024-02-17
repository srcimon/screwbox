package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.*;

public interface World {

    World drawFadingCircle(Vector position, double diameter, Color color);

    World drawSpriteBatch(SpriteBatch spriteBatch, Bounds clip);

    default World drawSpriteBatch(final SpriteBatch spriteBatch) {
        return drawSpriteBatch(spriteBatch, null);
    }

    World drawSprite(Sprite sprite, Vector origin, double scale, Percent opacity, Rotation rotation,
                     Flip flip, Bounds clip);

    default World drawSprite(final Sprite sprite, final Vector origin, final Percent opacity,
                             final Rotation rotation, final Flip flip) {
        return drawSprite(sprite, origin, 1, opacity, rotation, flip, null);
    }

    default World drawSprite(final Sprite sprite, final Vector origin, final Percent opacity) {
        return drawSprite(sprite, origin, opacity, Rotation.none(), Flip.NONE);
    }

    default World drawSprite(final Sprite sprite, final Vector origin) {
        return drawSprite(sprite, origin, Percent.max());
    }

    World drawText(Vector offset, String text, Font font, Color color);

    World drawTextCentered(Vector position, String text, Font font, Color color);

    World drawLine(Vector from, Vector to, Color color);

    default World drawLine(final Line line, final Color color) {
        return drawLine(line.from(), line.to(), color);
    }

    World fillCircle(Vector position, double diameter, Color color);

    World drawCircle(Vector position, double diameter, Color color, int strokeWidth);

    World fillRectangle(Bounds bounds, Color color);

    World drawTextCentered(Vector position, String text, Pixelfont font, Percent opacity, double scale);

    default World drawTextCentered(final Vector position, final String text, final Pixelfont font,
                                   final Percent opacity) {
        return drawTextCentered(position, text, font, opacity, 1);
    }

    Bounds visibleArea();

    World drawRectangle(Bounds bounds, Rotation rotation, Color color);

    default World drawRectangle(final Bounds bounds, final Color color) {
        return drawRectangle(bounds, Rotation.none(), color);
    }
}