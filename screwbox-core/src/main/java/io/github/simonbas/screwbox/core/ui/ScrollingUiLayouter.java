package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.graphics.Dimension;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.graphics.WindowBounds;

public class ScrollingUiLayouter implements UiLayouter {

    @Override
    public WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = screen.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = Offset.at(0, y);
        var dimension = Dimension.of(screen.size().width(), heightOfItem);
        return new WindowBounds(offset, dimension);
    }

}
