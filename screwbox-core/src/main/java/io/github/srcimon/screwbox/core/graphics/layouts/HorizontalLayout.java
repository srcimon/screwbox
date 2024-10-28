package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;

//TODO ViewportLayoutBundle
public class HorizontalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(int index, int count, ScreenBounds bounds) {
        int witdht = (int) (bounds.width() / count * 1.0);
        var offset = Offset.at(index * witdht, 0).add(bounds.offset());
        var size = Size.of(witdht, bounds.height());

        return new ScreenBounds(offset, size);
    }

}
