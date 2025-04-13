package dev.screwbox.core.ui.presets;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.ui.UiLayouter;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.UiMenuItem;

public class ScrollingUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds layout(final UiMenuItem item, final UiMenu menu, final ScreenBounds renderArea) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = renderArea.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = Offset.at(0, y);
        var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
