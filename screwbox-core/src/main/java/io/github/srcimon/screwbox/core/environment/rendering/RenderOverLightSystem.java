package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

import java.util.List;

@Order(Order.SystemOrder.PRESENTATION_ON_TOP_OF_LIGHT)
public class RenderOverLightSystem extends RenderSystem {

    @Override
    public void update(final Engine engine) {
        final List<Entity> entities = fetchRenderEntities(engine);
        for (final var viewport : engine.graphics().viewports()) {
            final SpriteBatch spriteBatch = renderEntitiesOnViewport(viewport, entities, render -> render.renderOverLight);
            viewport.canvas().drawSpriteBatch(spriteBatch);
        }
    }

}
