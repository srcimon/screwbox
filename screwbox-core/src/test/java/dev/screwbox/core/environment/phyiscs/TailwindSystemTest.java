package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.TailwindPropelledComponent;
import dev.screwbox.core.environment.physics.TailwindSystem;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class TailwindSystemTest {

    @Test
    void update_noTailwindEntity_doesNotAccelerate(DefaultEnvironment environment) {
        Entity leaf = new Entity().name("leaf")
                .add(new TransformComponent())
                .add(new PhysicsComponent())
                .add(new TailwindPropelledComponent());

        environment
                .addSystem(new TailwindSystem())
                .addEntity(leaf);

        environment.update();
        assertThat(leaf.get(PhysicsComponent.class).velocity.length()).isZero();
    }
}
