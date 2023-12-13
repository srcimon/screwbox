package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.ForwardSignalComponent;
import io.github.srcimon.screwbox.core.environment.logic.RegisterToSignalComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.logic.RegisterToSignalSystem;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EnvironmentExtension.class)
class RegisterToSignalSystemTest {

    @Test
    void update_registersAllSignalReceiversToSender(DefaultEnvironment environment) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        environment.addSystem(sender, receiverA, receiverB);
        environment.addSystem(new RegisterToSignalSystem());

        environment.update();

        var listenerIds = sender.get(ForwardSignalComponent.class).listenerIds;
        assertThat(listenerIds).contains(10, 11);
    }

    @Test
    void update_raisesExceptionOnMissingSender(DefaultEnvironment environment) {
        Entity receiver = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        environment.addEntity(receiver);
        environment.addSystem(new RegisterToSignalSystem());

        assertThatThrownBy(() -> environment.update())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("could not find entity with id 4711");
    }

    @Test
    void update_removesComponentsAndItselfWhenNotNeededAnymore(DefaultEnvironment environment) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        environment.addSystem(sender, receiverA, receiverB);
        environment.addSystem(new RegisterToSignalSystem());

        environment.updateTimes(2);

        assertThat(receiverA.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(receiverB.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(environment.systems()).isEmpty();
    }
}
