package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.components.FadeOutComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.ecosphere.internal.DefaultEcosphere;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class FadeOutSystemTest {

    @Test
    void update_reduecesOpacityOfSprites(DefaultEcosphere entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new RenderComponent(1));

        entities.addEntity(smoke);
        entities.addSystem(new FadeOutSystem());

        entities.update();

        Percent opacity = smoke.get(RenderComponent.class).opacity;
        assertThat(opacity).isEqualTo(Percent.of(0.5));
    }

    @Test
    void update_removesOutFadedComponents(DefaultEcosphere entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new RenderComponent(1));

        entities.addEntity(smoke);
        entities.addSystem(new FadeOutSystem());

        entities.updateTimes(3);

        assertThat(entities.entityCount()).isZero();
    }
}
