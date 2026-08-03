package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.internal.ViewportManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MockitoSettings
class DefaultSmokeTest {

    @InjectMocks
    DefaultSmoke smoke;

    @Mock
    ViewportManager viewportManager;

    @Mock
    ExecutorService executor;

    @Spy
    GraphicsConfiguration configuration = new GraphicsConfiguration(RenderingApi.OPEN_GL);

    @Test
    void emit_negativeAmount_throwsException() {
        Vector position = $(40, 10);
        assertThatThrownBy(() -> smoke.emit(position, -0.1, Color.RED))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("amount must be positive (actual value: -0.1)");
    }

    @Test
    void emit_autoTurnOnEnabled_activatesSmoke() {
        smoke.emit($(4, 1), 20, Color.WHITE);

        assertThat(configuration.isSmokeEnabled()).isTrue();
    }

    @Test
    void emit_autoTurnOnDisabled_leavesSmokeDisabled() {
        configuration.setAutoEnableSmoke(false);

        smoke.emit($(4, 1), 20, Color.WHITE);

        assertThat(configuration.isSmokeEnabled()).isFalse();
    }

    @Test
    void setOptions_optionsNull_throwsException() {
        assertThatThrownBy(() -> smoke.setOptions(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("options must not be null");
    }
}
