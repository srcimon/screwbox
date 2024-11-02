package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;

/**
 * Layouts {@link Viewport viewports} on top of each other.
 */
public class VerticalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(final int index, final int count, final ScreenBounds bounds) {
        final int height = (int) (bounds.height() * 1.0 / count);
        final var offset = Offset.at(0, index * height).add(bounds.offset());
        final var size = Size.of(bounds.width(), height);
        return new ScreenBounds(offset, size);
    }
}
