package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

@Order(Order.SystemOrder.PRESENTATION_ON_TOP_OF_LIGHT)
public class RenderOverLightSystem extends RenderSystem {


    @Override
    public void update(final Engine engine) {
        final SpriteBatch spriteBatch = createRenderBatch(engine, render -> render.renderOverLight);
        engine.graphics().canvas().drawSpriteBatch(spriteBatch);
    }
}
