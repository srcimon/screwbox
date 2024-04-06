package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.assets.FontsBundle;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;

public class SimpleUiRenderer implements UiRenderer {

    private static final Pixelfont NOT_SELECTED = FontsBundle.BOLDZILLA.getWhite();
    private static final Pixelfont SELECTED = FontsBundle.BOLDZILLA.getCustomColor(YELLOW);
    private static final Pixelfont INACTIVE = FontsBundle.BOLDZILLA.getCustomColor(WHITE.opacity(0.2));

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
