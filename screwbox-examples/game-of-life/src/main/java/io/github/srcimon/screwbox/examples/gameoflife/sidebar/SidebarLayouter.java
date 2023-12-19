package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiMenuItem;

public class SidebarLayouter implements UiLayouter {

    @Override
    public ScreenBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen) {
        var index = menu.itemIndex(item);
        return new ScreenBounds(20, 30 * index + 30, 200, 30);
    }
}
