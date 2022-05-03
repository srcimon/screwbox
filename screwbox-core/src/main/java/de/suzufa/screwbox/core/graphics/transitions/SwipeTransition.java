package de.suzufa.screwbox.core.graphics.transitions;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.window.Window;

public class SwipeTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Window window, final Percentage progress) {
        final int xMin = (int) (window.size().width() * progress.value());
        final Dimension size = Dimension.of(window.size().width() - xMin, window.size().height());
        window.drawRectangle(Offset.at(xMin, 0), size, Color.BLACK);
    }
}