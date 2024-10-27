package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Vector;
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
class AttentionFocusTest {

    @Mock
    Graphics graphics;

    @Mock
    Camera camera;

    @InjectMocks
    AttentionFocus attentionFocus;

    @BeforeEach
    void setUp() {
        when(graphics.camera()).thenReturn(camera);
    }

    @Test
    void distanceTo_samePositionAsCamera_isZero() {
        when(camera.position()).thenReturn($(20, 90));

        assertThat(attentionFocus.distanceTo($(20, 90))).isZero();
    }

    @Test
    void distanceTo_awayFromCamera_isDistanceToCamera() {
        when(camera.position()).thenReturn($(10, 40));

        assertThat(attentionFocus.distanceTo($(20, 90))).isEqualTo(50.99, offset(0.1));
    }

    @Test
    void direction_leftOfCamera_isDirectionVectorToTheLeft() {
        when(camera.position()).thenReturn($(10, 40));

        Vector direction = attentionFocus.direction($(-20, 90));

        assertThat(direction.length()).isEqualTo(1.0, offset(0.01));
        assertThat(direction.x()).isEqualTo(-0.51, offset(0.01));
        assertThat(direction.y()).isEqualTo(0.86, offset(0.01));
    }

    @Test
    void direction_rightOfCamera_sDirectionVectorToTheRight() {
        when(camera.position()).thenReturn($(10, 40));

        Vector direction = attentionFocus.direction($(20, 90));
        assertThat(direction.length()).isEqualTo(1.0, offset(0.01));
        assertThat(direction.x()).isEqualTo(0.196, offset(0.01));
        assertThat(direction.y()).isEqualTo(0.98, offset(0.01));
    }
}
