package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Dimension;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

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
    public void draw(final Screen screen, final Percent progress) {
        final long offsetCountToDraw = mosaikOffsets.size() - Math.round((mosaikOffsets.size() - 1) * progress.value());
        final int mosaikWidth = screen.size().width() / columns + 1;
        final int mosaikHeight = screen.size().height() / rows + 1;
        final Dimension mosaikDimension = Dimension.of(mosaikWidth, mosaikHeight);
        for (int i = 0; i < offsetCountToDraw; i++) {
            final Offset mosaikOffset = mosaikOffsets.get(i);
            final Offset screenOffset = Offset.at(mosaikOffset.x() * mosaikWidth, mosaikOffset.y() * mosaikHeight);
            screen.fillRectangle(screenOffset, mosaikDimension, Color.BLACK);
        }
    }

}
