package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiMenuItem;

public class SimpleUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds layout(final UiMenuItem item, final UiMenu menu, final ScreenBounds renderArea) {
        final int itemIndex = menu.itemIndex(item);
        final int heightOfItem = 50;
        final int heightOfMenu = menu.itemCount() * heightOfItem;
        final int y = renderArea.center().y() - heightOfMenu / 2;

        final var offset = Offset.at(0, y + itemIndex * heightOfItem);
        final var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
