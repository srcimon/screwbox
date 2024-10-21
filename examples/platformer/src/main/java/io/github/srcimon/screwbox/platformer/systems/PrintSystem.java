package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.platformer.components.TextComponent;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;

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