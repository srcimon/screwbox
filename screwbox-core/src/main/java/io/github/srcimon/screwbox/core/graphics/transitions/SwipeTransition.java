package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Size;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.filled;

public class SwipeTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    @Override
    public void draw(final Screen screen, final Percent progress) {
        final int xMin = (int) (screen.size().width() * progress.value());
        final Size size = Size.of(screen.size().width() - xMin, screen.size().height());
        screen.drawRectangle(Offset.at(xMin, 0), size, filled(BLACK));
    }
}