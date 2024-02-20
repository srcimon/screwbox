package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Size;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;
import static io.github.srcimon.screwbox.core.graphics.RectangleOptions.filled;

public class HorizontalLinesTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;
    private final int lineCount;

    public HorizontalLinesTransition(final int lineCount) {
        this.lineCount = lineCount;
    }

    @Override
    public void draw(final Screen screen, final Percent progress) {
        final int maxHeightPerLine = screen.size().height() / lineCount;
        for (int y = 0; y < screen.size().height(); y += maxHeightPerLine) {
            final Offset offset = Offset.at(0, y);
            final int height = (int) (maxHeightPerLine
                    - screen.size().height() / (double) lineCount * progress.value());
            final Size size = Size.of(screen.size().width(), height);
            screen.drawRectangle(offset, size, filled(BLACK));
        }
    }

}
