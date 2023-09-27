package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.entities.*;

@Order(SystemOrder.PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private final Archetype sprites = Archetype.of(RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final SpriteBatch spriteBatch = new SpriteBatch();
        final Bounds visibleArea = engine.graphics().world().visibleArea();

        for (final Entity entity : engine.entities().fetchAll(sprites)) {
            final var entityPosition = entity.get(TransformComponent.class).bounds.position();
            final RenderComponent render = entity.get(RenderComponent.class);
            final var sprite = render.sprite;
            final var spriteDimension = sprite.size();
            final var spriteBounds = Bounds.atOrigin(
                    entityPosition.x() - spriteDimension.width() / 2.0 + render.displacement.x(),
                    entityPosition.y() - spriteDimension.height() / 2.0 + render.displacement.y(),
                    spriteDimension.width() * render.scale,
                    spriteDimension.height() * render.scale);

            if (spriteBounds.intersects(visibleArea)) {
                spriteBatch.addEntry(
                        render.sprite,
                        spriteBounds.origin().add(render.displacement),
                        render.scale,
                        render.opacity,
                        render.rotation,
                        render.flip,
                        render.drawOrder);
            }
        }
        engine.graphics().world().drawSpriteBatch(spriteBatch);
    }
}
