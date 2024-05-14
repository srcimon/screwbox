package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

@Order(Order.SystemOrder.PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.of(RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final SpriteBatch spriteBatch = new SpriteBatch();
        final Graphics graphics = engine.graphics();
        final ScreenBounds visibleBounds = graphics.screen().bounds();
        double zoom = graphics.camera().zoom();

        for (final Entity entity : engine.environment().fetchAll(RENDERS)) {
            final RenderComponent render = entity.get(RenderComponent.class);
            if (mustRenderEntity(render)) {
                final double width = render.sprite.size().width() * render.options.scale();
                final double height = render.sprite.size().height() * render.options.scale();
                final var spriteBounds = Bounds.atPosition(entity.position(), width, height);

                final var entityScreenBounds = graphics.toScreenUsingParallax(spriteBounds, render.parallaxX, render.parallaxY);
                if (visibleBounds.intersects(entityScreenBounds)) {
                    spriteBatch.add(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom), render.drawOrder);
                }
            }
        }

        graphics.screen().drawSpriteBatch(spriteBatch);
    }

    protected boolean mustRenderEntity(final RenderComponent render) {
        return !render.renderOverLight;
    }
}
