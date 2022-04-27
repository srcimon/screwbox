package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.graphics.world.WorldSprite.sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;

public class SpriteRenderSystem implements EntitySystem {

    private final Archetype sprites;
	private final Class<? extends SpriteComponent> spriteComponentClass;

    private static final record SpriteBatchEntry(Sprite sprite, Vector position, int drawOrder, Percentage opacity)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(SpriteBatchEntry o) {
            return Integer.compare(drawOrder(), o.drawOrder());
        }

    }
    
    public SpriteRenderSystem() {
    	this(SpriteComponent.class);
	}
    
    SpriteRenderSystem(Class<? extends SpriteComponent> spriteComponentClass) {
    	this.spriteComponentClass = spriteComponentClass;
    	this.sprites = Archetype.of(spriteComponentClass, TransformComponent.class);
    }

    @Override
    public void update(Engine engine) {
        List<SpriteBatchEntry> spriteBatch = new ArrayList<>();
        Bounds visibleArea = engine.graphics().world().visibleArea();

        for (Entity entity : engine.entityEngine().fetchAll(sprites)) {
            Bounds entityBounds = entity.get(TransformComponent.class).bounds;
            SpriteComponent spriteComponent = entity.get(spriteComponentClass);
            var sprite = spriteComponent.sprite;
            final var spriteDimension = sprite.dimension();
            final var spriteBounds = Bounds.atOrigin(
                    entityBounds.position().x() - spriteDimension.width() / 2.0,
                    entityBounds.position().y() - spriteDimension.height() / 2.0,
                    spriteDimension.width(), spriteDimension.height());

            if (spriteBounds.intersects(visibleArea)) {
                spriteBatch.add(new SpriteBatchEntry(spriteComponent.sprite, spriteBounds.origin(),
                        spriteComponent.drawOrder, spriteComponent.opacity));
            }
        }

        Collections.sort(spriteBatch);

        for (final SpriteBatchEntry entry : spriteBatch) {
            engine.graphics().world().draw(sprite(entry.sprite(), entry.position(), entry.opacity()));
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_WORLD;
    }
}
