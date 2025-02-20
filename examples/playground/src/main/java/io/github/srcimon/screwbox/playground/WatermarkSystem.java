package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import static io.github.srcimon.screwbox.core.Percent.threeQuarter;

@Order(Order.SystemOrder.DEBUG_OVERLAY)
public class WatermarkSystem implements EntitySystem {

    private static final int PADDING = 14;

    @Override
    public void update(Engine engine) {
        final var options = TextDrawOptions.font(FontBundle.BOLDZILLA).opacity(threeQuarter());
        final var offset = Offset.at(PADDING, engine.graphics().screen().size().height() - PADDING);
        final String text = "Running on ScrewBox pure Java game engine - version %s - visit ScrewBox.dev".formatted(engine.version());
        engine.graphics().canvas().drawText(offset, text, options);
    }
}
