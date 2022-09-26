package de.suzufa.screwbox.core.audio.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.audio.Sound;

@ExtendWith(MockitoExtension.class)
class DefaultAudioTest {

    @InjectMocks
    DefaultAudio audio;

    @Mock
    AudioAdapter audioAdapter;

    @Mock
    Clip clip;

    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        audio = new DefaultAudio(executor, audioAdapter);
    }

    @Test
    void playEffect_effectVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.setEffectVolume(Percentage.min());
        audio.playEffect(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any(), any());
    }

    @Test
    void playEffectLooped_effectVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.muteEffects();
        audio.playEffectLooped(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any(), any());
    }

    @Test
    void playMusic_musicVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.muteMusic();
        audio.playMusic(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any(), any());
    }

    @Test
    void activeCount_noneActive_isZero() {
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void activeCount_noInstanceActive_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(audio.activeCount(sound)).isZero();
    }

    @Test
    void activeCount_onlyAnotherSoundIsActive_isZero() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percentage.max())).thenReturn(clip);

        audio.playEffect(sound);

        Sound secondSound = Sound.fromFile("kill.wav");

        assertThat(audio.activeCount(secondSound)).isZero();
    }

    @Test
    void playEffect_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percentage.max())).thenReturn(clip);

        audio.playEffect(sound);

        awaitShutdown();

        verify(clip).start();

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void playEffectLooped_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percentage.max())).thenReturn(clip);

        audio.playEffectLooped(sound);

        awaitShutdown();

        verify(clip).loop(Integer.MAX_VALUE);

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void activeCount_oneInstanceStartedAndStopped_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        when(audioAdapter.createClip(sound, Percentage.max())).thenReturn(clip);

        audio.playEffect(sound);

        awaitShutdown();

        audio.update(stopEventFor(clip));

        assertThat(audio.activeCount(sound)).isZero();
    }

    @Test
    void stopAllSounds_clipIsActive_clipIsStopped() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percentage.max())).thenReturn(clip);
        audio.playMusic(sound);

        audio.stopAllSounds();

        awaitShutdown();

        verify(clip).stop();
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void setEffectVolume_setsEffectVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percentage.half())).thenReturn(clip);

        audio.setEffectVolume(Percentage.half());
        audio.playEffect(sound);

        awaitShutdown();

        assertThat(audio.effectVolume()).isEqualTo(Percentage.half());
    }

    @Test
    void stop_instanceActive_stopsInstance() {
        Sound sound = Sound.fromFile("kill.wav");

        when(audioAdapter.createClip(sound, Percentage.max())).thenReturn(clip);

        audio.playEffect(sound);
        while (audio.activeCount() == 0) {
            // wait for sound to be started
        }
        audio.stop(sound);

        awaitShutdown();

        verify(clip).stop();
    }

    @Test
    void setMusicVolume_setsMusicVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percentage.of(0.7))).thenReturn(clip);

        audio.setMusicVolume(Percentage.of(0.7));
        audio.playMusic(sound);

        awaitShutdown();

        assertThat(audio.musicVolume()).isEqualTo(Percentage.of(0.7));
    }

    private LineEvent stopEventFor(Clip clipMock) {
        LineEvent stopEventMock = new LineEvent(mock(Line.class), Type.STOP, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getSource() {
                return clipMock;
            };
        };
        return stopEventMock;
    }

    @AfterEach
    void afterEach() {
        awaitShutdown();
    }

    private void awaitShutdown() {
        try {
            audio.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}