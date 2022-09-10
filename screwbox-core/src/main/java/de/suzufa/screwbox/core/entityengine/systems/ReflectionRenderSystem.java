package de.suzufa.screwbox.core.entityengine.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.ReflectionComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.SpriteBatch;

public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    @Override
    public void update(Engine engine) {
        List<Entity> reflectableEntities = engine.entityEngine().fetchAll(RELECTED_ENTITIES);
        var visibleArea = engine.graphics().world().visibleArea();
        for (Entity reflectionArea : engine.entityEngine().fetchAll(REFLECTING_AREAS)) {
            ReflectionComponent reflection = reflectionArea.get(ReflectionComponent.class);
            double waveSeed = engine.loop().lastUpdate().milliseconds() / 500.0;
            Bounds reflectionAreaBounds = reflectionArea.get(TransformComponent.class).bounds;
            var reflectedArea = reflectionAreaBounds
                    .moveBy(Vector.yOnly(-reflectionAreaBounds.height()))
                    .inflatedTop(reflection.useWaveEffect ? 2 : 0);
            final SpriteBatch spriteBatch = new SpriteBatch();
            for (var reflectableEntity : reflectableEntities) {
                var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {
                    final SpriteComponent spriteComponent = reflectableEntity.get(SpriteComponent.class);
                    final var spriteSize = spriteComponent.sprite.size();
                    final var spriteBounds = Bounds.atOrigin(
                            reflectableBounds.position().x() - spriteSize.width() / 2.0,
                            reflectableBounds.position().y() - spriteSize.height() / 2.0,
                            spriteSize.width() * spriteComponent.scale,
                            spriteSize.height() * spriteComponent.scale);

                    Vector oldPosition = spriteBounds.origin();
                    double actualY = reflectionAreaBounds.minY() +
                            (reflectionAreaBounds.minY() - oldPosition.y()
                                    - spriteComponent.sprite.size().height());

                    double waveMovementEffectX = reflection.useWaveEffect
                            ? Math.sin(waveSeed + actualY / 16) * 2 - 1
                            : 0;
                    double waveMovementEffectY = reflection.useWaveEffect
                            ? Math.sin(waveSeed) * 2 - 1
                            : 0;

                    double opacityModification = reflection.opacityModifier.value()
                            * (reflection.useWaveEffect ? Math.sin(waveSeed) * 0.3 + 0.7 : 1);
                    var actualPosition = Vector.of(oldPosition.x() + waveMovementEffectX,
                            actualY + waveMovementEffectY);

                    if (spriteBounds.moveTo(actualPosition).intersects(visibleArea)) {
                        spriteBatch.addEntry(
                                spriteComponent.sprite,
                                actualPosition,
                                spriteComponent.scale,
                                spriteComponent.opacity.multiply(opacityModification),
                                spriteComponent.rotation,
                                spriteComponent.flipMode.invertVertical(),
                                spriteComponent.drawOrder);
                    }
                }
            }
            engine.graphics().world().drawSpriteBatch(spriteBatch, reflectionAreaBounds);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
