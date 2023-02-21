package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.examples.platformer.components.BackgroundComponent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Order(SystemOrder.PRESENTATION_BACKGROUND)
public class BackgroundSystem implements EntitySystem {

    private static final Archetype BACKGROUNDS = Archetype.of(BackgroundComponent.class, RenderComponent.class);

    private static final Comparator<Entity> BACKGROUND_COMPARATOR = (o1, o2) -> Double
            .compare(o1.get(RenderComponent.class).drawOrder, o2.get(RenderComponent.class).drawOrder);

    @Override
    public void update(final Engine engine) {
        final var cameraPosition = engine.graphics().cameraPosition();
        final List<Entity> backgroundEntities = engine.entities().fetchAll(BACKGROUNDS);
        Collections.sort(backgroundEntities, BACKGROUND_COMPARATOR);
        for (final var entity : backgroundEntities) {
            final var background = entity.get(BackgroundComponent.class);
            final var sprite = entity.get(RenderComponent.class);

            final Offset offset = Offset.at(
                    cameraPosition.x() * -1 * background.parallaxX,
                    cameraPosition.y() * -1 * background.parallaxY);
            engine.graphics().screen().fillWith(offset, sprite.sprite, background.zoom, sprite.opacity);
        }
    }
}
