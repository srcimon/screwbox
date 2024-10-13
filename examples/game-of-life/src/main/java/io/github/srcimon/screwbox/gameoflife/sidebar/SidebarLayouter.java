package io.github.srcimon.screwbox.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiMenuItem;

public class SidebarLayouter implements UiLayouter {

    @Override
    public ScreenBounds calculateBounds(final UiMenuItem item, final UiMenu menu, final Size size) {
        var index = menu.itemIndex(item);
        return new ScreenBounds(20, 30 * index + 30, 200, 30);
    }
}
