package de.suzufa.screwbox.core.entityengine.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.ReflectionComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.SpriteBatch;
import de.suzufa.screwbox.core.graphics.World;

public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    @Override
    public void update(Engine engine) {
        List<Entity> reflectableEntities = engine.entityEngine().fetchAll(RELECTED_ENTITIES);
        World world = engine.graphics().world();
        var visibleArea = world.visibleArea();
        for (Entity reflectionArea : engine.entityEngine().fetchAll(REFLECTING_AREAS)) {
            ReflectionComponent reflection = reflectionArea.get(ReflectionComponent.class);
            double waveEffect = reflection.useWaveEffect
                    ? Math.sin(engine.loop().lastUpdate().milliseconds() / 500.0) * 0.3 + 0.7
                    : 1;
            Percentage opacityModifier = reflection.opacityModifier;
            var transform = reflectionArea.get(TransformComponent.class);
            var reflectedArea = transform.bounds.moveBy(Vector.yOnly(-transform.bounds.height())).inflated(2)
                    .intersection(visibleArea);
            if (reflectedArea.isPresent()) {

                final SpriteBatch spriteBatch = new SpriteBatch();

                for (var reflectableEntity : reflectableEntities) {
                    var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                    if (reflectableBounds.intersects(reflectedArea.get())) {
                        final SpriteComponent spriteComponent = reflectableEntity.get(SpriteComponent.class);
                        final var sprite = spriteComponent.sprite;
                        final var spriteDimension = sprite.size();
                        final var spriteBounds = Bounds.atOrigin(
                                reflectableBounds.position().x() - spriteDimension.width() / 2.0,
                                reflectableBounds.position().y() - spriteDimension.height() / 2.0,
                                spriteDimension.width() * spriteComponent.scale,
                                spriteDimension.height() * spriteComponent.scale);

                        if (spriteBounds.intersects(visibleArea)) {
                            Vector oldPosition = spriteBounds.origin();
                            double actualY = transform.bounds.minY() +
                                    (transform.bounds.minY() - oldPosition.y()
                                            - spriteComponent.sprite.size().height());
                            var actualPosition = Vector.of(oldPosition.x(), actualY);

                            Percentage opacity = spriteComponent.opacity
                                    .multiply(opacityModifier.value())
                                    .multiply(waveEffect);

                            double waveMovementEffectX = reflection.useWaveEffect
                                    ? Math.sin(engine.loop().lastUpdate().milliseconds() / 500.0 + actualY / 16) * 2 - 1
                                    : 0;
                            double waveMovementEffectY = reflection.useWaveEffect
                                    ? Math.sin(engine.loop().lastUpdate().milliseconds() / 500.0) * 2 - 1
                                    : 0;

                            spriteBatch.addEntry(
                                    spriteComponent.sprite,
                                    actualPosition.addX(waveMovementEffectX).addY(waveMovementEffectY),
                                    spriteComponent.scale,
                                    opacity,
                                    spriteComponent.rotation,
                                    spriteComponent.flipMode.invertVertical(),
                                    spriteComponent.drawOrder);
                        }
                    }
                }
                engine.graphics().world().drawSpriteBatch(spriteBatch, transform.bounds);
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
