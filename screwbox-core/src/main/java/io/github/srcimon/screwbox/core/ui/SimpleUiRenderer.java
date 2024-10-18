package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;

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
