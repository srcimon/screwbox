package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.FadeOutComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class FadeOutSystemTest {

    @Test
    void update_reduecesOpacityOfSprites(DefaultEntities entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new SpriteComponent(1));

        entities.add(smoke);
        entities.add(new FadeOutSystem());

        entities.update();

        Percent opacity = smoke.get(SpriteComponent.class).opacity;
        assertThat(opacity).isEqualTo(Percent.of(0.5));
    }

    @Test
    void update_removesOutFadedComponents(DefaultEntities entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new SpriteComponent(1));

        entities.add(smoke);
        entities.add(new FadeOutSystem());

        entities.updateTimes(3);

        assertThat(entities.entityCount()).isZero();
    }
}
