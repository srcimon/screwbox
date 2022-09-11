package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.ForwardSignalComponent;
import de.suzufa.screwbox.core.entities.components.RegisterToSignalComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class RegisterToSignalSystemTest {

    @Test
    void update_registersAllSignalReceiversToSender(DefaultEntityEngine entityEngine) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        entityEngine.add(sender, receiverA, receiverB);
        entityEngine.add(new RegisterToSignalSystem());

        entityEngine.update();

        var listenerIds = sender.get(ForwardSignalComponent.class).listenerIds;
        assertThat(listenerIds).contains(10, 11);
    }

    @Test
    void update_raisesExceptionOnMissingSender(DefaultEntityEngine entityEngine) {
        Entity receiver = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        entityEngine.add(receiver);
        entityEngine.add(new RegisterToSignalSystem());

        assertThatThrownBy(() -> entityEngine.update())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("could not find entity with id 4711");
    }

    @Test
    void update_removesComponentsAndItselfWhenNotNeededAnymore(DefaultEntityEngine entityEngine) {
        Entity sender = new Entity(4711).add(
                new ForwardSignalComponent());

        Entity receiverA = new Entity(10).add(
                new RegisterToSignalComponent(4711));

        Entity receiverB = new Entity(11).add(
                new RegisterToSignalComponent(4711));

        entityEngine.add(sender, receiverA, receiverB);
        entityEngine.add(new RegisterToSignalSystem());

        entityEngine.updateTimes(2);

        assertThat(receiverA.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(receiverB.hasComponent(RegisterToSignalComponent.class)).isFalse();
        assertThat(entityEngine.getSystems()).isEmpty();
    }
}
