package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Viewport;

import java.util.List;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;

/**
 * Renders {@link Entity entities} having a {@link RenderComponent} and also adds reflections for {@link Entity entities}
 * having a {@link ReflectionComponent}.
 */
@ExecutionOrder(PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.ofSpacial(RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> entities = engine.environment().fetchAll(RENDERS);
        for (final var viewport : engine.graphics().viewports()) {
            renderEntitiesOnViewport(viewport, entities);
        }
    }

    protected void renderEntitiesOnViewport(final Viewport viewport, final List<Entity> entities) {
        final double zoom = viewport.camera().zoom();
        final Canvas canvas = viewport.canvas();
        final ScreenBounds visibleBounds = new ScreenBounds(canvas.size());
        for (final Entity entity : entities) {
            final RenderComponent render = entity.get(RenderComponent.class);
            final double width = render.sprite.width() * render.options.scale();
            final double height = render.sprite.height() * render.options.scale();
            final var spriteBounds = Bounds.atPosition(entity.position(), width, height);

            final var entityScreenBounds = viewport.toCanvas(spriteBounds, render.parallaxX, render.parallaxY);
            if (visibleBounds.intersects(entityScreenBounds)) {
                final double zIndex = render.isSortOrthographic ? entityScreenBounds.maxY() : 0;
                canvas.drawSprite(render.sprite, entityScreenBounds.offset(), render.options
                        .scale(render.options.scale() * zoom)
                        .drawOrder(render.drawOrder)
                        .zIndex(zIndex));
            }
        }
    }

}
