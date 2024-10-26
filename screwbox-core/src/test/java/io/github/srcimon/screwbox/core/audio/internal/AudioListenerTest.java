package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioListenerTest {

    @Mock
    Graphics graphics;

    @Mock
    Camera camera;

    @InjectMocks
    AudioListener audioListener;

    @BeforeEach
    void setUp() {
        when(graphics.camera()).thenReturn(camera);
    }

    @Test
    void distanceTo_samePositionAsCamera_isZero() {
        when(camera.position()).thenReturn($(20, 90));

        assertThat(audioListener.distanceTo($(20, 90))).isZero();
    }

    @Test
    void distanceTo_awayFromCamera_isDistanceToCamera() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(audioListener.distanceTo($(20, 90))).isEqualTo(50.99, offset(0.1));
    }

    @Test
    void relativePosition_leftOfCamera_isMinusOne() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(audioListener.relativePosition($(-20, 90))).isEqualTo(-1.0);
    }

    @Test
    void relativePosition_rightOfCamera_isOne() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(audioListener.relativePosition($(20, 90))).isEqualTo(1.0);
    }
}
