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
class VisualAttentionTest {

    @Mock
    Graphics graphics;

    @Mock
    Camera camera;

    @InjectMocks
    VisualAttention visualAttention;

    @BeforeEach
    void setUp() {
        when(graphics.camera()).thenReturn(camera);
    }

    @Test
    void distanceTo_samePositionAsCamera_isZero() {
        when(camera.position()).thenReturn($(20, 90));

        assertThat(visualAttention.distanceTo($(20, 90))).isZero();
    }

    @Test
    void distanceTo_awayFromCamera_isDistanceToCamera() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(visualAttention.distanceTo($(20, 90))).isEqualTo(50.99, offset(0.1));
    }

    @Test
    void xDirection_leftOfCamera_isMinusOne() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(visualAttention.xDirection($(-20, 90))).isEqualTo(-1.0);
    }

    @Test
    void xDirection_rightOfCamera_isOne() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(visualAttention.xDirection($(20, 90))).isEqualTo(1.0);
    }
}
