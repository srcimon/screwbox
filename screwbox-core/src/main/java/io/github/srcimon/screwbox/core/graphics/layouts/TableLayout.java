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
    public ScreenBounds calculateBounds(int index, int count, final int padding, ScreenBounds bounds) {
        final int rows = (int) Math.ceil(count * 1.0 / columns);
        final int column = index % columns;
        final int row = Math.floorDiv(index, columns);
        final int width = (bounds.width() - padding * (columns - 1)) / columns;
        final int height = (bounds.height() - padding * (rows - 1)) / rows;
        final var offset = Offset.at(column * width + column * padding, row * height + row * padding).add(bounds.offset());
        final var isLastViewport = index == count - 1;

        var widthToUse = fillEmptySpace && isLastViewport
                ? bounds.width() - offset.x() + bounds.offset().x()
                : width;

        final var filledHeight = isLastViewport
                ? bounds.height() - offset.y() + bounds.offset().y()
                : height;

        final var size = Size.of(widthToUse, filledHeight);
        return new ScreenBounds(offset, size);
    }
}