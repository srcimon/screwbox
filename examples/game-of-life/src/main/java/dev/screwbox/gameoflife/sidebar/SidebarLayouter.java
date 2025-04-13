package dev.screwbox.gameoflife.sidebar;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.ui.UiLayouter;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.UiMenuItem;

public class SidebarLayouter implements UiLayouter {

    @Override
    public ScreenBounds layout(final UiMenuItem item, final UiMenu menu, final ScreenBounds renderArea) {
        var index = menu.itemIndex(item);
        return new ScreenBounds(20, 30 * index + 30, 200, 30);
    }
}
