package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.ForwardSignalComponent;
import de.suzufa.screwbox.core.entities.components.RegisterToSignalComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class RegisterToSignalSystemTest {

    @Test
    void update_registersAllSignalReceiversToSender(DefaultEntities entities) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        entities.add(sender, receiverA, receiverB);
        entities.add(new RegisterToSignalSystem());

        entities.update();

        var listenerIds = sender.get(ForwardSignalComponent.class).listenerIds;
        assertThat(listenerIds).contains(10, 11);
    }

    @Test
    void update_raisesExceptionOnMissingSender(DefaultEntities entities) {
        Entity receiver = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        entities.add(receiver);
        entities.add(new RegisterToSignalSystem());

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

        entities.add(sender, receiverA, receiverB);
        entities.add(new RegisterToSignalSystem());

        entities.updateTimes(2);

        assertThat(receiverA.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(receiverB.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(entities.getSystems()).isEmpty();
    }
}
