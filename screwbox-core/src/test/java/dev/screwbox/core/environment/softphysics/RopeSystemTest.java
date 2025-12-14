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
class RopeSystemTest {

    @Test
    void update_ropePresent_initializesNodeList(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
                .add(new RopeComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent());

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent());

        Entity secondNode = new Entity(3)
                .add(new TransformComponent());

        environment
                .addSystem(new RopeSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        assertThat(start.get(RopeComponent.class).nodes).containsExactly(start, firstNode, secondNode);

        environment.update();
        assertThat(start.get(RopeComponent.class).nodes).containsExactly(start, firstNode, secondNode);
        assertThat(start.get(RopeComponent.class).shape.definitionNotes()).containsExactly(start.position(), firstNode.position(), secondNode.position());
    }

    @Test
    void update_ropeIsLooped_throwsException(DefaultEnvironment environment) {
        Entity start = new Entity(1).name("rope-start")
                .add(new RopeComponent())
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent());

        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent());

        Entity secondNode = new Entity(3)
                .add(new SoftLinkComponent(1))
                .add(new TransformComponent());

        environment
                .addSystem(new RopeSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        assertThatThrownBy(environment::update)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("rope starting from entity with id 1 is looped");
    }
}
