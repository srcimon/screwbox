package io.github.simonbas.screwbox.core.graphics.transitions;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Dimension;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Screen;

public class SwipeTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        final int xMin = (int) (screen.size().width() * progress.value());
        final Dimension size = Dimension.of(screen.size().width() - xMin, screen.size().height());
        screen.fillRectangle(Offset.at(xMin, 0), size, Color.BLACK);
    }
}