package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.platformer.components.BackgroundComponent;

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
            for (final var viewport : engine.graphics().viewports()) {
                final var cameraPosition = viewport.camera().position();
                final Offset offset = Offset.at(
                        cameraPosition.x() * -1 * (background.parallaxX - 1),
                        cameraPosition.y() * -1 * (background.parallaxY - 1));
                viewport.canvas().fillWith(sprite.sprite, options.offset(offset).shaderSetup(ShaderBundle.BREEZE));
            }
        }
    }
}
