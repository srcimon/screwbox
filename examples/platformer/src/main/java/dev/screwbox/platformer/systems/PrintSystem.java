package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.platformer.components.TextComponent;

import static dev.screwbox.core.assets.FontBundle.BOLDZILLA;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

@Order(Order.SystemOrder.PRESENTATION_UI)
public class PrintSystem implements EntitySystem {

    private static final Archetype TEXTS = Archetype.of(TextComponent.class);
    private static final TextDrawOptions TITLE = font(BOLDZILLA).alignCenter().scale(7);
    private static final TextDrawOptions SUBTITLE = font(BOLDZILLA).alignCenter().scale(4);

    @Override
    public void update(final Engine engine) {
        for (var entity : engine.environment().fetchAll(TEXTS)) {
            if (engine.graphics().isWithinDistanceToVisibleArea(entity.position(), 1024)) {
                TextComponent textComponent = entity.get(TextComponent.class);
                Canvas canvas = engine.graphics().canvas();
                canvas.drawText(canvas.center(), textComponent.text, TITLE);
                canvas.drawText(canvas.center().addY(80), textComponent.subtext, SUBTITLE);
            }
        }
    }
}