package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Font.Style;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class SimpleUiRenderer implements UiRenderer {

    private static final Font FONT_NOT_SELECTED = new Font("Arial", 20, Style.BOLD);
    private static final Font FONT_SELECTED = FONT_NOT_SELECTED.withSize(26);

    @Override
    public void renderInactiveItem(UiMenuItem item, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), item.label(), FONT_NOT_SELECTED, Color.WHITE);
    }

    @Override
    public void renderActiveItem(UiMenuItem item, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), item.label(), FONT_SELECTED, Color.YELLOW);
    }

}
