package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.platformer.components.BackgroundComponent;

import java.util.Comparator;
import java.util.List;

@Order(Order.SystemOrder.PRESENTATION_BACKGROUND)
public class BackgroundSystem implements EntitySystem {

    private static final Archetype BACKGROUNDS = Archetype.of(BackgroundComponent.class, RenderComponent.class);

    private static final Comparator<Entity> BACKGROUND_COMPARATOR = Comparator.comparingDouble(o -> o.get(RenderComponent.class).drawOrder);

    @Override
    public void update(final Engine engine) {

        final List<Entity> backgroundEntities = engine.environment().fetchAll(BACKGROUNDS);
        backgroundEntities.sort(BACKGROUND_COMPARATOR);
        for (final var entity : backgroundEntities) {
            final var background = entity.get(BackgroundComponent.class);
            final var sprite = entity.get(RenderComponent.class);

            final SpriteFillOptions options = SpriteFillOptions.scale(background.zoom).opacity(sprite.options.opacity());
            for (final var viewport : engine.graphics().activeViewports()) {
                final var cameraPosition = viewport.camera().position();
                final Offset offset = Offset.at(
                        cameraPosition.x() * -1 * (background.parallaxX - 1),
                        cameraPosition.y() * -1 * (background.parallaxY - 1));
                viewport.canvas().fillWith(sprite.sprite, options.offset(offset));
            }
        }
    }
}
