package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;

import java.util.List;

//TODO ViewportLayoutBundle
public class VerticalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(int viewportId, int viewportCount, ScreenBounds bounds) {
        int height = (int) (bounds.height() / viewportCount * 1.0);
        var offset = Offset.at(0, viewportId * height).add(bounds.offset());
        var size = Size.of(bounds.width(), height);
        return new ScreenBounds(offset, size);
    }


}
