package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.platformer.components.BackgroundComponent;

import static dev.screwbox.core.environment.Order.PRESENTATION_BACKGROUND;

@ExecutionOrder(PRESENTATION_BACKGROUND)
public class BackgroundSystem implements EntitySystem {

    private static final Archetype BACKGROUNDS = Archetype.of(BackgroundComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var resolutionScale = engine.graphics().configuration().resolutionScale();
        for (final var entity : engine.environment().fetchAll(BACKGROUNDS)) {
            final var background = entity.get(BackgroundComponent.class);
            final var config = entity.get(RenderComponent.class);

            final var options = SpriteFillOptions.scale(background.zoom * resolutionScale)
                .opacity(config.options.opacity())
                .drawOrder(config.options.drawOrder());

            for (final var viewport : engine.graphics().viewports()) {
                final var cameraPosition = viewport.camera().position();
                final Offset offset = Offset.at(
                    cameraPosition.x() * -1 * (background.parallaxX - 1),
                    cameraPosition.y() * -1 * (background.parallaxY - 1));
                viewport.canvas().fillWith(config.sprite, options.offset(offset).shaderSetup(ShaderBundle.BREEZE));
            }
        }
    }
}
