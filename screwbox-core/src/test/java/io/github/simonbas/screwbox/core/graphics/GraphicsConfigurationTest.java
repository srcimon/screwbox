package io.github.simonbas.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.simonbas.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GraphicsConfigurationTest {

    GraphicsConfiguration graphicsConfiguration;

    @Mock
    GraphicsConfigurationListener graphicsConfigListener;

    @Captor
    ArgumentCaptor<GraphicsConfigurationEvent> event;

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
        verifyEventForProperty(LIGHTMAP_BLUR);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 7 })
    void setLightmapScale_outOfRange_throwsException(int resolution) {
        assertThatThrownBy(() -> graphicsConfiguration.setLightmapScale(resolution))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("valid range for lightmap scale is 1 to 6");
    }

    @Test
    void setLightmapScale_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setLightmapScale(3);

        assertThat(graphicsConfiguration.lightmapScale()).isEqualTo(3);
        verifyEventForProperty(LIGHTMAP_SCALE);
    }

    @Test
    void setUseAntialiasing_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setUseAntialiasing(true);

        assertThat(graphicsConfiguration.isUseAntialising()).isTrue();
        verifyEventForProperty(ANTIALIASING);
    }

    @Test
    void setResolution_resolutionSet_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.setResolution(640, 480);

        assertThat(graphicsConfiguration.resolution()).isEqualTo(Dimension.of(640, 480));
        verifyEventForProperty(RESOLUTION);
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
        verifyEventForProperty(WINDOW_MODE, 2);
    }

    @Test
    void toggleAntialising_updatesOptionAndNotifiesListeners() {
        graphicsConfiguration.toggleAntialising();

        assertThat(graphicsConfiguration.isUseAntialising()).isTrue();

        graphicsConfiguration.toggleAntialising();

        assertThat(graphicsConfiguration.isUseAntialising()).isFalse();
        verifyEventForProperty(ANTIALIASING, 2);
    }

    private void verifyEventForProperty(GraphicsConfigurationEvent.ConfigurationProperty property) {
        verifyEventForProperty(property, 1);
    }

    private void verifyEventForProperty(GraphicsConfigurationEvent.ConfigurationProperty property, int count) {
        verify(graphicsConfigListener, times(count)).configurationChanged(event.capture());
        assertThat(event.getValue().changedProperty()).isEqualTo(property);
    }
}
