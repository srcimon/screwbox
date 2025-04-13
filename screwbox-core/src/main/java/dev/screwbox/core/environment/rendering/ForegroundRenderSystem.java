package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.SpriteBatch;

import java.util.List;

@Order(Order.SystemOrder.PRESENTATION_FOREGROUND)
public class ForegroundRenderSystem extends RenderSystem {

    @Override
    public void update(final Engine engine) {
        final List<Entity> entities = fetchRenderEntities(engine);
        for (final var viewport : engine.graphics().viewports()) {
            final SpriteBatch spriteBatch = renderEntitiesOnViewport(viewport, entities, render -> render.renderInForeground);
            viewport.canvas().drawSpriteBatch(spriteBatch);
        }
    }

}
