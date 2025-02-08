package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class AreaTriggerSystemTest {

    @Test
    void update_updatesTriggerStatusOfCollidedTriggers(DefaultEnvironment environment) {
        Entity deathTrap = new Entity().add(
                new TransformComponent(Bounds.atOrigin(20, 20, 20, 20)),
                new TriggerAreaComponent(Archetype.of(StaticColliderComponent.class)));

        Entity sheepDeterminedToDie = new Entity().add(
                new TransformComponent(Bounds.atOrigin(10, 10, 20, 20)),
                new StaticColliderComponent());

        environment.addEntities(deathTrap, sheepDeterminedToDie);
        environment.addSystem(new AreaTriggerSystem());

        environment.update();

        assertThat(deathTrap.get(TriggerAreaComponent.class).isTriggered).isTrue();
    }

    @Test
    void update_doesntUpdateStatusOfNonCollidedTriggers(DefaultEnvironment environment) {
        Entity deathTrap = new Entity().add(
                new TransformComponent(Bounds.atOrigin(20, 20, 20, 20)),
                new TriggerAreaComponent(Archetype.of(StaticColliderComponent.class)));

        Entity birdWatchingSheepDie = new Entity().add(
                new TransformComponent(Bounds.atOrigin(10, 200, 20, 20)),
                new StaticColliderComponent());

        environment.addEntities(deathTrap, birdWatchingSheepDie);
        environment.addSystem(new AreaTriggerSystem());

        environment.update();

        assertThat(deathTrap.get(TriggerAreaComponent.class).isTriggered).isFalse();
    }
}
