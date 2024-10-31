package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;
import io.github.srcimon.screwbox.core.utils.Validate;

/**
 * Layouts {@link Viewport viewports} in colums. Number of colums can be specified.
 */
public class TableLayout implements ViewportLayout {

    public final int columns;
    private final boolean fillEmptySpace;

    /**
     * Layouts in two columns and fills the empty space if last row has only one {@link Viewport}.
     */
    public TableLayout() {
        this(2, true);
    }

    /**
     * Layouts in the specified number of columns.
     *
     * @param columns        specifies the number of columns
     * @param fillEmptySpace specifies if the last {@link Viewport} will be stretched so that there is no remaining empty space
     */
    public TableLayout(final int columns, final boolean fillEmptySpace) {
        Validate.positive(columns, "columns must be positive");
        this.columns = columns;
        this.fillEmptySpace = fillEmptySpace;
    }

    @Override
    public ScreenBounds calculateBounds(int index, int count, ScreenBounds bounds) {
        final int width = (int) (bounds.width() * 1.0 / columns );
        final int rows = (int) Math.ceil(count * 1.0 / columns);
        final int height = (int) (bounds.height() * 1.0 / rows );
        final int column = index % columns;
        final int row = Math.floorDiv(index, columns);

        final var offset = Offset.at(column * width, row * height);
        final var isLastViewport = index == count - 1;
        final var widthToUse = fillEmptySpace && isLastViewport
                ? (columns - column) * width
                : width;

        final var size = Size.of(widthToUse, height);
        return new ScreenBounds(offset, size);
    }


}
