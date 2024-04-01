package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.SpriteFillOptions;
import io.github.srcimon.screwbox.examples.platformer.components.BackgroundComponent;

import java.util.Comparator;
import java.util.List;

@Order(SystemOrder.PRESENTATION_BACKGROUND)
public class BackgroundSystem implements EntitySystem {

    private static final Archetype BACKGROUNDS = Archetype.of(BackgroundComponent.class, RenderComponent.class);

    private static final Comparator<Entity> BACKGROUND_COMPARATOR = Comparator.comparingDouble(o -> o.get(RenderComponent.class).drawOrder);

    @Override
    public void update(final Engine engine) {
        final var cameraPosition = engine.graphics().camera().position();
        final List<Entity> backgroundEntities = engine.environment().fetchAll(BACKGROUNDS);
        backgroundEntities.sort(BACKGROUND_COMPARATOR);
        for (final var entity : backgroundEntities) {
            final var background = entity.get(BackgroundComponent.class);
            final var sprite = entity.get(RenderComponent.class);

            final SpriteFillOptions options = SpriteFillOptions.scale(background.zoom).opacity(sprite.options.opacity());
            final Offset offset = Offset.at(
                    cameraPosition.x() * -1 * background.parallaxX,
                    cameraPosition.y() * -1 * background.parallaxY);
            engine.graphics().screen().drawSpriteFill(sprite.sprite, options.offset(offset));
        }
    }
}
