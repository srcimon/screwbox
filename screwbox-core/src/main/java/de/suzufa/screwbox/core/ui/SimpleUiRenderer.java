package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class SimpleUiRenderer implements UiRenderer {

    private static final Pixelfont NOT_SELECTED = Pixelfont.defaultFont(Color.WHITE);
    private static final Pixelfont SELECTED = Pixelfont.defaultFont(Color.YELLOW);
    private static final Pixelfont INACTIVE = Pixelfont.defaultFont(Color.WHITE.opacity(0.2));

    @Override
    public void renderSelectableItem(String label, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), label, NOT_SELECTED, 2);
    }

    @Override
    public void renderSelectedItem(String label, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), label, SELECTED, 2.5);
    }

    @Override
    public void renderInactiveItem(String label, WindowBounds bounds, Window window) {
        window.drawTextCentered(bounds.center(), label, INACTIVE, 2);
    }

}
