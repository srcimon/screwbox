package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.environment.physics.TailwindPropelledComponent;
import dev.screwbox.core.environment.physics.TailwindSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

    @Test
    void update_nearbyMovingTailwindEntity_accelerates(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.1);

        Entity leaf = new Entity().name("leaf")
                .add(new TransformComponent(0, 0, 16, 16))
                .add(new PhysicsComponent())
                .add(new TailwindPropelledComponent());

        Entity car = new Entity().name("car")
                .add(new TransformComponent(10, 0, 16, 16))
                .add(new TailwindComponent(40, Percent.max()), tailwind -> tailwind.lastPosition = $(-10, 0));

        environment
                .addSystem(new TailwindSystem())
                .addEntity(leaf)
                .addEntity(car);

        environment.update();

        assertThat(leaf.get(PhysicsComponent.class).velocity.length()).isEqualTo(20.0);
        assertThat(car.get(TailwindComponent.class).lastPosition).isEqualTo($(10, 0));
    }
}
