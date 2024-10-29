package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;
import io.github.srcimon.screwbox.core.utils.Validate;

//TODO ViewportLayoutBundle
public class TableLayout implements ViewportLayout {

    public final int columns;

    public TableLayout() {
        this(2);
    }

    public TableLayout(final int columns) {
        Validate.positive(columns, "columns must be positive");
        this.columns = columns;
    }

    @Override
    public ScreenBounds calculateBounds(int index, int count, ScreenBounds bounds) {
        int width = (int) (bounds.width() / columns * 1.0);
        int rows = (int) Math.ceil(count * 1.0 / columns);
        int height = (int) (bounds.height() / rows * 1.0);
        int column = index % columns;
        int row = Math.floorDiv(index, columns);

        var offset = Offset.at(column * width, row * height);
        var size = index == count - 1
                ? Size.of((columns - column) * width, height)
                : Size.of(width, height);

        return new ScreenBounds(offset, size);
    }


}
