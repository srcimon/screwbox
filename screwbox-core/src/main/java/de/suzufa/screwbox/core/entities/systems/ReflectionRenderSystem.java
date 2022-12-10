package de.suzufa.screwbox.core.entities.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
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
import de.suzufa.screwbox.core.graphics.World;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    private class ReflectionArea {

        private final double opacityModifier;
        private final Bounds area;
        private final Bounds reflectedArea;
        final double waveSeed;
        private final boolean useWaveEffect;

        public ReflectionArea(final Bounds area, final ReflectionComponent options, final Time time) {
            this.area = area;
            waveSeed = time.milliseconds() / 500.0;
            useWaveEffect = options.useWaveEffect;
            opacityModifier = useWaveEffect
                    ? (Math.sin(waveSeed) * 0.25 + 0.75) * options.opacityModifier.value()
                    : options.opacityModifier.value();
            reflectedArea = area.moveBy(0, -area.height()).inflatedTop(useWaveEffect ? 2 : 0);
        }

        public SpriteBatch createRenderBatchFor(final List<Entity> reflectableEntities) {
            final SpriteBatch spriteBatch = new SpriteBatch();
            for (final var reflectableEntity : reflectableEntities) {
                final var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {
                    addSpriteToBatch(spriteBatch, reflectableEntity, reflectableBounds);
                }
            }
            return spriteBatch;
        }

        private void addSpriteToBatch(final SpriteBatch batch, final Entity reflected, final Bounds bounds) {
            final SpriteComponent spriteComponent = reflected.get(SpriteComponent.class);
            final var spriteSize = spriteComponent.sprite.size();
            final var spritePos = bounds.position().add(-spriteSize.width() / 2.0, -spriteSize.height() / 2.0);

            var position = Vector.of(spritePos.x(),
                    2 * area.minY() - spritePos.y() - spriteSize.height());

            if (useWaveEffect) {
                position = position.add(
                        Math.sin(waveSeed + spritePos.y() / 16) * 2,
                        Math.sin(waveSeed) * 2);
            }
            batch.addEntry(
                    spriteComponent.sprite,
                    position,
                    spriteComponent.scale,
                    spriteComponent.opacity.multiply(opacityModifier),
                    spriteComponent.rotation,
                    spriteComponent.flip.invertVertical(),
                    spriteComponent.drawOrder);
        }
    }

    @Override
    public void update(final Engine engine) {
        final Time time = Time.now();
        final World world = engine.graphics().world();
        final Bounds visibleArea = world.visibleArea();
        final List<Entity> reflectableEntities = engine.entities().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.entities().fetchAll(REFLECTING_AREAS)) {
            final var reflectionOnScreen = reflectionEntity.get(TransformComponent.class).bounds
                    .intersection(visibleArea);
            if (reflectionOnScreen.isPresent()) {
                final var area = new ReflectionArea(reflectionOnScreen.get(),
                        reflectionEntity.get(ReflectionComponent.class), engine.loop().lastUpdate());
                final SpriteBatch batch = area.createRenderBatchFor(reflectableEntities);
                world.drawSpriteBatch(batch, reflectionOnScreen.get());
            }
        }
        System.out.println(Duration.since(time).nanos());
    }

}
