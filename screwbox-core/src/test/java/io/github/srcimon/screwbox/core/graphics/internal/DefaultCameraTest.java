package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions.infinite;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions.lastingForDuration;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultCameraTest {

    @Mock
    Canvas canvas;

    @InjectMocks
    DefaultCamera camera;

    @Test
    void changeZoomBy_changeIsPixelperfectValue_changesZoom() {
        var result = camera.changeZoomBy(1);

        assertThat(camera.zoom()).isEqualTo(2.0);
        assertThat(result).isEqualTo(2.0);
    }

    @Test
    void changeZoomBy_changeIsNotPixelperfectValue_changesZoomByPixelperfectValue() {
        var result = camera.changeZoomBy(0.2);

        assertThat(camera.zoom()).isEqualTo(1.1875);
        assertThat(result).isEqualTo(1.1875);
    }

    @Test
    void moveCameraWithinVisualBounds_cameraIsWithinBounds_updatesCameraPosition() {
        camera.setPosition($(10, 10));
        when(canvas.width()).thenReturn(100);
        when(canvas.height()).thenReturn(100);

        var result = camera.moveWithinVisualBounds($(20, 20), $$(-200, -20, 400, 400));

        assertThat(result).isEqualTo($(30, 30));
    }

    @Test
    void updatePosition_positionNull_throwsException() {
        assertThatThrownBy(() -> camera.setPosition(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("position must not be NULL");
    }


    @Test
    void setZoomRestriction_minNegative_exception() {
        assertThatThrownBy(() -> camera.setZoomRestriction(-2, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("min zoom must be positive");
    }

    @Test
    void setZoomRestriction_maxBelowMin_exception() {
        assertThatThrownBy(() -> camera.setZoomRestriction(1, 0.3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("max zoom must not be lower than min zoom");
    }

    @Test
    void setZoomRestriction_validMinAndMax_restrictsZoomRange() {
        camera.setZoomRestriction(1, 5);

        assertThat(camera.setZoom(0.2)).isEqualTo(1);
        assertThat(camera.zoom()).isEqualTo(1);
        assertThat(camera.minZoom()).isEqualTo(1);

        assertThat(camera.setZoom(12)).isEqualTo(5);
        assertThat(camera.zoom()).isEqualTo(5);
        assertThat(camera.maxZoom()).isEqualTo(5);
    }

    @Test
    void isShaking_noActiveShake_isFalse() {
        camera.update();

        assertThat(camera.isShaking()).isFalse();
    }

    @Test
    void isShaking_hasActiveShake_isTrue() {
        camera.shake(infinite());

        camera.update();

        assertThat(camera.isShaking()).isTrue();
    }

    @Test
    void isShaking_shakeHasEnded_isFalse() {
        camera.shake(lastingForDuration(ofMillis(10)));
        TestUtil.sleep(ofMillis(20));

        camera.update();

        assertThat(camera.isShaking()).isFalse();
    }

    @Test
    void setZoom_notPixelperfect_setsPixelperfectValueAndUpdatesWorld() {
        var result = camera.setZoom(3.2);

        assertThat(camera.zoom()).isEqualTo(3.1875);
        assertThat(result).isEqualTo(3.1875);
    }

    @Test
    void stopShaking_notShaking_noException() {
        assertThatNoException().isThrownBy(camera::stopShaking);
    }

    @Test
    void stopShaking_isShaking_stopsShaking() {
        camera.shake(CameraShakeOptions.infinite());

        camera.stopShaking();

        assertThat(camera.isShaking()).isFalse();
    }

    @Test
    void focus_noShake_isSameAsPosition() {
        assertThat(camera.focus()).isEqualTo(camera.position());
    }

    @RepeatedTest(3)
    void focus_isShaking_isDifferentFromPositionButLessThanStrength() {
        camera.shake(CameraShakeOptions.infinite().strength(4));

        camera.update();

        assertThat(camera.focus().distanceTo(camera.position())).isLessThan(6);
        assertThat(camera.focus()).isNotEqualTo(camera.position());
    }

    @Test
    void update_noActiveScreenShake_resetsSwing() {
        camera.update();

        assertThat(camera.swing()).isEqualTo(Rotation.none());
    }

    @Test
    void update_activeScreenShake_setsScreenShake() {
        camera.shake(CameraShakeOptions.infinite().strength(40).swing(Rotation.degrees(45)));

        camera.update();

        assertThat(camera.focus()).isNotEqualTo(camera.position());
        assertThat(camera.swing()).isNotEqualTo(Rotation.none());
    }

}
