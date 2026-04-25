package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.ScreenBounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@MockitoSettings
class AttentionFocusTest {

    @Mock
    DefaultCamera firstCamera;

    @Mock
    DefaultCamera secondCamera;

    @Mock
    ViewportManager viewportManager;

    @InjectMocks
    AttentionFocus attentionFocus;

    @BeforeEach
    void setUp() {
        var firstViewport = new DefaultViewport(new DefaultCanvas(null, new ScreenBounds(0, 0, 100, 100)), firstCamera);
        var secondViewport = new DefaultViewport(new DefaultCanvas(null, new ScreenBounds(0, 0, 100, 100)), secondCamera);
        when(viewportManager.viewports()).thenReturn(List.of(firstViewport, secondViewport));
    }

    @Test
    void distanceTo_samePositionAsNearestCamera_isZero() {
        when(firstCamera.position()).thenReturn($(20, 90));
        when(secondCamera.position()).thenReturn($(2510, 960));

        assertThat(attentionFocus.distanceTo($(20, 90))).isZero();
    }

    @Test
    void distanceTo_awayFromCamera_isDistanceToNearestCamera() {
        when(firstCamera.position()).thenReturn($(80, 10));
        when(secondCamera.position()).thenReturn($(10, 40));

        assertThat(attentionFocus.distanceTo($(20, 90))).isEqualTo(50.99, offset(0.1));
    }

    @Test
    void direction_leftOfAllCameras_isDirectionVectorToTheLeft() {
        when(firstCamera.position()).thenReturn($(10, 40));
        when(secondCamera.position()).thenReturn($(4, 40));

        Vector direction = attentionFocus.direction($(-20, 90));

        assertThat(direction.length()).isEqualTo(1.0, offset(0.01));
        assertThat(direction.x()).isEqualTo(-0.47, offset(0.01));
        assertThat(direction.y()).isEqualTo(0.88, offset(0.01));
    }

    @Test
    void direction_rightOfCameras_sDirectionVectorToTheRight() {
        when(firstCamera.position()).thenReturn($(10, 40));
        when(secondCamera.position()).thenReturn($(4, 40));

        Vector direction = attentionFocus.direction($(20, 90));
        assertThat(direction.length()).isEqualTo(1.0, offset(0.01));
        assertThat(direction.x()).isEqualTo(0.25, offset(0.01));
        assertThat(direction.y()).isEqualTo(0.97, offset(0.01));
    }

    @Test
    void isVisible_visibleInSecondViewport_isTrue() {
        when(firstCamera.focus()).thenReturn($(5000, 5000));
        when(firstCamera.zoom()).thenReturn(1.0);
        when(secondCamera.focus()).thenReturn($(0, 0));
        when(secondCamera.zoom()).thenReturn(1.0);
        assertThat(attentionFocus.isVisible(Bounds.$$(20, 10, 4, 4))).isTrue();
    }

    @Test
    void isVisible_notIntersectingAnyViewport_isFalse() {
        when(firstCamera.focus()).thenReturn($(5000, 5000));
        when(firstCamera.zoom()).thenReturn(1.0);
        when(secondCamera.focus()).thenReturn($(5000, 5000));
        when(secondCamera.zoom()).thenReturn(1.0);
        assertThat(attentionFocus.isVisible(Bounds.$$(20, 10, 4, 4))).isFalse();
    }
}
