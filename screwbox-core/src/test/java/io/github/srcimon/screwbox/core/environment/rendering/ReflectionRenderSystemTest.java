package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.*;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class ReflectionRenderSystemTest {

    private static final Sprite SPRITE = Sprite.fromFile("tile.bmp");

    @Captor
    ArgumentCaptor<SpriteBatch> spriteBatch;

    @Captor
    ArgumentCaptor<Bounds> restrictedArea;

    @Test
    void update_entityInReflectedArea_drawsReflection(DefaultEnvironment environment, Loop loop, World world) {
        when(loop.lastUpdate()).thenReturn(Time.atNanos(500000000));
        when(world.visibleArea()).thenReturn($$(0, 0, 1024, 768));

        Entity body = new Entity()
                .add(new TransformComponent($$(0, 0, 10, 10)))
                .add(new RenderComponent(SPRITE));

        Entity mirror = new Entity()
                .add(new TransformComponent($$(0, 10, 10, 10)))
                .add(new ReflectionComponent());

        environment.addEntity(body)
                .addEntity(mirror)
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        verify(world).drawSpriteBatch(spriteBatch.capture(), restrictedArea.capture());

        var spriteBatchEntries = spriteBatch.getValue().entriesInDrawOrder();
        assertThat(spriteBatchEntries).hasSize(1);
        assertThat(restrictedArea.getValue()).isEqualTo($$(0, 10, 10, 10));

        var spriteBatchEntry = spriteBatchEntries.getFirst();
        assertThat(spriteBatchEntry.options()).isEqualTo(SpriteDrawOptions.originalSize().flipVertical(true).opacity(Percent.of(0.25)));
        assertThat(spriteBatchEntry.sprite()).isEqualTo(SPRITE);
        assertThat(spriteBatchEntry.position()).isEqualTo($(-3, 7));
    }

    @RepeatedTest(2)
    void update_reflectionAreaNotInWindow_drawsNoSprites(DefaultEnvironment environment, Loop loop,
                                                         World world) {
        when(loop.lastUpdate()).thenReturn(Time.now());
        when(world.visibleArea()).thenReturn($$(0, 0, 1024, 768));

        Entity body = new Entity()
                .add(new TransformComponent($$(2000, 0, 10, 10)))
                .add(new RenderComponent(SPRITE));

        Entity mirror = new Entity()
                .add(new TransformComponent($$(2000, 10, 10, 10)))
                .add(new ReflectionComponent());

        environment.addEntity(body)
                .addEntity(mirror)
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        verify(world, never()).drawSpriteBatch(any(), any());
    }

    @Test
    void update_waveReflectionEffectUsed_drawsOnDifferenPositions(DefaultEnvironment environment, Loop loop,
                                                                  World world) {
        when(loop.lastUpdate()).thenReturn(Time.atNanos(500000000));
        when(world.visibleArea()).thenReturn($$(0, 0, 1024, 768));

        Entity body = new Entity()
                .add(new TransformComponent($$(0, 0, 10, 10)))
                .add(new RenderComponent(SPRITE));

        Entity mirror = new Entity()
                .add(new TransformComponent($$(0, 10, 10, 10)))
                .add(new ReflectionComponent(Percent.half(), true));

        environment.addEntity(body)
                .addEntity(mirror)
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        verify(world).drawSpriteBatch(spriteBatch.capture(), restrictedArea.capture());

        var spriteBatchEntries = spriteBatch.getValue().entriesInDrawOrder();
        assertThat(spriteBatchEntries).hasSize(1);
        assertThat(restrictedArea.getValue()).isEqualTo($$(0, 10, 10, 10));

        var spriteBatchEntry = spriteBatchEntries.getFirst();
        assertThat(spriteBatchEntry.options().isFlipHorizontal()).isFalse();
        assertThat(spriteBatchEntry.options().isFlipVertical()).isTrue();
        assertThat(spriteBatchEntry.sprite()).isEqualTo(SPRITE);
        assertThat(spriteBatchEntry.options().opacity().value()).isEqualTo(0.48, offset(0.01));
        assertThat(spriteBatchEntry.position().x()).isEqualTo(-1.55, offset(0.01));
        assertThat(spriteBatchEntry.position().y()).isEqualTo(8.68, offset(0.01));
    }
}
