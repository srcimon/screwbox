package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.platformer.components.ShadowComponent;
import dev.screwbox.tiles.Tileset;

import java.util.Optional;

public class ShadowSystem implements EntitySystem {

    private static final Sprite SHADOW = Tileset.fromJson("tilesets/effects/shadow.json").findById(0);
    private static final Archetype SHADOW_CASTERS = Archetype.of(CastShadowComponent.class);
    private static final Archetype SHADOWS = Archetype.of(ShadowComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity shadowCaster : engine.environment().fetchAll(SHADOW_CASTERS)) {
            final Bounds bounds = Bounds.atPosition(Vector.zero(), SHADOW.width(),
                    SHADOW.height());
            final int drawOrder = shadowCaster.get(RenderComponent.class).drawOrder;
            final Entity shadow = new Entity().add(
                    new TransformComponent(bounds),
                    new RenderComponent(SHADOW, drawOrder),
                    new ShadowComponent(shadowCaster.id().orElseThrow()));
            engine.environment().addEntity(shadow);
            shadowCaster.remove(CastShadowComponent.class);
        }

        for (final Entity shadow : engine.environment().fetchAll(SHADOWS)) {
            final int linkedId = shadow.get(ShadowComponent.class).linkedTo;
            final var linked = engine.environment().tryFetchById(linkedId);
            if (linked.isPresent()) {
                final Bounds linkedBounds = linked.get().get(TransformComponent.class).bounds;
                final Optional<Vector> position = engine.physics()
                        .raycastFrom(linkedBounds.position())
                        .ignoringEntitiesHaving(PhysicsComponent.class)
                        .checkingBorders(Borders.TOP_ONLY)
                        .castingVertical(64)
                        .nearestHit();

                RenderComponent renderComponent = shadow.get(RenderComponent.class);
                if (position.isEmpty()) {
                    renderComponent.options = renderComponent.options.opacity(Percent.zero());
                } else {
                    final Vector shadowPosition = position.get();
                    TransformComponent transformComponent = shadow.get(TransformComponent.class);
                    transformComponent.bounds = transformComponent.bounds.moveTo(shadowPosition);
                    final double length = linkedBounds.position().distanceTo(position.get());
                    final double calculatedOpacity = Math.clamp((64 - length) / 100, 0, 1);
                    renderComponent.options = renderComponent.options.opacity(Percent.of(calculatedOpacity));
                }
            } else {
                engine.environment().remove(shadow);
            }
        }
    }
}
