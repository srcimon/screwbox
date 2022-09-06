package de.suzufa.screwbox.core.entityengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterReflectionComponent;

public class WaterReflectionSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.of(
            WaterComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            WaterReflectionComponent.class, TransformComponent.class, SpriteComponent.class);

    private static final record SpriteBatchEntry(SpriteComponent spriteComponent, Vector position)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry o) {
            return Integer.compare(spriteComponent.drawOrder, o.spriteComponent.drawOrder);
        }

    }

    @Override
    public void update(Engine engine) {
        List<Entity> reflectableEntities = engine.entityEngine().fetchAll(RELECTED_ENTITIES);

        for (Entity water : engine.entityEngine().fetchAll(WATERS)) {
            var transform = water.get(TransformComponent.class);
            var reflectedArea = transform.bounds.moveBy(Vector.yOnly(-transform.bounds.height()));
            final List<SpriteBatchEntry> spriteBatch = new ArrayList<>();

            for (var reflectableEntity : reflectableEntities) {
                var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {

                    final SpriteComponent spriteComponent = reflectableEntity.get(SpriteComponent.class);
                    final var sprite = spriteComponent.sprite;
                    final var spriteDimension = sprite.size();
                    final var spriteBounds = Bounds.atOrigin(
                            reflectableBounds.position().x() - spriteDimension.width() / 2.0,
                            reflectableBounds.position().y() - spriteDimension.height() / 2.0,
                            spriteDimension.width() * spriteComponent.scale,
                            spriteDimension.height() * spriteComponent.scale);

                    spriteBatch.add(new SpriteBatchEntry(spriteComponent, spriteBounds.origin()));
                }
            }
            Collections.sort(spriteBatch);
            for (final SpriteBatchEntry entry : spriteBatch) {
                final SpriteComponent spriteC = entry.spriteComponent;
                engine.graphics().world().drawSprite(
                        spriteC.sprite,
                        Vector.of(entry.position.x(),
                                transform.bounds.minY() + (transform.bounds.minY() - entry.position.y()
                                        - entry.spriteComponent.sprite.size().height())),
                        spriteC.scale,
                        spriteC.opacity.substract(0.7),
                        spriteC.rotation,
                        spriteC.flipMode.invertVertical());// TODO:invert
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
