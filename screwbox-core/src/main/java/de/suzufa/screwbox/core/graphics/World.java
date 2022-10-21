package de.suzufa.screwbox.core.graphics;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public interface World {

    World drawColor(Color color);

    Color drawColor();

    World drawFadingCircle(Vector position, double diameter, Color color);

    World drawSpriteBatch(SpriteBatch spriteBatch, Bounds clipArea);

    default World drawSpriteBatch(final SpriteBatch spriteBatch) {
        return drawSpriteBatch(spriteBatch, null);
    }

    World drawSprite(Sprite sprite, Vector origin, double scale, Percentage opacity, Angle rotation,
            FlipMode flipMode, Bounds clipArea);

    default World drawSprite(final Sprite sprite, final Vector origin, final Percentage opacity,
            final Angle rotation, final FlipMode flipMode) {
        return drawSprite(sprite, origin, 1, opacity, rotation, flipMode, null);
    }

    default World drawSprite(final Sprite sprite, final Vector origin, final Percentage opacity) {
        return drawSprite(sprite, origin, opacity, Angle.none(), FlipMode.NONE);
    }

    default World drawSprite(final Sprite sprite, final Vector origin) {
        return drawSprite(sprite, origin, Percentage.max());
    }

    World drawText(Vector offset, String text, Font font, Color color);

    default World drawText(final Vector offset, final String text, final Font font) {
        return drawText(offset, text, font, drawColor());
    }

    World drawTextCentered(Vector position, String text, Font font, Color color);

    default World drawTextCentered(final Vector position, final String text, final Font font) {
        return drawTextCentered(position, text, font, drawColor());
    }

    World drawLine(Vector from, Vector to, Color color);

    default World drawLine(final Segment line, final Color color) {
        return drawLine(line.from(), line.to(), color);
    }

    default World drawLine(final Segment line) {
        return drawLine(line, drawColor());
    }

    default World drawLine(final Vector from, final Vector to) {
        return drawLine(from, to, drawColor());
    }

    World drawCircle(Vector position, int diameter, Color color);

    default World drawCircle(final Vector position, final int diameter) {
        return drawCircle(position, diameter, drawColor());
    }

    World drawRectangle(Bounds bounds, Color color);

    default World drawRectangle(final Bounds bounds) {
        return drawRectangle(bounds, drawColor());
    }

    World drawTextCentered(Vector position, String text, Pixelfont font, Percentage opacity, double scale);

    default World drawTextCentered(final Vector position, final String text, final Pixelfont font,
            final Percentage opacity) {
        return drawTextCentered(position, text, font, opacity, 1);
    }

    Bounds visibleArea();

}