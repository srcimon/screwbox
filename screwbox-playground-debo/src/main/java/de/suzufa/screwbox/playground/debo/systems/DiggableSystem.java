package de.suzufa.screwbox.playground.debo.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.FadeOutComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.playground.debo.components.DiggableComponent;
import de.suzufa.screwbox.playground.debo.components.DiggingComponent;

@Order(UpdatePriority.SIMULATION_BEGIN)
public class DiggableSystem implements EntitySystem {

    private static final Archetype DIGGINGS = Archetype.of(DiggingComponent.class, PhysicsBodyComponent.class);

    private static final Archetype DIGGABLES = Archetype.of(DiggableComponent.class, TransformComponent.class,
            SpriteComponent.class);

    private static final Asset<Sound> DIG_SOUND = Sound.assetFromFile("sounds/dig.wav");

    @Override
    public void update(Engine engine) {
        for (final var digging : engine.entities().fetchAll(DIGGINGS)) {
            var diggingBody = digging.get(TransformComponent.class);
            Optional<Entity> hitEntity = engine.physics().raycastFrom(diggingBody.bounds.position())
                    .checkingFor(DIGGABLES)
                    .checkingBorders(Borders.TOP_ONLY)
                    .castingVertical(14)
                    .selectAnyEntity();

            if (hitEntity.isEmpty()) {
                return;
            }

            Entity entity = hitEntity.get();
            if (!entity.hasComponent(FadeOutComponent.class)) {
                entity.add(new FadeOutComponent(2));
                entity.remove(ColliderComponent.class);
                PhysicsBodyComponent physicsBodyComponent = digging.get(PhysicsBodyComponent.class);
                physicsBodyComponent.momentum = Vector.of(physicsBodyComponent.momentum.x(), -150);
                engine.audio().playEffect(DIG_SOUND);
            }
        }
    }
}
