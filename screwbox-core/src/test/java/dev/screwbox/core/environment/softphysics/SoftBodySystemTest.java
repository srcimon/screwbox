package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

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
                .add(new TransformComponent());

        environment
                .addSystem(new SoftBodySystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        assertThat(start.get(SoftBodyComponent.class).nodes).containsExactly(start, firstNode, secondNode);

        environment.update();

        assertThat(start.get(SoftBodyComponent.class).nodes).containsExactly(start, firstNode, secondNode);
    }

    @Test
    void update_nodesAreLooped_createsSoftBody(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
                .add(new SoftBodyComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent());

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent());

        Entity secondNode = new Entity(3)
                .add(new SoftLinkComponent(1))
                .add(new TransformComponent());

        environment
                .addSystem(new SoftBodySystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        assertThat(start.get(SoftBodyComponent.class).nodes).containsExactly(start, firstNode, secondNode);
    }
}
