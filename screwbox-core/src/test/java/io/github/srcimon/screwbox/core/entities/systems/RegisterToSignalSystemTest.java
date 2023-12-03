package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.ForwardSignalComponent;
import io.github.srcimon.screwbox.core.entities.components.RegisterToSignalComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EntitiesExtension.class)
class RegisterToSignalSystemTest {

    @Test
    void update_registersAllSignalReceiversToSender(DefaultEntities entities) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        entities.addSystem(sender, receiverA, receiverB);
        entities.addSystem(new RegisterToSignalSystem());

        entities.update();

        var listenerIds = sender.get(ForwardSignalComponent.class).listenerIds;
        assertThat(listenerIds).contains(10, 11);
    }

    @Test
    void update_raisesExceptionOnMissingSender(DefaultEntities entities) {
        Entity receiver = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        entities.addEntity(receiver);
        entities.addSystem(new RegisterToSignalSystem());

        assertThatThrownBy(() -> entities.update())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("could not find entity with id 4711");
    }

    @Test
    void update_removesComponentsAndItselfWhenNotNeededAnymore(DefaultEntities entities) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        entities.addSystem(sender, receiverA, receiverB);
        entities.addSystem(new RegisterToSignalSystem());

        entities.updateTimes(2);

        assertThat(receiverA.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(receiverB.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(entities.systems()).isEmpty();
    }
}
