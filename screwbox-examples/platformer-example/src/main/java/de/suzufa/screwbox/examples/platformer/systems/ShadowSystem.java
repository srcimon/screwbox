package de.suzufa.screwbox.examples.platformer.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.core.utils.MathUtil;
import de.suzufa.screwbox.examples.platformer.components.CastShadowComponent;
import de.suzufa.screwbox.examples.platformer.components.ShadowComponent;
import de.suzufa.screwbox.tiled.Tileset;

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
                    final Bounds updatedBounds = shadow.get(TransformComponent.class).bounds.moveTo(shadowPosition);
                    shadow.get(TransformComponent.class).bounds = updatedBounds;
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
