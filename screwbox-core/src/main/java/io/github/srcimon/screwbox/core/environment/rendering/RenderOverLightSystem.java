package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.Order;

@Order(Order.SystemOrder.PRESENTATION_ON_TOP_OF_LIGHT)
public class RenderOverLightSystem extends RenderSystem {


    protected boolean mustRender(final RenderComponent render) {
        return render.renderOverLight;
    }
}
