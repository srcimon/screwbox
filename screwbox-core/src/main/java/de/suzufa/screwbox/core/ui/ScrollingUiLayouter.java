package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.window.Window;

public class ScrollingUiLayouter implements UiLayouter {

    @Override
    public WindowBounds screenBoundsOf(UiMenuItem item, UiMenu menu, Window screen) {
        int itemIndex = menu.itemIndex(item);
        int heightOfItem = 50;
        int y = screen.center().y() + (itemIndex - menu.activeItemIndex()) * heightOfItem;

        var offset = Offset.at(0, y);
        var dimension = Dimension.of(screen.size().width(), heightOfItem);
        return new WindowBounds(offset, dimension);
    }

}
