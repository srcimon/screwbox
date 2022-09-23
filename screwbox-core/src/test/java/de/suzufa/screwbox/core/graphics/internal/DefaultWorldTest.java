package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.Vector.zero;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;

@ExtendWith(MockitoExtension.class)
class DefaultWorldTest {

    @InjectMocks
    DefaultWorld world;

    @Mock
    Window window;

    @Test
    void updateCameraPosition_updatesVisibleArea() {
        when(window.size()).thenReturn(Dimension.of(1024, 768));
        world.updateCameraPosition(Vector.of(100, 20));

        assertThat(world.visibleArea())
                .isEqualTo(Bounds.atPosition(100, 20, 1024, 768));
    }

    @Test
    void drawRectangle_inBounds_callsWindow() {
        when(window.size()).thenReturn(Dimension.of(1024, 768));
        world.updateCameraPosition(zero());
        world.updateCameraZoom(2.5);

        world.drawRectangle(Bounds.atPosition(0, 0, 100, 100), Color.RED);

        verify(window).drawRectangle(Offset.at(387, 259), Dimension.of(250, 250), Color.RED);
    }

    @Test
    void drawColor_setsColorForDrawing() {
        when(window.size()).thenReturn(Dimension.of(1024, 768));
        world.drawColor(Color.BLUE);
        world.drawLine(Segment.between(zero(), zero()));

        assertThat(world.drawColor()).isEqualTo(Color.BLUE);
        verify(window).drawLine(Offset.at(512, 384), Offset.at(512, 384), Color.BLUE);
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
}