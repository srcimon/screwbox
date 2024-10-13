package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.Rendertarget;

public class SimpleUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds calculateBounds(final UiMenuItem item, final UiMenu menu, Size size) {
        final int itemIndex = menu.itemIndex(item);
        final int heightOfItem = 50;
        final int heightOfMenu = menu.itemCount() * heightOfItem;
        final int y = size.center().y() - heightOfMenu / 2;

        final var offset = Offset.at(0, y + itemIndex * heightOfItem);
        final var dimension = Size.of(size.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
