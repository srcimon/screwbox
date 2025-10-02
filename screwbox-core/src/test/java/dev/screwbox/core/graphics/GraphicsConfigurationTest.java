package dev.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.verification.VerificationMode;

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
        verifyEventPosted(LIGHTMAP_BLUR, times(1));

    }

    @Test
    void setUseAntialiasing_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setUseAntialiasing(true);

        assertThat(graphicsConfiguration.isUseAntialiasing()).isTrue();
        verifyEventPosted(ANTIALIASING, times(1));
    }

    @Test
    void setResolution_resolutionSet_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setResolution(640, 480);

        assertThat(graphicsConfiguration.resolution()).isEqualTo(Size.of(640, 480));
        verifyEventPosted(RESOLUTION, times(1));
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
        verifyEventPosted(FULLSCREEN, times(2));
    }

    @Test
    void toggleAntialiasing_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleAntialiasing();

        assertThat(graphicsConfiguration.isUseAntialiasing()).isTrue();

        graphicsConfiguration.toggleAntialiasing();

        assertThat(graphicsConfiguration.isUseAntialiasing()).isFalse();
        verifyEventPosted(ANTIALIASING, times(2));
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
        verifyEventPosted(BACKGROUND_COLOR, times(1));
    }

    @Test
    void setLightEnabled_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightEnabled(true);

        assertThat(graphicsConfiguration.isLightEnabled()).isTrue();
        verifyEventPosted(LIGHT_ENABLED, times(1));
    }

    @Test
    void setAutoEnableLight_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setAutoEnableLight(false);

        assertThat(graphicsConfiguration.isAutoEnableLight()).isFalse();
        verifyEventPosted(AUTO_ENABLE_LIGHT, times(1));
    }

    @Test
    void setOverlayShader_setsOverlayShaderAndNotifiesListeners() {
        graphicsConfiguration.setOverlayShader(ShaderBundle.WATER);

        assertThat(graphicsConfiguration.overlayShader()).isEqualTo(ShaderBundle.WATER.get());
        verifyEventPosted(OVERLAY_SHADER, times(1));
    }

    @Test
    void setLensFlaresEnabled_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLensFlareEnabled(false);

        assertThat(graphicsConfiguration.isLensFlareEnabled()).isFalse();
        verifyEventPosted(LENS_FLARE_ENABLED, times(1));
    }

    @Test
    void toggleLensFlare_invertsLensFlareSettingAndNotifiesListeners() {
        assertThat(graphicsConfiguration.isLensFlareEnabled()).isTrue();
        graphicsConfiguration.toggleLensFlare();
        assertThat(graphicsConfiguration.isLensFlareEnabled()).isFalse();
        graphicsConfiguration.toggleLensFlare();
        assertThat(graphicsConfiguration.isLensFlareEnabled()).isTrue();

        verifyEventPosted(LENS_FLARE_ENABLED, times(2));
    }

    private void verifyEventPosted(final GraphicsConfigurationEvent.ConfigurationProperty configurationProperty, final VerificationMode times) {
        verify(graphicsConfigListener, times)
                .configurationChanged(argThat(event -> event.changedProperty().equals(configurationProperty)));
    }
}
