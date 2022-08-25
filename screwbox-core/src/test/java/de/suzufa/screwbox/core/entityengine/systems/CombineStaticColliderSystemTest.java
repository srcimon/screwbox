package de.suzufa.screwbox.core.entityengine.systems;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.StaticMarkerComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.test.extensions.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class CombineStaticColliderSystemTest {

    @Test
    void update_combinesHorizontallyAlignedColliders(DefaultEntityEngine entityEngine) {
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
    void update_combinesVerticallyAlignedColliders(DefaultEntityEngine entityEngine) {
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
    void update_ignoresDifferentColliders(DefaultEntityEngine entityEngine) {
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
    void update_removesItselfAfterFinishingAllColliders(DefaultEntityEngine entityEngine) {
        entityEngine.add(new CombineStaticCollidersSystem());

        entityEngine.update();

        assertThat(entityEngine.getSystems()).isEmpty();
    }
}
