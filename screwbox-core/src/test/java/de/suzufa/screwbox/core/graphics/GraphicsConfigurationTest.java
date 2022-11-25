package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty;

@ExtendWith(MockitoExtension.class)
class GraphicsConfigurationTest {

    GraphicsConfiguration graphicsConfiguration;

    @Mock
    GraphicsConfigurationListener graphicsConfigListener;

    @BeforeEach
    void beforeEach() {
        graphicsConfiguration = new GraphicsConfiguration();
        graphicsConfiguration.registerListener(graphicsConfigListener);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 7 })
    void setLightmapBlur_outOfRange_throwsException(int blur) {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapBlur(blur))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("valid range for lightmap blur is 0 (no blur) to 6 (heavy blur)");
    }

    @Test
    void setLightmapBlur_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightmapBlur(2);

        assertThat(graphicsConfiguration.lightmapBlur()).isEqualTo(2);
        verify(graphicsConfigListener).configurationChanged(ConfigurationProperty.LIGHTMAP_BLUR);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 7 })
    void setLightmapResolution_outOfRange_throwsException(int resolution) {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapResolution(resolution))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("valid range for lightmap resolution is 1 to 6");
    }

    @Test
    void setLightmapResolution_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightmapResolution(3);

        assertThat(graphicsConfiguration.lightmapResolution()).isEqualTo(3);
        verify(graphicsConfigListener).configurationChanged(ConfigurationProperty.LIGHTMAP_RESOLUTION);
    }

    @Test
    void setUseAntialiasing_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setUseAntialiasing(true);

        assertThat(graphicsConfiguration.isUseAntialising()).isTrue();
        verify(graphicsConfigListener).configurationChanged(ConfigurationProperty.ANTIALIASING);
    }

    @Test
    void setResolution_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setResolution(640, 480);

        assertThat(graphicsConfiguration.resolution()).isEqualTo(Dimension.of(640, 480));
        verify(graphicsConfigListener).configurationChanged(ConfigurationProperty.RESOLUTION);
    }

    @Test
    void toggleFullscreen_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleFullscreen();

        assertThat(graphicsConfiguration.isFullscreen()).isTrue();

        graphicsConfiguration.toggleFullscreen();

        assertThat(graphicsConfiguration.isFullscreen()).isFalse();
        verify(graphicsConfigListener, times(2)).configurationChanged(ConfigurationProperty.WINDOW_MODE);
    }

    @Test
    void toggleAntialising_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleAntialising();

        assertThat(graphicsConfiguration.isUseAntialising()).isTrue();

        graphicsConfiguration.toggleAntialising();

        assertThat(graphicsConfiguration.isUseAntialising()).isFalse();
        verify(graphicsConfigListener, times(2)).configurationChanged(ConfigurationProperty.ANTIALIASING);
    }
}
