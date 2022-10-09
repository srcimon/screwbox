package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class SimpleUiRenderer implements UiRenderer {

    private static final Pixelfont NOT_SELECTED = Pixelfont.defaultFont(Color.WHITE);
    private static final Pixelfont SELECTED = Pixelfont.defaultFont(Color.YELLOW);

    @Override
    public void renderSelectableItem(UiMenuItem item, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), item.label(), NOT_SELECTED, 2);
    }

    @Override
    public void renderSelectedItem(UiMenuItem item, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), item.label(), SELECTED, 2.5);
    }

}
