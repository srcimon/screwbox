package de.suzufa.screwbox.core.entities.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ReflectionComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.SpriteBatch;

public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> reflectableEntities = engine.entities().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.entities().fetchAll(REFLECTING_AREAS)) {
            final ReflectionComponent reflection = reflectionEntity.get(ReflectionComponent.class);
            final var possibleReflectionAreaBounds = reflectionEntity.get(TransformComponent.class).bounds
                    .intersection(engine.graphics().world().visibleArea());
            if (possibleReflectionAreaBounds.isPresent()) {
                final Bounds reflectionArea = possibleReflectionAreaBounds.get();
                renderReflection(engine, reflection, reflectionArea, reflectableEntities);
            }
        }
    }

    private void renderReflection(final Engine engine, final ReflectionComponent reflection,
            final Bounds reflectionArea, final List<Entity> reflectableEntities) {
        final double waveSeed = engine.loop().lastUpdate().milliseconds() / 500.0;
        final var reflectedArea = reflectionArea
                .moveBy(0, -reflectionArea.height())
                .inflatedTop(reflection.useWaveEffect ? 2 : 0);
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

                final double actualY = reflectionArea.minY() + (reflectionArea.minY() - spriteBounds.position().y());
                final var actualPosition = Vector.of(spriteBounds.position().x(), actualY);
                final double waveMovementEffectX = reflection.useWaveEffect ? Math.sin(waveSeed + actualY / 16) * 2 : 0;
                final double waveMovementEffectY = reflection.useWaveEffect ? Math.sin(waveSeed) * 2 : 0;
                final Vector waveEffectPosition = actualPosition.addX(waveMovementEffectX).addY(waveMovementEffectY);
                final Bounds reflectionBounds = spriteBounds.moveTo(waveEffectPosition);

                if (reflectionBounds.intersects(engine.graphics().world().visibleArea())) {
                    final Percent opacity = spriteComponent.opacity
                            .multiply(reflection.opacityModifier.value())
                            .multiply(reflection.useWaveEffect ? Math.sin(waveSeed) * 0.25 + 0.75 : 1);

                    spriteBatch.addEntry(
                            spriteComponent.sprite,
                            reflectionBounds.origin(),
                            spriteComponent.scale,
                            opacity,
                            spriteComponent.rotation,
                            spriteComponent.flip.invertVertical(),
                            spriteComponent.drawOrder);
                }
            }
        }
        engine.graphics().world().drawSpriteBatch(spriteBatch, reflectionArea);
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
