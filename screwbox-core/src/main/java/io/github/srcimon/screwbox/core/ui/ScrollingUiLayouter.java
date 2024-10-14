package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.internal.Rendertarget;

public class ScrollingUiLayouter implements UiLayouter {

    @Override
    public ScreenBounds calculateBounds(UiMenuItem item, UiMenu menu, ScreenBounds renderArea) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = renderArea.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = Offset.at(0, y);
        var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
