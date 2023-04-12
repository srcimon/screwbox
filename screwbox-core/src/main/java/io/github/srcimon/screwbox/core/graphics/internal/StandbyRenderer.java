package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Angle;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.*;

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
    public void fillRectangle(final WindowBounds bounds, final Color color) {
        // does nothing
    }

    @Override
    public void fillCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
                           final Angle rotation, final Flip flip, final WindowBounds clipArea) {
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
    public void drawFadingCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(final Asset<Sprite> sprite, final Offset origin, final double scale, final Percent opacity,
                           final Angle rotation,
                           final Flip flip, final WindowBounds clipArea) {
        // does nothing
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

}
