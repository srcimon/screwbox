package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Offset;

import static io.github.srcimon.screwbox.core.Percent.threeQuarter;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.DEBUG_OVERLAY;
import static io.github.srcimon.screwbox.core.graphics.options.TextDrawOptions.font;


/**
 * Adds a watermark to the bottom of the screen which shows current engine version.
 */
@Order(DEBUG_OVERLAY)
public class EngineWatermarkSystem implements EntitySystem {

    private static final String TEXT_TEMPLATE = "Running on ScrewBox game engine - version %s - visit ScrewBox.dev";
    private static final int PADDING = 14;

    @Override
    public void update(final Engine engine) {
        final var canvas = engine.graphics().canvas();
        final var offset = Offset.at(PADDING, canvas.height() - PADDING);
        final String text = TEXT_TEMPLATE.formatted(engine.version());
        canvas.drawText(offset, text, font(FontBundle.BOLDZILLA).opacity(threeQuarter()));
    }
}
