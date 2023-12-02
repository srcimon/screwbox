package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.entities.components.StaticShadowCasterMarkerComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Bounds.atOrigin;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntitiesExtension.class)
class CombineStaticShadowCastersSystemTest {

    @Test
    void update_combinesHorizontallyAlignedColliders(DefaultEntities entities) {
        Entity brickA = new Entity().add(
                new StaticShadowCasterMarkerComponent(),
                new ShadowCasterComponent(),
                new TransformComponent(atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticShadowCasterMarkerComponent(),
                new ShadowCasterComponent(),
                new TransformComponent(atOrigin(20, 0, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticShadowCasterMarkerComponent(),
                new ShadowCasterComponent(),
                new TransformComponent(atOrigin(40, 0, 20, 20)));

        entities.addSystem(brickA, brickB, brickC);
        entities.addSystem(new CombineStaticShadowCastersSystem());

        entities.update(); // one brick per cycle aligned
        entities.update(); // ...and another one

        var shadowCasters = entities.fetchAll(Archetype.of(ShadowCasterComponent.class));
        var bounds = shadowCasters.get(0).get(TransformComponent.class).bounds;
        assertThat(shadowCasters).hasSize(1);
        assertThat(bounds).isEqualTo(atOrigin(0, 0, 60, 20));
    }

    @Test
    void update_removesItselfAfterFinishingAllEntities(DefaultEntities entities) {
        entities.addSystem(new CombineStaticShadowCastersSystem());

        entities.update();

        assertThat(entities.systems()).isEmpty();
    }
}
