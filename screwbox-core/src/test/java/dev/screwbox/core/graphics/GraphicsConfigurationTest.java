package dev.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class GraphicsConfigurationTest {

    GraphicsConfiguration graphicsConfiguration;

    @Mock
    GraphicsConfigurationListener graphicsConfigListener;

    @BeforeEach
    void beforeEach() {
        graphicsConfiguration = new GraphicsConfiguration();
        graphicsConfiguration.addListener(graphicsConfigListener);
    }

    @Test
    void setLightmapBlur_blurIsTooHigh_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapBlur(7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("blur only supports values 0 (no blur) to 6 (heavy blur) (actual value: 7)");
    }

    @Test
    void addListener_listenerNull_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.addListener(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("listener must not be null");
    }

    @Test
    void setLightmapBlur_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightmapBlur(2);

        assertThat(graphicsConfiguration.lightmapBlur()).isEqualTo(2);

        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(LIGHTMAP_BLUR)));
    }

    @Test
    void setLightmapScale_scaleIsZero_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapScale(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("lightmap scale must be in range 1 to 16 (actual value: 0)");
    }

    @Test
    void setLightmapScale_scaleTooHeight_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapScale(27))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("lightmap scale must be in range 1 to 16 (actual value: 27)");
    }

    @Test
    void setLightmapScale_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightmapScale(3);

        assertThat(graphicsConfiguration.lightmapScale()).isEqualTo(3);
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(LIGHTMAP_SCALE)));
    }

    @Test
    void setUseAntialiasing_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setUseAntialiasing(true);

        assertThat(graphicsConfiguration.isUseAntialiasing()).isTrue();
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(ANTIALIASING)));
    }

    @Test
    void setResolution_resolutionSet_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setResolution(640, 480);

        assertThat(graphicsConfiguration.resolution()).isEqualTo(Size.of(640, 480));
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(RESOLUTION)));
    }

    @Test
    void setResolution_resolutionNull_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.setResolution(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("resolution must not be null");
    }

    @Test
    void toggleFullscreen_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleFullscreen();

        assertThat(graphicsConfiguration.isFullscreen()).isTrue();

        graphicsConfiguration.toggleFullscreen();

        assertThat(graphicsConfiguration.isFullscreen()).isFalse();
        verify(graphicsConfigListener, times(2)).configurationChanged(argThat(
                event -> event.changedProperty().equals(WINDOW_MODE)));
    }

    @Test
    void toggleAntialiasing_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleAntialiasing();

        assertThat(graphicsConfiguration.isUseAntialiasing()).isTrue();

        graphicsConfiguration.toggleAntialiasing();

        assertThat(graphicsConfiguration.isUseAntialiasing()).isFalse();
        verify(graphicsConfigListener, times(2)).configurationChanged(argThat(
                event -> event.changedProperty().equals(ANTIALIASING)));
    }

    @Test
    void setBackgroundColor_colorNull_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.setBackgroundColor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("background color must not be null");
    }

    @Test
    void setBackgroundColor_colorBlue_setsBackgroundColorBlueAndNotifiesListeners() {
        graphicsConfiguration.setBackgroundColor(Color.BLUE);

        assertThat(graphicsConfiguration.backgroundColor()).isEqualTo(Color.BLUE);
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(BACKGROUND_COLOR)));
    }

    @Test
    void setLightEnabled_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightEnabled(true);

        assertThat(graphicsConfiguration.isLightEnabled()).isTrue();
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(LIGHT_ENABLED)));
    }

    @Test
    void setAutoEnableLight_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setAutoEnableLight(false);

        assertThat(graphicsConfiguration.isAutoEnableLight()).isFalse();
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(AUTO_ENABLE_LIGHT)));
    }

    @Test
    void setOverlayShader_setsOverlayShaderAndNotifiesListeners() {
        graphicsConfiguration.setOverlayShader(ShaderBundle.WATER);

        assertThat(graphicsConfiguration.overlayShader()).isEqualTo(ShaderBundle.WATER.get());
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(OVERLAY_SHADER)));
    }

    @Test
    void setLensFlaresEnabled_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLensFlareEnabled(false);

        assertThat(graphicsConfiguration.isLensFlareEnabled()).isFalse();
        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(LENS_FLARE_ENABLED)));
    }

    @Test
    void toggleLensFlare_invertsLensFlareSettingAndNotifiesListeners() {
        assertThat(graphicsConfiguration.isLensFlareEnabled()).isTrue();
        graphicsConfiguration.toggleLensFlare();
        assertThat(graphicsConfiguration.isLensFlareEnabled()).isFalse();
        graphicsConfiguration.toggleLensFlare();
        assertThat(graphicsConfiguration.isLensFlareEnabled()).isTrue();

        verify(graphicsConfigListener, times(2)).configurationChanged(argThat(
                event -> event.changedProperty().equals(LENS_FLARE_ENABLED)));
    }

    @Test
    void setResolution_noAutoAdjustLightmapScale_doesNotChangeLightmapScale() {
        var initialScale = graphicsConfiguration.lightmapScale();

        var lightmapScale = graphicsConfiguration
                .setAutoAdjustLightmapScale(false)
                .setResolution(640, 480)
                .lightmapScale();

        assertThat(lightmapScale).isEqualTo(initialScale);
    }

    @Test
    void setResolution_autoAdjustLightmapScale_changesLightmapScaleAndRaisesEvents() {
        var lightmapScale = graphicsConfiguration
                .setResolution(640, 480)
                .lightmapScale();

        assertThat(lightmapScale).isEqualTo(3);

        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(RESOLUTION)));

        verify(graphicsConfigListener).configurationChanged(argThat(
                event -> event.changedProperty().equals(AUTO_ADJUST_LIGHTMAP_SCALE)));
    }

    @Test
    void xxxx() {
        var scaleDirect = graphicsConfiguration
                .setResolution(2560, 1440)
                .lightmapScale();

        var scaleAfterSecondSwitch = graphicsConfiguration
                .setResolution(2560, 1440)
                .lightmapScale();

        assertThat(scaleDirect).isEqualTo(scaleAfterSecondSwitch);
    }
//TODO refactor out verify code
    //TODO test setAutoAdjustLightmapScale posts event
}
