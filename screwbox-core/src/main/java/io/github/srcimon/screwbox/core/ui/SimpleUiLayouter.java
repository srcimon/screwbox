package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;

public class SimpleUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds calculateBounds(final UiMenuItem item, final UiMenu menu, final ScreenBounds renderArea) {
        final int itemIndex = menu.itemIndex(item);
        final int heightOfItem = 50;
        final int heightOfMenu = menu.itemCount() * heightOfItem;
        final int y = renderArea.center().y() - heightOfMenu / 2;

        final var offset = renderArea.offset().addY(y + itemIndex * heightOfItem);
        final var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
