package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Camera;
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
    Camera firstCamera;

    @Mock
    Camera secondCamera;

    @Mock
    ViewportManager viewportManager;

    @InjectMocks
    AttentionFocus attentionFocus;

    @BeforeEach
    void setUp() {
        var firstViewport = new DefaultViewport(null, firstCamera);
        var secondViewport = new DefaultViewport(null, secondCamera);
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
}
