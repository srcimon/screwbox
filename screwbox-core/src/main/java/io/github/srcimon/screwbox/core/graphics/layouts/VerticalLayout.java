package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;

public class VerticalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(int index, int count, ScreenBounds bounds) {
        int height = (int) (bounds.height() / count * 1.0);
        var offset = Offset.at(0, index * height).add(bounds.offset());
        var size = Size.of(bounds.width(), height);
        return new ScreenBounds(offset, size);
    }
}
