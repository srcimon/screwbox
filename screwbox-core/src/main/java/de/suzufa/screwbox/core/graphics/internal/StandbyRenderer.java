package de.suzufa.screwbox.core.graphics.internal;

import java.util.concurrent.Future;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Flip;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

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
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
            final Angle rotation, final Flip flip, WindowBounds clipArea) {
        // does nothing
    }

    @Override
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        // does nothing
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final Color color) {
        // does nothing
    }

    @Override
    public void drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        // does nothing
    }

    @Override
    public void drawFadingCircle(Offset offset, int diameter, Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(Future<Sprite> sprite, Offset origin, double scale, Percent opacity, Angle rotation,
            Flip flip, WindowBounds clipArea) {
        // does nothing
    }

}
