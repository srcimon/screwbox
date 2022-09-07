package de.suzufa.screwbox.core.entityengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Sprite;

public class SpriteRenderSystem implements EntitySystem {

    private final Archetype sprites;
    private final Class<? extends SpriteComponent> spriteComponentClass;

    private static final record SpriteBatchEntry(
            Sprite sprite, Vector position, double scale, Percentage opacity, Rotation rotation, FlipMode flipMode,
            int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry o) {
            return Integer.compare(drawOrder, o.drawOrder);
        }

    }

    public SpriteRenderSystem() {
        this(SpriteComponent.class);
    }

    SpriteRenderSystem(final Class<? extends SpriteComponent> spriteComponentClass) {
        this.spriteComponentClass = spriteComponentClass;
        this.sprites = Archetype.of(spriteComponentClass, TransformComponent.class);
    }

    @Override
    public void update(final Engine engine) {
        final List<SpriteBatchEntry> spriteBatch = new ArrayList<>();
        final Bounds visibleArea = engine.graphics().world().visibleArea();

        for (final Entity entity : engine.entityEngine().fetchAll(sprites)) {
            final Bounds entityBounds = entity.get(TransformComponent.class).bounds;
            final SpriteComponent spriteComponent = entity.get(spriteComponentClass);
            final var sprite = spriteComponent.sprite;
            final var spriteDimension = sprite.size();
            final var spriteBounds = Bounds.atOrigin(
                    entityBounds.position().x() - spriteDimension.width() / 2.0,
                    entityBounds.position().y() - spriteDimension.height() / 2.0,
                    spriteDimension.width() * spriteComponent.scale,
                    spriteDimension.height() * spriteComponent.scale);

            if (spriteBounds.intersects(visibleArea)) {
                spriteBatch.add(new SpriteBatchEntry(
                        spriteComponent.sprite,

                        spriteBounds.origin(),
                        spriteComponent.scale,
                        spriteComponent.opacity,
                        spriteComponent.rotation,
                        spriteComponent.flipMode,
                        spriteComponent.drawOrder

                ));
            }
        }

        Collections.sort(spriteBatch);

        for (final SpriteBatchEntry entry : spriteBatch) {
            engine.graphics().world().drawSprite(
                    entry.sprite,
                    entry.position,
                    entry.scale,
                    entry.opacity,
                    entry.rotation,
                    entry.flipMode);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_WORLD;
    }
}
