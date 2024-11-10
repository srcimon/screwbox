package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;

/**
 * Layouts {@link Viewport viewports} on next to each other.
 */
public class HorizontalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(final int index, final int count, final ScreenBounds bounds, final int padding) {//TODO APPLY PADDING
        final int width = (int) (bounds.width() * 1.0 / count);
        final var offset = Offset.at(index * width, 0).add(bounds.offset());
        final var size = Size.of(width, bounds.height());
        return new ScreenBounds(offset, size);
    }

}
