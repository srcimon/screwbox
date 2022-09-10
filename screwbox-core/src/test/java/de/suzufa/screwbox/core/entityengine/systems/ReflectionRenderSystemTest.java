package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ReflectionComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.SpriteBatch;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith({ EntityEngineExtension.class, MockitoExtension.class })
class ReflectionRenderSystemTest {

    private static final Sprite SPRITE = Sprite.fromFile("tile.bmp");

    @Captor
    ArgumentCaptor<SpriteBatch> spriteBatch;

    @Captor
    ArgumentCaptor<Bounds> restrictedArea;

    @Test
    void update_entityInReflectedArea_drawsReflection(DefaultEntityEngine entityEngine, GameLoop loop, World world) {
        when(loop.lastUpdate()).thenReturn(Time.atNanos(500000));
        when(world.visibleArea()).thenReturn($$(0, 0, 1024, 768));

        Entity body = new Entity()
                .add(new TransformComponent($$(0, 0, 10, 10)))
                .add(new SpriteComponent(SPRITE));

        Entity mirror = new Entity()
                .add(new TransformComponent($$(0, 10, 10, 10)))
                .add(new ReflectionComponent());

        entityEngine.add(body)
                .add(mirror)
                .add(new ReflectionRenderSystem());

        entityEngine.update();

        verify(world).drawSpriteBatch(spriteBatch.capture(), restrictedArea.capture());

        var spriteBatchEntries = spriteBatch.getValue().entriesInDrawOrder();
        assertThat(spriteBatchEntries).hasSize(1);
        assertThat(restrictedArea.getValue()).isEqualTo($$(0, 10, 10, 10));

        var spriteBatchEntry = spriteBatchEntries.get(0);
        assertThat(spriteBatchEntry.flipMode()).isEqualTo(FlipMode.VERTICAL);
        assertThat(spriteBatchEntry.sprite()).isEqualTo(SPRITE);
        assertThat(spriteBatchEntry.position()).isEqualTo($(-3.152647485592124, 6));
    }

    @Test
    void update_reflectionAreaNotInWindow_drawsNothing(DefaultEntityEngine entityEngine, GameLoop loop, World world) {
        when(loop.lastUpdate()).thenReturn(Time.atNanos(500000));
        when(world.visibleArea()).thenReturn($$(0, 0, 1024, 768));

        Entity body = new Entity()
                .add(new TransformComponent($$(2000, 0, 10, 10)))
                .add(new SpriteComponent(SPRITE));

        Entity mirror = new Entity()
                .add(new TransformComponent($$(2000, 10, 10, 10)))
                .add(new ReflectionComponent());

        entityEngine.add(body)
                .add(mirror)
                .add(new ReflectionRenderSystem());

        entityEngine.update();

        verify(world, never()).drawSpriteBatch(any(), any());
    }
}
