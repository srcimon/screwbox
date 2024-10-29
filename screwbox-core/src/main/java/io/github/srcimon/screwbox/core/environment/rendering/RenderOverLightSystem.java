package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.Viewport;

@Order(Order.SystemOrder.PRESENTATION_ON_TOP_OF_LIGHT)
public class RenderOverLightSystem extends RenderSystem {


    protected boolean mustRender(final RenderComponent render) {
        return render.renderOverLight;
    }

    //TODO mae the overwrite more elegant
    protected void addReflectionsToSpriteBatch(final Engine engine, final Viewport viewport, final SpriteBatch spriteBatch) {
        // does nothing
    }
}
