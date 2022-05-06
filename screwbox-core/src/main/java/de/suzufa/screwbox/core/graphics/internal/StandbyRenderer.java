package de.suzufa.screwbox.core.graphics.internal;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.window.WindowRepeatingSprite;
import de.suzufa.screwbox.core.graphics.window.WindowText;

public class StandbyRenderer implements Renderer {

    @Override
    public void updateScreen(final boolean antialiased) {
        // does nothing
    }

    @Override
    public Sprite takeScreenshot() {
        return null;
    }

    @Override
    public int calculateTextWidth(final String text, final Font font) {
        return 0;
    }

    @Override
    public void draw(final WindowText text) {
        // does nothing
    }

    @Override
    public void draw(final WindowRepeatingSprite repeatingSprite) {
        // does nothing
    }

    @Override
    public void draw(final WindowLine line) {
        // does nothing
    }

    @Override
    public void draw(final WindowPolygon polygon) {
        // does nothing
    }

    @Override
    public void fillWith(final Color color) {
        // does nothing
    }

    @Override
    public void drawRectangle(final WindowBounds bounds, final Color color) {
        // does nothing
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percentage opacity,
            final Rotation rotation) {
        // does nothing
    }

}
