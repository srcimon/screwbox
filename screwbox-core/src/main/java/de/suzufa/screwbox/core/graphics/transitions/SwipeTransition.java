package de.suzufa.screwbox.core.graphics.transitions;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Screen;

public class SwipeTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        final int xMin = (int) (screen.size().width() * progress.value());
        final Dimension size = Dimension.of(screen.size().width() - xMin, screen.size().height());
        screen.fillRectangle(Offset.at(xMin, 0), size, Color.BLACK);
    }
}