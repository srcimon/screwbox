package dev.screwbox.core.environment.audio;

import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.Audio;
import dev.screwbox.core.audio.AudioConfiguration;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoundSystemTest {

    public static final Playback PLAYBACK = new Playback(UUID.randomUUID(), SoundBundle.PHASER.get(), SoundOptions.playContinuously());

    @BeforeEach
    void setUp(Camera camera) {
        when(camera.position()).thenReturn(Vector.zero());
    }

    @Test
    void update_entityInRangeWithoutPlayback_startsAudioPlayback(DefaultEnvironment environment, Audio audio, AudioConfiguration config, Graphics graphics) {
        when(config.soundRange()).thenReturn(400.0);
        when(graphics.isWithinDistanceToVisibleArea($(30, 20), 800)).thenReturn(true);

        environment
                .addEntity(new TransformComponent($(30, 20)), new SoundComponent(SoundBundle.NOTIFY))
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio).playSound(SoundBundle.NOTIFY.get(), SoundOptions.playOnce().position($(30, 20)));
    }

    @Test
    void update_entityInRangeWithActivePlayback_updatesPositionOfSound(DefaultEnvironment environment, Audio audio, AudioConfiguration config, Graphics graphics) {
        when(config.soundRange()).thenReturn(400.0);
        when(graphics.isWithinDistanceToVisibleArea($(30, 20), 800)).thenReturn(true);
        SoundComponent soundComponent = new SoundComponent(SoundBundle.NOTIFY);
        soundComponent.playback = PLAYBACK;

        when(audio.playbackIsActive(soundComponent.playback)).thenReturn(true);
        environment
                .addEntity(new TransformComponent($(30, 20)), soundComponent)
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio).updatePlaybackOptions(soundComponent.playback, SoundOptions.playOnce().position($(30, 20)));
    }

    @Test
    void update_entityInRangeWithInactivePlayback_startsAudioPlayback(DefaultEnvironment environment, Audio audio, AudioConfiguration config, Graphics graphics) {
        when(config.soundRange()).thenReturn(400.0);
        when(graphics.isWithinDistanceToVisibleArea($(30, 20), 800)).thenReturn(true);
        SoundComponent soundComponent = new SoundComponent(SoundBundle.NOTIFY);
        soundComponent.playback = PLAYBACK;

        environment
                .addEntity(new TransformComponent($(30, 20)), soundComponent)
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio).playSound(SoundBundle.NOTIFY.get(), SoundOptions.playOnce().position($(30, 20)));
    }

    @Test
    void update_entityOutOfRangeWithoutPlayback_doesNothing(DefaultEnvironment environment, Audio audio, AudioConfiguration config, Graphics graphics) {
        when(config.soundRange()).thenReturn(4.0);
        when(graphics.isWithinDistanceToVisibleArea($(30, 20), 2048)).thenReturn(true);

        SoundComponent soundComponent = new SoundComponent(SoundBundle.NOTIFY);

        environment
                .addEntity(new TransformComponent($(30, 20)), soundComponent)
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio, never()).updatePlaybackOptions(any(), any());
        verify(audio, never()).stopPlayback(any());
        verify(audio, never()).playSound(any(Sound.class), any());
    }

    @Test
    void update_entityOutOfRangeWithPlayback_stopsPlaybackAndRemovesPlayback(DefaultEnvironment environment, Audio audio, AudioConfiguration config, Graphics graphics) {
        when(config.soundRange()).thenReturn(4.0);
        when(graphics.isWithinDistanceToVisibleArea($(30, 20), 2048)).thenReturn(false);

        SoundComponent soundComponent = new SoundComponent(SoundBundle.NOTIFY);
        soundComponent.playback = PLAYBACK;

        environment
                .addEntity(new TransformComponent($(30, 20)), soundComponent)
                .addSystem(new SoundSystem());

        environment.update();

        verify(audio).stopPlayback(PLAYBACK);
        assertThat(soundComponent.playback).isNull();
    }
}
