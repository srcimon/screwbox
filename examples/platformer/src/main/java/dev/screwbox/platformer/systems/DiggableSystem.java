package dev.screwbox.platformer.systems;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.HasOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.graphics.options.CameraShakeOptions;
import dev.screwbox.core.navigation.Borders;
import dev.screwbox.core.particles.ParticlesBundle;
import dev.screwbox.platformer.components.DiggableComponent;
import dev.screwbox.platformer.components.DiggingComponent;

@HasOrder(Order.SIMULATION_EARLY)
public class DiggableSystem implements EntitySystem {

    private static final Archetype DIGGINGS = Archetype.of(DiggingComponent.class, PhysicsComponent.class);
    private static final Archetype DIGGABLES = Archetype.ofSpacial(DiggableComponent.class, RenderComponent.class);
    private static final Asset<Sound> DIG_SOUND = Sound.assetFromFile("sounds/dig.wav");

    @Override
    public void update(final Engine engine) {
        for (final var digging : engine.environment().fetchAll(DIGGINGS)) {
            engine.navigation().raycastFrom(digging.position())
                    .checkingFor(DIGGABLES)
                    .ignoringEntitiesHaving(TweenComponent.class)
                    .checkingBorders(Borders.TOP_ONLY)
                    .castingVertical(14)
                    .selectAnyEntity().ifPresent(entity -> {
                        engine.graphics().camera().shake(CameraShakeOptions.lastingForDuration(Duration.oneSecond()).strength(8));
                        engine.environment().remove(entity);
                        engine.particles().spawnMultiple(10, entity.bounds(), ParticlesBundle.SMOKE_TRAIL.get().source(entity));
                        var physicsComponent = digging.get(PhysicsComponent.class);
                        physicsComponent.velocity = Vector.of(physicsComponent.velocity.x(), -150);
                        engine.audio().playSound(DIG_SOUND);
                    });

        }
    }
}
