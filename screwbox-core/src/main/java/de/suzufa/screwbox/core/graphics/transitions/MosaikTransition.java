package de.suzufa.screwbox.core.graphics.transitions;

import static java.util.Collections.shuffle;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.window.Window;

public final class MosaikTransition implements ScreenTransition {

    private static final long serialVersionUID = 1L;

    private final List<Offset> mosaikOffsets = new ArrayList<>();
    private final int rows;
    private final int columns;

    public MosaikTransition(final int columns, final int rows) {
        this.rows = rows;
        this.columns = columns;
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                mosaikOffsets.add(Offset.at(x, y));
            }
        }
        shuffle(mosaikOffsets);
    }

    @Override
    public void draw(final Window window, final Percentage progress) {
        final long offsetCountToDraw = mosaikOffsets.size() - Math.round((mosaikOffsets.size() - 1) * progress.value());
        final int mosaikWidth = window.size().width() / columns + 1;
        final int mosaikHeight = window.size().height() / rows + 1;
        final Dimension mosaikDimension = Dimension.of(mosaikWidth, mosaikHeight);
        for (int i = 0; i < offsetCountToDraw; i++) {
            final Offset mosaikOffset = mosaikOffsets.get(i);
            final Offset screenOffset = Offset.at(mosaikOffset.x() * mosaikWidth, mosaikOffset.y() * mosaikHeight);
            window.drawRectangle(screenOffset, mosaikDimension, Color.BLACK);
        }
    }

}
