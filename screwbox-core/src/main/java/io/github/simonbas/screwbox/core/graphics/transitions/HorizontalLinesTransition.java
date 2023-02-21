package io.github.simonbas.screwbox.core.graphics.transitions;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.graphics.Dimension;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Screen;

import static io.github.simonbas.screwbox.core.graphics.Color.BLACK;

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
            final Dimension size = Dimension.of(screen.size().width(), height);
            screen.fillRectangle(offset, size, BLACK);
        }
    }

}
