package de.suzufa.screwbox.core.entities.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.core.entities.components.ReflectionComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.SpriteBatch;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    private class ReflectionArea {

        private ReflectionComponent options;
        private Bounds area;
        private Bounds reflectedArea;

        public ReflectionArea(Bounds area, ReflectionComponent options) {
            this.options = options;
            this.area = area;
            reflectedArea = area.moveBy(0, -area.height());
        }

        public SpriteBatch createRenderBatchFor(List<Entity> reflectableEntities, Time time, Engine engine) {
            final double waveSeed = time.milliseconds() / 500.0;

            final SpriteBatch spriteBatch = new SpriteBatch();
            for (final var reflectableEntity : reflectableEntities) {
                final var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {
                    final SpriteComponent spriteComponent = reflectableEntity.get(SpriteComponent.class);
                    final var spriteSize = spriteComponent.sprite.size();
                    final var spriteBounds = Bounds.atOrigin(
                            reflectableBounds.position().x() - spriteSize.width() / 2.0,
                            reflectableBounds.position().y() - spriteSize.height() / 2.0,
                            spriteSize.width() * spriteComponent.scale,
                            spriteSize.height() * spriteComponent.scale);

                    final double actualY = area.minY() + (area.minY() - spriteBounds.position().y());
                    final var actualPosition = Vector.of(spriteBounds.position().x(), actualY);
                    final double waveMovementEffectX = options.useWaveEffect ? Math.sin(waveSeed + actualY / 16) * 2
                            : 0;
                    final double waveMovementEffectY = options.useWaveEffect ? Math.sin(waveSeed) * 2 : 0;
                    final Vector waveEffectPosition = actualPosition.add(waveMovementEffectX, waveMovementEffectY);
                    final Bounds reflectionBounds = spriteBounds.moveTo(waveEffectPosition);

                    if (reflectionBounds.intersects(engine.graphics().world().visibleArea())) {
                        final Percent opacity = spriteComponent.opacity
                                .multiply(options.opacityModifier.value())
                                .multiply(options.useWaveEffect ? Math.sin(waveSeed) * 0.25 + 0.75 : 1);

                        spriteBatch.addEntry(
                                spriteComponent.sprite,
                                reflectionBounds.origin(),
                                spriteComponent.scale,
                                opacity,
                                spriteComponent.rotation,
                                spriteComponent.flip.invertVertical(),
                                spriteComponent.drawOrder);
                    } else {
                        System.out.println("bl");
                    }
                }
            }
            return spriteBatch;
        }
    }

    @Override
    public void update(final Engine engine) {
        Time time = Time.now();
        final Bounds visibleArea = engine.graphics().world().visibleArea();
        final List<Entity> reflectableEntities = engine.entities().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.entities().fetchAll(REFLECTING_AREAS)) {
            final var reflectionOnScreen = reflectionEntity.get(TransformComponent.class).bounds
                    .intersection(visibleArea);
            if (reflectionOnScreen.isPresent()) {
                var area = new ReflectionArea(reflectionOnScreen.get(),
                        reflectionEntity.get(ReflectionComponent.class));
                SpriteBatch batch = area.createRenderBatchFor(reflectableEntities, engine.loop().lastUpdate(), engine);
                engine.graphics().world().drawSpriteBatch(batch, reflectionOnScreen.get());
            }
        }
        System.out.println(Duration.since(time).nanos());
    }

}
