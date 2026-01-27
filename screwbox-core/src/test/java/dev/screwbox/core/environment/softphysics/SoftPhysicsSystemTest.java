package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoftPhysicsSystemTest {

    @Test
    void update_idleEntities_initializesDistance(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        Entity target = new Entity(1)
            .add(new TransformComponent(0, 0, 4, 4))
            .add(new PhysicsComponent());

        Entity linked = new Entity(2)
            .add(new TransformComponent(100, 0, 4, 4))
            .add(new SoftLinkComponent(1))
            .add(new PhysicsComponent());

        environment
            .addSystem(new SoftPhysicsSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(target)
            .addEntity(linked);

        environment.update();

        assertThat(target.position().distanceTo(linked.position())).isEqualTo(100);
        assertThat(linked.get(SoftLinkComponent.class).angle).isEqualTo(Angle.degrees(270));
        assertThat(linked.get(SoftLinkComponent.class).length).isEqualTo(100.0);
    }

    @Test
    void update_initializedLength_addsMotionToEntities(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        Entity target = new Entity(1)
            .add(new TransformComponent(0, 0, 4, 4))
            .add(new PhysicsComponent());

        Entity linked = new Entity(2)
            .add(new TransformComponent(100, 0, 4, 4))
            .add(new SoftLinkComponent(1), config -> config.length = 10)
            .add(new PhysicsComponent());

        environment
            .addSystem(new SoftPhysicsSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(target)
            .addEntity(linked);

        environment.update(); // length initialization
        environment.update();

        assertThat(target.position().distanceTo(linked.position())).isEqualTo(94.0, offset(0.1));
        assertThat(linked.get(SoftLinkComponent.class).angle).isEqualTo(Angle.degrees(270));
        assertThat(linked.get(SoftLinkComponent.class).length).isEqualTo(10.0);

        environment.update();

        assertThat(target.position().distanceTo(linked.position())).isEqualTo(82.0, offset(0.1));
        assertThat(linked.get(SoftLinkComponent.class).angle).isEqualTo(Angle.degrees(270));

        environment.updateTimes(4);

        assertThat(target.position().distanceTo(linked.position())).isEqualTo(20.0, offset(0.1));
        assertThat(linked.get(SoftLinkComponent.class).angle).isEqualTo(Angle.degrees(90));
    }

    @Test
    void update_noPhysics_justUpdatesData(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        Entity target = new Entity(1)
            .add(new TransformComponent(0, 0, 4, 4));

        Entity linked = new Entity(2)
            .add(new TransformComponent(100, 0, 4, 4))
            .add(new SoftLinkComponent(1), config -> config.length = 10);

        environment
            .addSystem(new SoftPhysicsSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(target)
            .addEntity(linked);

        environment.update();

        assertThat(target.position().distanceTo(linked.position())).isEqualTo(100.0);
        assertThat(linked.get(SoftLinkComponent.class).angle).isEqualTo(Angle.degrees(270));
    }

    @Test
    void update_idleStructures_initializesDistance(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        Entity firstTarget = new Entity(1)
            .add(new TransformComponent(50, 0, 4, 4));

        Entity secondTarget = new Entity(2)
            .add(new TransformComponent(0, 0, 4, 4));

        Entity linked = new Entity(3)
            .add(new TransformComponent(100, 0, 4, 4))
            .add(new SoftStructureComponent(1, 2));

        environment
            .addSystem(new SoftPhysicsSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(firstTarget)
            .addEntity(secondTarget)
            .addEntity(linked);

        environment.update();

        assertThat(linked.get(SoftStructureComponent.class).lengths).contains(50.0, 100.0);
    }

    @Test
    void update_linkedEntitiesOutOfLength_movesEntities(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        Entity firstTarget = new Entity(1)
            .add(new PhysicsComponent())
            .add(new TransformComponent(50, 0, 4, 4));

        Entity secondTarget = new Entity(2)
            .add(new PhysicsComponent())
            .add(new TransformComponent(0, 10, 4, 4));

        Entity linked = new Entity(3)
            .add(new PhysicsComponent())
            .add(new TransformComponent(100, 0, 4, 4))
            .add(new SoftStructureComponent(1, 2), config -> {
                config.lengths[0] = 10;
                config.lengths[1] = 20;
            });

        environment
            .addSystem(new SoftPhysicsSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(firstTarget)
            .addEntity(secondTarget)
            .addEntity(linked);

        environment.update(); // length initialization
        environment.update();

        assertThat(firstTarget.position().x()).isEqualTo(53.0);
        assertThat(firstTarget.position().y()).isZero();

        assertThat(secondTarget.position().x()).isEqualTo(2.98, offset(0.1));
        assertThat(secondTarget.position().y()).isEqualTo(9.7, offset(0.1));

        assertThat(linked.position().x()).isEqualTo(94.01, offset(0.1));
        assertThat(linked.position().y()).isEqualTo(0.29, offset(0.1));
    }

    @Test
    void update_entityIsLinkedToSelf_throwsException(DefaultEnvironment environment) {
        Entity linked = new Entity(2)
            .add(new TransformComponent(100, 0, 4, 4))
            .add(new SoftLinkComponent(2));

        environment
            .addSystem(new SoftPhysicsSystem())
            .addEntity(linked);

        assertThatThrownBy(environment::update)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("soft link of entity with id 2 is linked to self");
    }
}