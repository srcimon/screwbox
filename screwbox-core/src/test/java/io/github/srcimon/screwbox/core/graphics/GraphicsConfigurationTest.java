package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
                .hasMessage("blur only supports values 0 (no blur) to 6 (heavy blur)");
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
                .hasMessage("lightmap scale must be positive");
    }

    @Test
    void setLightmapScale_scaleTooHight_throwsException() {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapScale(7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("lightmap scale supports only values up to 6");
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

        assertThat(graphicsConfiguration.isUseAntialising()).isTrue();
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
    void toggleAntialising_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleAntialising();

        assertThat(graphicsConfiguration.isUseAntialising()).isTrue();

        graphicsConfiguration.toggleAntialising();

        assertThat(graphicsConfiguration.isUseAntialising()).isFalse();
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
}
