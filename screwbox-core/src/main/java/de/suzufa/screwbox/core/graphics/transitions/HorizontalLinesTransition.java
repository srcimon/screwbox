package de.suzufa.screwbox.core.graphics.transitions;

import static de.suzufa.screwbox.core.graphics.Color.BLACK;
import static de.suzufa.screwbox.core.graphics.window.WindowRectangle.rectangle;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.window.Window;

public class HorizontalLinesTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;
    private final int lineCount;

    public HorizontalLinesTransition(final int lineCount) {
        this.lineCount = lineCount;
    }

    @Override
    public void draw(final Window window, final Percentage progress) {
        final int maxHeightPerLine = window.size().height() / lineCount;
        for (int y = 0; y < window.size().height(); y += maxHeightPerLine) {
            final Offset offset = Offset.at(0, y);
            final int height = (int) (maxHeightPerLine
                    - window.size().height() / (double) lineCount * progress.value());
            final Dimension size = Dimension.of(window.size().width(), height);
            window.draw(rectangle(offset, size, BLACK));
        }
    }

}
