package dev.screwbox.core.ui.presets;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.ui.UiRenderer;

import static dev.screwbox.core.assets.FontBundle.BOLDZILLA;
import static dev.screwbox.core.graphics.Color.WHITE;
import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

public class SimpleUiRenderer implements UiRenderer {

    private static final TextDrawOptions SELECTABLE = font(BOLDZILLA).scale(2).alignCenter();
    private static final TextDrawOptions SELECTED = font(BOLDZILLA.customColor(YELLOW)).scale(2.5).alignCenter();
    private static final TextDrawOptions INACTIVE = font(BOLDZILLA.customColor(WHITE.opacity(0.2))).scale(2).alignCenter();

    @Override
    public void renderSelectableItem(String label, ScreenBounds bounds, Canvas canvas) {
        canvas.drawText(bounds.center(), label, SELECTABLE);
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Canvas canvas) {
        canvas.drawText(bounds.center(), label, SELECTED);
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Canvas canvas) {
        canvas.drawText(bounds.center(), label, INACTIVE);
    }

}
