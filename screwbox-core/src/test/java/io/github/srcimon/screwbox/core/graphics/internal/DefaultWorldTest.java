package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.BundledSprites;
import io.github.srcimon.screwbox.core.graphics.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.Vector.zero;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultWorldTest {

    DefaultWorld world;

    @Mock
    Screen screen;

    @BeforeEach
    void setUp() {
        when(screen.size()).thenReturn(Size.of(1024, 768));
        world = new DefaultWorld(screen);
    }

    @Test
    void visibleArea_noCameraUpdateYet_returnsVisibleArea() {
        assertThat(world.visibleArea()).isEqualTo(Bounds.$$(-512, -384, 1024, 768));
    }

    @Test
    void updateCameraPosition_updatesVisibleArea() {
        world.updateCameraPosition(Vector.of(100, 20));

        assertThat(world.visibleArea())
                .isEqualTo(Bounds.atPosition(100, 20, 1024, 768));
    }

    @Test
    void drawCircle_callsScreen() {
        world.updateCameraPosition($(4, 2));
        world.updateZoom(2);

        world.drawCircle($(20, 19), 10, CircleDrawOptions.fading(RED));
        verify(screen).drawCircle(Offset.at(544, 418), 20, CircleDrawOptions.fading(RED));
    }

    @Test
    void drawRectangle_callsScreen() {
        world.updateCameraPosition(zero());
        world.updateZoom(2.5);

        world.drawRectangle(Bounds.atPosition(0, 0, 100, 100), RectangleDrawOptions.filled(RED));

        verify(screen).drawRectangle(Offset.at(387, 259), Size.of(250, 250), RectangleDrawOptions.filled(RED));
    }

    @Test
    void drawSpriteBatch_twoSprites_drawsSpriteInDrawOrder() {
        Sprite second = BundledSprites.BLOB_MOVING_16.get();
        Sprite first = BundledSprites.MOON_SURFACE_16.get();

        var batch = new SpriteBatch();
        batch.addEntry(second, $(10, 20), SpriteDrawOptions.scaled(2), 2);
        batch.addEntry(first, $(41, 20), SpriteDrawOptions.originalSize(), 1);

        world.drawSpriteBatch(batch, Bounds.$$(40, 40, 100, 200));
        InOrder orderVerifier = inOrder(screen);

        orderVerifier.verify(screen).drawSprite(first, Offset.at(553, 404), SpriteDrawOptions.originalSize(), new ScreenBounds(552, 424, 100, 200));
        orderVerifier.verify(screen).drawSprite(second, Offset.at(506, 388), SpriteDrawOptions.scaled(2), new ScreenBounds(552, 424, 100, 200));
    }

    @Test
    void drawText_callsScreen() {
        world.drawText($(20, 19), "Hello World", TextDrawOptions.systemFont("Arial").bold());

        verify(screen).drawText(Offset.at(532, 403), "Hello World", TextDrawOptions.systemFont("Arial").bold());
    }
}