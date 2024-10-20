package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;

public class ScrollingUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds calculateBounds(UiMenuItem item, UiMenu menu, ScreenBounds renderArea) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = renderArea.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = renderArea.offset().addY(y);
        var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
