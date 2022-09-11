package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.FadeOutComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class FadeOutSystemTest {

    @Test
    void update_reduecesOpacityOfSprites(DefaultEntities entityEngine, GameLoop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new SpriteComponent(1));

        entityEngine.add(smoke);
        entityEngine.add(new FadeOutSystem());

        entityEngine.update();

        Percentage opacity = smoke.get(SpriteComponent.class).opacity;
        assertThat(opacity).isEqualTo(Percentage.of(0.5));
    }

    @Test
    void update_removesOutFadedComponents(DefaultEntities entityEngine, GameLoop loop) {
        when(loop.delta()).thenReturn(0.5);

        Entity smoke = new Entity().add(new FadeOutComponent(1), new SpriteComponent(1));

        entityEngine.add(smoke);
        entityEngine.add(new FadeOutSystem());

        entityEngine.updateTimes(3);

        assertThat(entityEngine.entityCount()).isZero();
    }
}
