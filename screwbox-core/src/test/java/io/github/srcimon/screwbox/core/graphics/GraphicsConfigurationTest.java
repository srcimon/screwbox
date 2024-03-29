package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @ValueSource(ints = {-1, 7})
    void setLightmapBlur_outOfRange_throwsException(int blur) {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapBlur(blur))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("valid range for lightmap blur is 0 (no blur) to 6 (heavy blur)");
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

    @ParameterizedTest
    @ValueSource(ints = {0, 7})
    void setLightmapScale_outOfRange_throwsException(int resolution) {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapScale(resolution))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("valid range for lightmap scale is 1 to 6");
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

}
