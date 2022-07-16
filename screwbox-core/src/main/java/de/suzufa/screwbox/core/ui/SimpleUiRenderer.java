package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class SimpleUiRenderer implements UiRenderer {

    @Override
    public void renderInactiveItem(UiMenuItem item, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), item.label(), Pixelfont.defaultWhite(), 2);
    }

    @Override
    public void renderActiveItem(UiMenuItem item, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), item.label(), Pixelfont.defaultWhite(), Percentage.half(), 2.5);
    }

}
