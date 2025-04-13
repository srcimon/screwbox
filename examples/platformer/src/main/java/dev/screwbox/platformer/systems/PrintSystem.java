package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.platformer.components.TextComponent;

import static dev.screwbox.core.assets.FontBundle.BOLDZILLA;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

@Order(Order.SystemOrder.PRESENTATION_UI)
public class PrintSystem implements EntitySystem {

    private static final Archetype TEXTS = Archetype.of(TextComponent.class);

    @Override
    public void update(final Engine engine) {
        for (var entity : engine.environment().fetchAll(TEXTS)) {
            TextComponent textComponent = entity.get(TextComponent.class);
            Canvas canvas = engine.graphics().canvas();
            canvas.drawText(canvas.center(), textComponent.text, font(BOLDZILLA).alignCenter().scale(7));
            canvas.drawText(canvas.center().addY(80), textComponent.subtext, font(BOLDZILLA).alignCenter().scale(4));
        }
    }
}