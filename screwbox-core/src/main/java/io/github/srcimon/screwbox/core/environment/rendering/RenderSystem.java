package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

@Order(SystemOrder.PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private final Archetype sprites = Archetype.of(RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final SpriteBatch spriteBatch = new SpriteBatch();
        final Bounds visibleArea = engine.graphics().world().visibleArea();

        for (final Entity entity : engine.environment().fetchAll(sprites)) {
            final var entityPosition = entity.position();
            final RenderComponent render = entity.get(RenderComponent.class);
            final double width = render.sprite.size().width() * render.options.scale();
            final double height = render.sprite.size().height() * render.options.scale();
            final var spriteBounds = Bounds.atPosition(
                    entityPosition.x(),
                    entityPosition.y(),
                    width,
                    height);

            if (spriteBounds.intersects(visibleArea)) {
                spriteBatch.addEntry(render.sprite, spriteBounds.origin(), render.options, render.drawOrder);
            }
        }
        engine.graphics().world().drawSpriteBatch(spriteBatch);
    }
}
