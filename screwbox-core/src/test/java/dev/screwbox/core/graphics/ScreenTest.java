package dev.screwbox.core.graphics;

import dev.screwbox.core.Rotation;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class ScreenTest {

    @Spy
    Screen screen;

    @Test
    void absoluteRotation_screenIsRotatedAndShaking_returnsSumOfBoth() {
        when(screen.shake()).thenReturn(Rotation.degrees(20));
        when(screen.rotation()).thenReturn(Rotation.degrees(100));

        assertThat(screen.absoluteRotation()).isEqualTo(Rotation.degrees(120));
    }
}
