package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EnvironmentExtension.class)
class SoftBodySystemTest {

    @Test
    void update_softBodyPresent_initializesNodeList(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
                .add(new SoftBodyComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent());

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent());

        Entity secondNode = new Entity(3)
                .add(new TransformComponent())
                .add(new SoftLinkComponent(1));

        environment
                .addSystem(new SoftBodySystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        assertThat(start.get(SoftBodyComponent.class).nodes).containsExactly(start, firstNode, secondNode, start);

        environment.update();

        assertThat(start.get(SoftBodyComponent.class).nodes).containsExactly(start, firstNode, secondNode, start);
    }

    @Test
    void update_bodyIsNotClosed_throwsException(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
                .add(new SoftBodyComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent());

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent());

        Entity secondNode = new Entity(3)
                .add(new TransformComponent());

        environment
                .addSystem(new SoftBodySystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        assertThatThrownBy(environment::update)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("soft body is not closed");
    }

    @Test
    void update_entityIsLinkedToSelf_throwsException(DefaultEnvironment environment) {
        Entity start = new Entity(1)
                .add(new SoftBodyComponent())
                .add(new SoftLinkComponent(1))
                .add(new TransformComponent());

        environment
                .addSystem(new SoftBodySystem())
                .addEntity(start);

        assertThatThrownBy(environment::update)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("soft link of entity with id 1 is linked to self");

    }
}
