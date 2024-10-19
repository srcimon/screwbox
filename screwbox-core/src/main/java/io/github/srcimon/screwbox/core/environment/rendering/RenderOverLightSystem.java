package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

import java.util.List;

@Order(Order.SystemOrder.PRESENTATION_ON_TOP_OF_LIGHT)
public class RenderOverLightSystem extends RenderSystem {

    @Override
    protected boolean mustRenderEntity(final RenderComponent render) {
        return render.renderOverLight;
    }

    @Override
    protected void drawReflections(Engine engine, List<Entity> renderEntities, SpriteBatch spriteBatch, Graphics graphics) {

    }
}
