package dev.screwbox.core.graphics.layouts;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.ViewportLayout;

/**
 * Layouts {@link Viewport viewports} on top of each other.
 */
public class VerticalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(final int index, final int count, final int padding, final ScreenBounds bounds) {
        final int totalPadding = padding * (count - 1);
        final int paddingToTheTop = index * padding;
        final int height = (bounds.height() - totalPadding) / count;
        final var offset = Offset.at(0, paddingToTheTop + index * height).add(bounds.offset());
        final var filledHeight = index == count - 1
                ? bounds.height() - offset.y() + bounds.offset().y()
                : height;

        final var size = Size.of(bounds.width(), filledHeight);

        return new ScreenBounds(offset, size);
    }
}
