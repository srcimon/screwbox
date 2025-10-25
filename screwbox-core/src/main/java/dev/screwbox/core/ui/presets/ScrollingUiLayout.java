package dev.screwbox.core.ui.presets;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.ui.UiLayout;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.UiMenuItem;

public class ScrollingUiLayout implements UiLayout {

    @Override
    public ScreenBounds layout(final UiMenuItem item, final UiMenu menu, final ScreenBounds bounds) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = bounds.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = Offset.at(0, y);
        var dimension = Size.of(bounds.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
