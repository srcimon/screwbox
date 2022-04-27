package de.suzufa.screwbox.core.graphics.transitions;

import static de.suzufa.screwbox.core.graphics.window.WindowRectangle.rectangle;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.window.Window;

public class SwipeTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Window screen, final Percentage progress) {
        final int xMin = (int) (screen.size().width() * progress.value());
        final Dimension size = Dimension.of(screen.size().width() - xMin, screen.size().height());
        screen.draw(rectangle(Offset.at(xMin, 0), size, Color.BLACK));
    }

}