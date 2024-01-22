package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import java.io.Serial;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Percent.max;
import static io.github.srcimon.screwbox.core.Percent.zero;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Timeout(1)
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

        audio.setEffectVolume(zero());
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
    void playMusicLooped_musicVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.muteMusic();
        audio.playMusicLooped(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any(), any());
    }

    @Test
    void isActive_noInstanceActive_isFalse() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(audio.isActive(sound)).isFalse();
    }

    @Test
    void isActive_twoInstanceActive_isTrue() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, max())).thenReturn(clip);

        audio.playMusic(sound);
        audio.playMusic(sound);

        awaitShutdown();

        assertThat(audio.isActive(sound)).isTrue();
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
        when(audioAdapter.createClip(sound, max())).thenReturn(clip);

        audio.playEffect(sound);

        Sound secondSound = Sound.fromFile("kill.wav");

        assertThat(audio.activeCount(secondSound)).isZero();
    }

    @Test
    void playEffect_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, max())).thenReturn(clip);

        audio.playEffect(sound);

        awaitShutdown();

        verify(clip).start();

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void playEffectLooped_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, max())).thenReturn(clip);

        audio.playEffectLooped(sound);

        awaitShutdown();

        verify(clip).loop(Integer.MAX_VALUE);

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void activeCount_oneInstanceStartedAndStopped_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        when(audioAdapter.createClip(sound, max())).thenReturn(clip);

        audio.playEffect(sound);

        awaitShutdown();

        audio.update(stopEventFor(clip));

        assertThat(audio.activeCount(sound)).isZero();
    }


    @Test
    void stopAllSounds_clipIsActive_clipIsStopped() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, max())).thenReturn(clip);
        audio.playMusic(sound);

        audio.stopAllSounds();

        awaitShutdown();

        verify(clip).stop();
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void setEffectVolume_setsEffectVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percent.half())).thenReturn(clip);

        audio.setEffectVolume(Percent.half());
        audio.playEffect(sound);

        awaitShutdown();

        assertThat(audio.effectVolume()).isEqualTo(Percent.half());
    }

    @Test
    void setMusicVolume_setsMusicVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound, Percent.of(0.7))).thenReturn(clip);

        audio.setMusicVolume(Percent.of(0.7));
        audio.playMusic(sound);

        awaitShutdown();

        assertThat(audio.musicVolume()).isEqualTo(Percent.of(0.7));
    }

    @Test
    void muteMusic_setsMusicVoulmeToZero() {
        audio.muteMusic();

        assertThat(audio.musicVolume()).isEqualTo(zero());
        assertThat(audio.effectVolume()).isEqualTo(max());
    }

    @Test
    void muteEffects_setsEffectsVoulmeToZero() {
        audio.muteEffects();

        assertThat(audio.musicVolume()).isEqualTo(max());
        assertThat(audio.effectVolume()).isEqualTo(zero());
    }

    @Test
    void mute_setsEffectsAndMusicVolumeToZero() {
        audio.mute();

        assertThat(audio.musicVolume()).isEqualTo(zero());
        assertThat(audio.effectVolume()).isEqualTo(zero());
    }

    private LineEvent stopEventFor(Clip clipMock) {
        return new LineEvent(mock(Line.class), Type.STOP, 0) {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Object getSource() {
                return clipMock;
            }
        };
    }

    @AfterEach
    void afterEach() {
        awaitShutdown();
    }

    private void awaitShutdown() {
        audio.shutdown();
        TestUtil.shutdown(executor);
    }
}