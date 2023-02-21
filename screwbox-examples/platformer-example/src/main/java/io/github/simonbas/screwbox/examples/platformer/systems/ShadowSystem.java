package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.core.physics.Borders;
import io.github.simonbas.screwbox.core.utils.MathUtil;
import io.github.simonbas.screwbox.examples.platformer.components.CastShadowComponent;
import io.github.simonbas.screwbox.examples.platformer.components.ShadowComponent;
import io.github.simonbas.screwbox.tiled.Tileset;

import java.util.Optional;

public class ShadowSystem implements EntitySystem {

    private static final Sprite SHADOW = Tileset.fromJson("tilesets/effects/shadow.json").findById(0);
    private static final Archetype SHADOW_CASTERS = Archetype.of(CastShadowComponent.class);
    private static final Archetype SHADOWS = Archetype.of(ShadowComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity shadowCaster : engine.entities().fetchAll(SHADOW_CASTERS)) {
            final Bounds bounds = Bounds.atPosition(Vector.zero(), SHADOW.size().width(),
                    SHADOW.size().height());
            final int drawOrder = shadowCaster.get(RenderComponent.class).drawOrder;
            final Entity shadow = new Entity().add(
                    new TransformComponent(bounds),
                    new RenderComponent(SHADOW, drawOrder),
                    new ShadowComponent(shadowCaster.id().orElseThrow()));
            engine.entities().add(shadow);
            shadowCaster.remove(CastShadowComponent.class);
        }

        for (final Entity shadow : engine.entities().fetchAll(SHADOWS)) {
            final int linkedId = shadow.get(ShadowComponent.class).linkedTo;
            final var linked = engine.entities().fetchById(linkedId);
            if (linked.isPresent()) {
                final Bounds linkedBounds = linked.get().get(TransformComponent.class).bounds;
                final Optional<Vector> position = engine.physics()
                        .raycastFrom(linkedBounds.position())
                        .ignoringEntitiesHaving(PhysicsBodyComponent.class)
                        .checkingBorders(Borders.TOP_ONLY)
                        .castingVertical(64)
                        .nearestHit();

                if (position.isEmpty()) {
                    shadow.get(RenderComponent.class).opacity = Percent.min();
                } else {
                    final Vector shadowPosition = position.get();
                    shadow.get(TransformComponent.class).bounds = shadow.get(TransformComponent.class).bounds.moveTo(shadowPosition);
                    final double length = linkedBounds.position().distanceTo(position.get());
                    final double calculatedOpacity = MathUtil.clamp(0, (64 - length) / 100, 1);
                    shadow.get(RenderComponent.class).opacity = Percent.of(calculatedOpacity);
                }
            } else {
                engine.entities().remove(shadow);
            }
        }
    }
}
