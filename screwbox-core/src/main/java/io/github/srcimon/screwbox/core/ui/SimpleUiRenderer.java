package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.FontsBundle;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.TextDrawOptions;

import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;

public class SimpleUiRenderer implements UiRenderer {

    private static final Asset<Pixelfont> NOT_SELECTED = FontsBundle.BOLDZILLA.white();
    private static final Asset<Pixelfont> SELECTED = FontsBundle.BOLDZILLA.customColor(YELLOW);
    private static final Asset<Pixelfont> INACTIVE = FontsBundle.BOLDZILLA.customColor(WHITE.opacity(0.2));

    @Override
    public void renderSelectableItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawText(bounds.center(), label, TextDrawOptions.font(NOT_SELECTED).scale(2));
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawText(bounds.center(), label, TextDrawOptions.font(SELECTED).scale(2.5));
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawText(bounds.center(), label, TextDrawOptions.font(INACTIVE).scale(2));
    }

}
