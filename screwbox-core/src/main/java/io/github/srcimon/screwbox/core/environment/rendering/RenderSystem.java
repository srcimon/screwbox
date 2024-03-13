package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;
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
                final var options = SpriteDrawOptions
                        .scaled(render.scale)
                        .opacity(render.opacity)
                        .opacity(render.opacity)
                        .flip(render.flip);

                spriteBatch.addEntry(render.sprite, spriteBounds.origin(), options, render.drawOrder);
            }
        }
        final var world = engine.graphics().world();
//TODO renderComponent.options?
        for (final SpriteBatch.SpriteBatchEntry entry : spriteBatch.entriesInDrawOrder()) {
            world.drawSprite(entry.sprite(), entry.position(), entry.options());
        }
    }
}
