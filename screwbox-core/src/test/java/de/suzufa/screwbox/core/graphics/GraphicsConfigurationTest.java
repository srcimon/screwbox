package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
