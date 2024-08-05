package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DynamicSoundSupportTest {

    @Mock
    Camera camera;

    @Mock
    AudioConfiguration configuration;

    @InjectMocks
    DynamicSoundSupport dynamicSoundSupport;

    @Test
    void xxx() {
        var refreshedOptions = dynamicSoundSupport.refreshOptions(SoundOptions.playOnce());
        //TODO Implement
    }
}
