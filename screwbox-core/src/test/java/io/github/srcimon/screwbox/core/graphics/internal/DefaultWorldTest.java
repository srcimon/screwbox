package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Flip;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.Vector.zero;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Sprite.invisible;
import static org.assertj.core.api.Assertions.assertThat;
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
    void drawSprite_callsScreen() {
        Sprite sprite = invisible();

        world.updateCameraPosition($(4, 2));
        world.updateZoom(1.5);

        world.drawSprite(sprite, $(20, 4), 2, Percent.half(), Rotation.degrees(4), Flip.NONE, null);

        verify(screen).drawSprite(sprite, Offset.at(535, 386), 3, Percent.half(), Rotation.degrees(4), Flip.NONE,
                null);
    }
}