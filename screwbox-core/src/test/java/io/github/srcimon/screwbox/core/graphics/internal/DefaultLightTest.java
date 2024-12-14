package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@MockitoSettings
class DefaultLightTest {

    @Mock
    GraphicsConfiguration configuration;

    @InjectMocks
    DefaultLight light;

    @Test
    void setAmbientLight_ambientLightNull_throwsException() {
        assertThatThrownBy(() -> light.setAmbientLight(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ambient light must not be null");
    }

    @Test
    void setAmbientLight_notNull_setsLight() {
        light.setAmbientLight(Percent.half());

        assertThat(light.ambientLight()).isEqualTo(Percent.half());
    }
}
