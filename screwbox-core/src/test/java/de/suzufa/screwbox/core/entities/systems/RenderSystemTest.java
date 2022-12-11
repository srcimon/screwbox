package de.suzufa.screwbox.core.entities.systems;

import static de.suzufa.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.SpriteBatch;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith({ EntitiesExtension.class, MockitoExtension.class })
class RenderSystemTest {

    @Captor
    ArgumentCaptor<SpriteBatch> spriteBatch;

    @Test
    void bla(DefaultEntities entities, World world) {
        Sprite visibleBackgroundSprite = Sprite.invisible();
        Sprite visibleForegroundsSprite = Sprite.invisible();
        Sprite outOfBoundsSprite = Sprite.invisible();

        entities.add(new Entity()
                .add(new RenderComponent(visibleBackgroundSprite, 2))
                .add(new TransformComponent($$(100, 100, 32, 32))));

        entities.add(new Entity()
                .add(new RenderComponent(visibleForegroundsSprite, 1))
                .add(new TransformComponent($$(50, 50, 32, 32))));

        entities.add(new Entity()
                .add(new RenderComponent(outOfBoundsSprite))
                .add(new TransformComponent($$(400, 100, 32, 32))));

        Mockito.when(world.visibleArea()).thenReturn($$(0, 0, 200, 200));
        entities.add(new RenderSystem());

        entities.update();

        verify(world).drawSpriteBatch(spriteBatch.capture());

        var batchEntries = spriteBatch.getValue().entriesInDrawOrder();
        assertThat(batchEntries).hasSize(2);
        assertThat(batchEntries.get(0).sprite()).isEqualTo(visibleBackgroundSprite);
        assertThat(batchEntries.get(1).sprite()).isEqualTo(visibleForegroundsSprite);
    }
}
