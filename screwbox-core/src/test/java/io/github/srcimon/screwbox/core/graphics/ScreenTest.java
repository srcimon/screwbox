package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
