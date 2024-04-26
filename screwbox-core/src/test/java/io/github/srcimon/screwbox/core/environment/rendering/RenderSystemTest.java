package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderSystemTest {

    @Captor
    ArgumentCaptor<SpriteBatch> spriteBatch;

    @Test
    void update_rendersSprites(DefaultEnvironment environment, World world) {
        Sprite visibleBackgroundSprite = Sprite.invisible();
        Sprite visibleForegroundsSprite = Sprite.invisible();
        Sprite outOfBoundsSprite = Sprite.invisible();

        environment.addEntity(new Entity()
                .add(new RenderComponent(visibleBackgroundSprite, 2))
                .add(new TransformComponent($$(100, 100, 32, 32))));

        environment.addEntity(new Entity()
                .add(new RenderComponent(visibleForegroundsSprite, 1))
                .add(new TransformComponent($$(50, 50, 32, 32))));

        environment.addEntity(new Entity()
                .add(new RenderComponent(outOfBoundsSprite))
                .add(new TransformComponent($$(400, 100, 32, 32))));

        when(world.visibleArea()).thenReturn($$(0, 0, 200, 200));
        environment.addSystem(new RenderSystem());

        environment.update();

        verify(world).drawSpriteBatch(spriteBatch.capture());

        var batchEntries = spriteBatch.getValue().entriesInDrawOrder();
        assertThat(batchEntries).hasSize(2);
        assertThat(batchEntries.get(0).sprite()).isEqualTo(visibleBackgroundSprite);
        assertThat(batchEntries.get(1).sprite()).isEqualTo(visibleForegroundsSprite);
    }
}
