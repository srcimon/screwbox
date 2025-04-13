package dev.screwbox.core.ui.presets;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.ui.UiLayouter;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.UiMenuItem;

public class SimpleUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds layout(final UiMenuItem item, final UiMenu menu, final ScreenBounds renderArea) {
        final int itemIndex = menu.itemIndex(item);
        final int heightOfItem = 50;
        final int heightOfMenu = menu.itemCount() * heightOfItem;
        final int y = renderArea.center().y() - heightOfMenu / 2;

        final var offset = renderArea.offset().addY(y + itemIndex * heightOfItem);
        final var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
