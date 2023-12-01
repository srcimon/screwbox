package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.*;
import io.github.srcimon.screwbox.core.graphics.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.Vector.zero;
import static io.github.srcimon.screwbox.core.graphics.Sprite.invisible;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultWorldTest {

    @InjectMocks
    DefaultWorld world;

    @Mock
    Screen screen;

    @Test
    void updateCameraPosition_updatesVisibleArea() {
        when(screen.size()).thenReturn(Size.of(1024, 768));
        world.updateCameraPosition(Vector.of(100, 20));

        assertThat(world.visibleArea())
                .isEqualTo(Bounds.atPosition(100, 20, 1024, 768));
    }

    @Test
    void drawRectangle_inBounds_callsWindow() {
        when(screen.size()).thenReturn(Size.of(1024, 768));
        world.updateCameraPosition(zero());
        world.updateCameraZoom(2.5);

        world.fillRectangle(Bounds.atPosition(0, 0, 100, 100), Color.RED);

        verify(screen).fillRectangle(Offset.at(387, 259), Size.of(250, 250), Color.RED);
    }

    @Test
    void drawColor_setsColorForDrawing() {
        when(screen.size()).thenReturn(Size.of(1024, 768));
        world.drawColor(Color.BLUE);
        world.drawLine(Line.between(zero(), zero()));

        assertThat(world.drawColor()).isEqualTo(Color.BLUE);
        verify(screen).drawLine(Offset.at(512, 384), Offset.at(512, 384), Color.BLUE);
    }

    @Test
    void restrictZoomRangeTo_minNegative_exception() {
        assertThatThrownBy(() -> world.restrictZoomRangeTo(-2, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("min zoom must be positive");
    }

    @Test
    void restrictZoomRangeTo_maxBelowMin_exception() {
        assertThatThrownBy(() -> world.restrictZoomRangeTo(1, 0.3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("max zoom must not be lower than min zoom");
    }

    @Test
    void restrictZoomRangeTo_validMinAndMax_restrictsZoomRange() {
        when(screen.size()).thenReturn(Size.of(1024, 768));

        world.restrictZoomRangeTo(1, 5);

        assertThat(world.updateCameraZoom(0.2)).isEqualTo(1);
        assertThat(world.wantedZoom()).isEqualTo(1);

        assertThat(world.updateCameraZoom(12)).isEqualTo(5);
        assertThat(world.wantedZoom()).isEqualTo(5);
    }

    @Test
    void drawSprite_callsWindow() {
        when(screen.size()).thenReturn(Size.of(1024, 768));
        Sprite sprite = invisible();

        world.updateCameraPosition($(4, 2));
        world.updateCameraZoom(1.5);

        world.drawSprite(sprite, $(20, 4), 2, Percent.half(), Rotation.degrees(4), Flip.NONE, null);

        verify(screen)
                .drawSprite(sprite, Offset.at(535, 386), 3, Percent.half(), Rotation.degrees(4), Flip.NONE,
                        null);
    }
}