package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.FadeOutComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class FadeOutSystemTest {

    @Test
    void update_reduecesOpacityOfSprites(DefaultEntities entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new RenderComponent(1));

        entities.add(smoke);
        entities.add(new FadeOutSystem());

        entities.update();

        Percent opacity = smoke.get(RenderComponent.class).opacity;
        assertThat(opacity).isEqualTo(Percent.of(0.5));
    }

    @Test
    void update_removesOutFadedComponents(DefaultEntities entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new RenderComponent(1));

        entities.add(smoke);
        entities.add(new FadeOutSystem());

        entities.updateTimes(3);

        assertThat(entities.entityCount()).isZero();
    }
}
