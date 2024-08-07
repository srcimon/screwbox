package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoundSystemTest {

    @Test
    void update_entityWithoutPlayFound_startsAudioPlayback(DefaultEnvironment environment, Audio audio) {
        environment
                .addEntity(new TransformComponent($(30, 20)), new SoundComponent(SoundBundle.NOTIFY))
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio).playSound(SoundBundle.NOTIFY.get(), SoundOptions.playContinuously().position($(30, 20)));
    }

    @Test
    void update_entityWithActivePlayingSound_updatesPositionOfSound(DefaultEnvironment environment, Audio audio) {
        SoundComponent soundComponent = new SoundComponent(SoundBundle.NOTIFY);
        soundComponent.playback = new Playback(UUID.randomUUID(), SoundBundle.PHASER.get(), SoundOptions.playContinuously());

        when(audio.isActive(soundComponent.playback)).thenReturn(true);
        environment
                .addEntity(new TransformComponent($(30, 20)), soundComponent)
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio).updatePlaybackOptions(soundComponent.playback, SoundOptions.playContinuously().position($(30, 20)));
    }
}
