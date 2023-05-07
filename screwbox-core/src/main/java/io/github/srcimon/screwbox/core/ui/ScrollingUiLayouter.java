package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.WindowBounds;

public class ScrollingUiLayouter implements UiLayouter {

    @Override
    public WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = screen.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = Offset.at(0, y);
        var dimension = Size.of(screen.size().width(), heightOfItem);
        return new WindowBounds(offset, dimension);
    }

}
