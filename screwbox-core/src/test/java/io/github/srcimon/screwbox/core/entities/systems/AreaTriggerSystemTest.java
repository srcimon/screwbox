package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.SignalComponent;
import io.github.srcimon.screwbox.core.entities.components.StaticMarkerComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.components.TriggerAreaComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntitiesExtension.class)
class AreaTriggerSystemTest {

    @Test
    void update_updatesTriggerStatusOfCollidedTriggers(DefaultEntities entities) {
        Entity deathTrap = new Entity().add(
                new TransformComponent(Bounds.atOrigin(20, 20, 20, 20)),
                new TriggerAreaComponent(Archetype.of(StaticMarkerComponent.class)),
                new SignalComponent());

        Entity sheepDeterminedToDie = new Entity().add(
                new TransformComponent(Bounds.atOrigin(10, 10, 20, 20)),
                new StaticMarkerComponent());

        entities.addSystem(deathTrap, sheepDeterminedToDie);
        entities.addSystem(new AreaTriggerSystem());

        entities.update();

        assertThat(deathTrap.get(SignalComponent.class).isTriggered).isTrue();
    }

    @Test
    void update_doesntUpdateStatusOfNonCollidedTriggers(DefaultEntities entities) {
        Entity deathTrap = new Entity().add(
                new TransformComponent(Bounds.atOrigin(20, 20, 20, 20)),
                new TriggerAreaComponent(Archetype.of(StaticMarkerComponent.class)),
                new SignalComponent());

        Entity birdWatchingSheepDie = new Entity().add(
                new TransformComponent(Bounds.atOrigin(10, 200, 20, 20)),
                new SignalComponent(),
                new StaticMarkerComponent());

        entities.addSystem(deathTrap, birdWatchingSheepDie);
        entities.addSystem(new AreaTriggerSystem());

        entities.update();

        assertThat(deathTrap.get(SignalComponent.class).isTriggered).isFalse();
    }
}
