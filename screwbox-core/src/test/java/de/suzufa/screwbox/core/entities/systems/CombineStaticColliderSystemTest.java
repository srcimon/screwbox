package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.StaticMarkerComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class CombineStaticColliderSystemTest {

    @Test
    void update_combinesHorizontallyAlignedColliders(DefaultEntities entityEngine) {
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

        entityEngine.add(brickA, brickB, brickC);
        entityEngine.add(new CombineStaticCollidersSystem());

        entityEngine.update(); // one brick per cycle aligned
        entityEngine.update(); // ...and another one

        var colliders = entityEngine.fetchAll(Archetype.of(ColliderComponent.class));
        var bounds = colliders.get(0).get(TransformComponent.class).bounds;
        assertThat(colliders).hasSize(1);
        assertThat(bounds).isEqualTo(Bounds.atOrigin(0, 0, 60, 20));
    }

    @Test
    void update_combinesVerticallyAlignedColliders(DefaultEntities entityEngine) {
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

        entityEngine.add(brickA, brickB, brickC);
        entityEngine.add(new CombineStaticCollidersSystem());

        entityEngine.update(); // one brick per cycle aligned
        entityEngine.update(); // ...and another one

        var colliders = entityEngine.fetchAll(Archetype.of(ColliderComponent.class));
        var bounds = colliders.get(0).get(TransformComponent.class).bounds;
        assertThat(colliders).hasSize(1);
        assertThat(bounds).isEqualTo(Bounds.atOrigin(0, 0, 20, 60));
    }

    @Test
    void update_ignoresDifferentColliders(DefaultEntities entityEngine) {
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

        entityEngine.add(brickA, brickB, brickC);
        entityEngine.add(new CombineStaticCollidersSystem());

        entityEngine.update();

        var colliders = entityEngine.fetchAll(Archetype.of(ColliderComponent.class));
        assertThat(colliders).hasSize(3);
    }

    @Test
    void update_removesItselfAfterFinishingAllColliders(DefaultEntities entityEngine) {
        entityEngine.add(new CombineStaticCollidersSystem());

        entityEngine.update();

        assertThat(entityEngine.getSystems()).isEmpty();
    }
}
