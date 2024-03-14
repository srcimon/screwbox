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
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

@Order(SystemOrder.PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private final Archetype sprites = Archetype.of(RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final SpriteBatch spriteBatch = new SpriteBatch();
        final Bounds visibleArea = engine.graphics().world().visibleArea();

        for (final Entity entity : engine.environment().fetchAll(sprites)) {
            final var entityPosition = entity.get(TransformComponent.class).bounds.position();
            final RenderComponent render = entity.get(RenderComponent.class);
            final var spriteDimension = render.sprite.size();
            final var spriteBounds = Bounds.atOrigin(
                    entityPosition.x() - spriteDimension.width() / 2.0,
                    entityPosition.y() - spriteDimension.height() / 2.0,
                    spriteDimension.width() * render.scale,
                    spriteDimension.height() * render.scale);

            if (spriteBounds.intersects(visibleArea)) {
                final var options = new SpriteDrawOptions(render.scale,render.opacity, render.rotation, render.flip);
                spriteBatch.addEntry(render.sprite, spriteBounds.origin(), options, render.drawOrder);
            }
        }
        engine.graphics().world().drawSpriteBatch(spriteBatch);
    }
}
