package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ViewportManagerTest {

    @InjectMocks
    ViewportManager viewportManager;

    @Test
    void enableSplitScreen_oneScreen_setsSplitScreenEnabled() {
        viewportManager.enableSplitScreen(SplitScreenOptions.screenCount(2));

        assertThat(viewportManager.isSplitScreenEnabled()).isTrue();
    }

    @Test
    void disableSplitScreen_splitScreenEnabled_setsSplitScreenEnabledFalse() {
        viewportManager.enableSplitScreen(SplitScreenOptions.screenCount(2));

        viewportManager.disableSplitScreen();

        assertThat(viewportManager.isSplitScreenEnabled()).isFalse();
    }
}
