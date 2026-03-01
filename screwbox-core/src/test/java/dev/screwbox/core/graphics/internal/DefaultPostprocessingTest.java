package dev.screwbox.core.graphics.internal;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class DefaultPostprocessingTest {

    @InjectMocks
    DefaultPostProcessing postProcessing;

    @Test
    void isActive_noFilterAndShockwaves_isFalse() {
        assertThat(postProcessing.isActive()).isFalse();
    }
}
