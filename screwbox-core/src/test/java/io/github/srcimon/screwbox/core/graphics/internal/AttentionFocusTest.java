package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        when(viewportManager.activeViewports()).thenReturn(List.of(firstViewport, secondViewport));
    }
    @Test
    void distanceTo_samePositionAsNerestCamera_isZero() {
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

    //TODO FIX
//    @Test
//    void direction_leftOfAllCameras_isDirectionVectorToTheLeft() {
//        when(firstCamera.position()).thenReturn($(10, 40));
//        when(secondCamera.position()).thenReturn($(4, 40));
//
//        Vector direction = attentionFocus.direction($(-20, 90));
//
//        assertThat(direction.length()).isEqualTo(1.0, offset(0.01));
//        assertThat(direction.x()).isEqualTo(-0.51, offset(0.01));
//        assertThat(direction.y()).isEqualTo(0.86, offset(0.01));
//    }
//
//    @Test
//    void direction_rightOfCamera_sDirectionVectorToTheRight() {
//        when(firstCamera.position()).thenReturn($(10, 40));
//
//        Vector direction = attentionFocus.direction($(20, 90));
//        assertThat(direction.length()).isEqualTo(1.0, offset(0.01));
//        assertThat(direction.x()).isEqualTo(0.196, offset(0.01));
//        assertThat(direction.y()).isEqualTo(0.98, offset(0.01));
//    }
}
