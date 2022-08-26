package de.suzufa.screwbox.core.entityengine.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.StaticMarkerComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class AreaTriggerSystemTest {

    @Test
    void update_updatesTriggerStatusOfCollidedTriggers(DefaultEntityEngine entityEngine) {
        Entity deathTrap = new Entity().add(
                new TransformComponent(Bounds.atOrigin(20, 20, 20, 20)),
                new TriggerAreaComponent(Archetype.of(StaticMarkerComponent.class)),
                new SignalComponent());

        Entity sheepDeterminedToDie = new Entity().add(
                new TransformComponent(Bounds.atOrigin(10, 10, 20, 20)),
                new StaticMarkerComponent());

        entityEngine.add(deathTrap, sheepDeterminedToDie);
        entityEngine.add(new AreaTriggerSystem());

        entityEngine.update();

        assertThat(deathTrap.get(SignalComponent.class).isTriggered).isTrue();
    }

    @Test
    void update_doesntUpdateStatusOfNonCollidedTriggers(DefaultEntityEngine entityEngine) {
        Entity deathTrap = new Entity().add(
                new TransformComponent(Bounds.atOrigin(20, 20, 20, 20)),
                new TriggerAreaComponent(Archetype.of(StaticMarkerComponent.class)),
                new SignalComponent());

        Entity birdWatchingSheepDie = new Entity().add(
                new TransformComponent(Bounds.atOrigin(10, 200, 20, 20)),
                new SignalComponent(),
                new StaticMarkerComponent());

        entityEngine.add(deathTrap, birdWatchingSheepDie);
        entityEngine.add(new AreaTriggerSystem());

        entityEngine.update();

        assertThat(deathTrap.get(SignalComponent.class).isTriggered).isFalse();
    }
}
