package dev.screwbox.core.graphics.layouts;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.ViewportLayout;

/**
 * Layouts {@link Viewport viewports} on next to each other.
 */
public class HorizontalLayout implements ViewportLayout {

    @Override
    public ScreenBounds calculateBounds(final int index, final int count, final int padding, final ScreenBounds bounds) {
        final int totalPadding = padding * (count - 1);
        final int paddingToTheLeft = index * padding;
        final int width = (bounds.width() - totalPadding) / count;
        final var offset = Offset.at(paddingToTheLeft + index * width, 0).add(bounds.offset());
        final var filledWidth = index == count - 1
                ? bounds.width() - offset.x() + bounds.offset().x()
                : width;
        final var size = Size.of(filledWidth, bounds.height());
        return new ScreenBounds(offset, size);
    }

}
