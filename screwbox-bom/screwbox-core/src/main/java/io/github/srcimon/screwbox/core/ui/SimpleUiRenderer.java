package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

public class SimpleUiRenderer implements UiRenderer {

    private static final Pixelfont NOT_SELECTED = Pixelfont.defaultFont(Color.WHITE);
    private static final Pixelfont SELECTED = Pixelfont.defaultFont(Color.YELLOW);
    private static final Pixelfont INACTIVE = Pixelfont.defaultFont(Color.WHITE.opacity(0.2));

    @Override
    public void renderSelectableItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawTextCentered(bounds.center(), label, NOT_SELECTED, 2);
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawTextCentered(bounds.center(), label, SELECTED, 2.5);
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawTextCentered(bounds.center(), label, INACTIVE, 2);
    }

}
