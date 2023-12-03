package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.ColliderComponent;
import io.github.srcimon.screwbox.core.entities.components.StaticMarkerComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntitiesExtension.class)
class CombineStaticColliderSystemTest {

    @Test
    void update_combinesHorizontallyAlignedColliders(DefaultEntities entities) {
        Entity brickA = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(20, 0, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(40, 0, 20, 20)));

        entities.addSystem(brickA, brickB, brickC);
        entities.addSystem(new CombineStaticCollidersSystem());

        entities.update(); // one brick per cycle aligned
        entities.update(); // ...and another one

        var colliders = entities.fetchAll(Archetype.of(ColliderComponent.class));
        var bounds = colliders.get(0).get(TransformComponent.class).bounds;
        assertThat(colliders).hasSize(1);
        assertThat(bounds).isEqualTo(Bounds.atOrigin(0, 0, 60, 20));
    }

    @Test
    void update_combinesVerticallyAlignedColliders(DefaultEntities entities) {
        Entity brickA = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 20, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 40, 20, 20)));

        entities.addSystem(brickA, brickB, brickC);
        entities.addSystem(new CombineStaticCollidersSystem());

        entities.update(); // one brick per cycle aligned
        entities.update(); // ...and another one

        var colliders = entities.fetchAll(Archetype.of(ColliderComponent.class));
        var bounds = colliders.get(0).get(TransformComponent.class).bounds;
        assertThat(colliders).hasSize(1);
        assertThat(bounds).isEqualTo(Bounds.atOrigin(0, 0, 20, 60));
    }

    @Test
    void update_ignoresDifferentColliders(DefaultEntities entities) {
        Entity brickA = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(4),
                new TransformComponent(Bounds.atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(2),
                new TransformComponent(Bounds.atOrigin(20, 0, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticMarkerComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(40, 0, 20, 30)));

        entities.addSystem(brickA, brickB, brickC);
        entities.addSystem(new CombineStaticCollidersSystem());

        entities.update();

        var colliders = entities.fetchAll(Archetype.of(ColliderComponent.class));
        assertThat(colliders).hasSize(3);
    }

    @Test
    void update_removesItselfAfterFinishingAllColliders(DefaultEntities entities) {
        entities.addSystem(new CombineStaticCollidersSystem());

        entities.update();

        assertThat(entities.systems()).isEmpty();
    }
}
