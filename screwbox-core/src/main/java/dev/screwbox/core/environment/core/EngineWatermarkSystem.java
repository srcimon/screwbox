package dev.screwbox.core.environment.core;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Offset;

import static dev.screwbox.core.Percent.threeQuarter;
import static dev.screwbox.core.environment.Order.DEBUG_OVERLAY;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;


/**
 * Adds a watermark to the bottom of the screen which shows current engine version.
 */
@ExecutionOrder(DEBUG_OVERLAY)
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
