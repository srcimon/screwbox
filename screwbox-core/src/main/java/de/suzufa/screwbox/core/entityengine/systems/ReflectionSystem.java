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

//TODO: implement relfections left right / top down
public class ReflectionSystem implements EntitySystem {

    private static final Archetype REFLECTING_FLOORS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    @Override
    public void update(Engine engine) {
        List<Entity> reflectableEntities = engine.entityEngine().fetchAll(RELECTED_ENTITIES);
        World world = engine.graphics().world();
        var visibleArea = world.visibleArea();
        for (Entity floor : engine.entityEngine().fetchAll(REFLECTING_FLOORS)) {
            var transform = floor.get(TransformComponent.class);
            if (transform.bounds.intersects(visibleArea)) {
                var reflectedArea = transform.bounds.moveBy(Vector.yOnly(-transform.bounds.height()))
                        .intersection(visibleArea);

                Percentage opacityReduction = floor.get(ReflectionComponent.class).opacityReduction;
                final SpriteBatch spriteBatch = new SpriteBatch();

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

                        if (spriteBounds.intersects(visibleArea)) {
                            Vector oldPosition = spriteBounds.origin();
                            double actualY = transform.bounds.minY() +
                                    (transform.bounds.minY() - oldPosition.y()
                                            - spriteComponent.sprite.size().height());
                            var actualPosition = Vector.of(oldPosition.x(), actualY);

                            spriteBatch.addEntry(
                                    spriteComponent.sprite,
                                    actualPosition,
                                    spriteComponent.scale,
                                    spriteComponent.opacity.substract(opacityReduction.value()),
                                    spriteComponent.rotation,
                                    spriteComponent.flipMode.invertVertical(),
                                    spriteComponent.drawOrder);
                        }
                    }
                }
                engine.graphics().world().drawSpriteBatch(spriteBatch);
                // TODO: world().drawSpriteBatch(batch, restrictedArea)
                // TODO: alternative: world().restrictDrawingArea()
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
