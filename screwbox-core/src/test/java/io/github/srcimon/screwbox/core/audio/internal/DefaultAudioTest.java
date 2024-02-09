package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import java.io.Serial;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Percent.zero;
import static io.github.srcimon.screwbox.core.audio.PlaybackOptions.playLooped;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultAudioTest {

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
    void changeEffectsVolume_afterEffectWasPlayed_changesEffectVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);


        audio.playEffect(sound);

        awaitShutdown();

        audio.configuration().setEffectVolume(Percent.half());

        verify(audioAdapter).setVolume(clip, Percent.max());
        verify(audioAdapter).setVolume(clip, Percent.half());
    }

    @Test
    void changeMusicVolume_afterMusicWasPlayed_changesMusicVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);


        audio.playMusic(sound);

        awaitShutdown();

        audio.configuration().setMusicVolume(Percent.half());

        verify(audioAdapter).setVolume(clip, Percent.max());
        verify(audioAdapter).setVolume(clip, Percent.half());
    }

    @Test
    void playEffect_effectVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.configuration().setEffectVolume(zero());
        audio.playEffect(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playEffectLooped_effectVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.configuration().muteEffects();
        audio.playEffect(sound, playLooped());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playMusic_musicVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.configuration().muteMusic();
        audio.playMusic(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playMusicLooped_musicVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.fromFile("kill.wav");

        audio.configuration().muteMusic();
        audio.playMusic(sound, playLooped());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void isActive_noInstanceActive_isFalse() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(audio.isActive(sound)).isFalse();
    }

    @Test
    void isActive_twoInstanceActive_isTrue() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);

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
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playEffect(sound);

        Sound secondSound = Sound.fromFile("kill.wav");

        assertThat(audio.activeCount(secondSound)).isZero();
    }

    @Test
    void playEffect_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playEffect(sound);

        awaitShutdown();

        verify(clip).loop(0);

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void playEffect_looped_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playEffect(sound, playLooped());

        awaitShutdown();

        verify(clip).loop(Integer.MAX_VALUE - 1);

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void activeCount_oneInstanceStartedAndStopped_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playEffect(sound);

        awaitShutdown();

        audio.update(stopEventFor(clip));

        assertThat(audio.activeCount(sound)).isZero();
    }


    @Test
    void stopAllSounds_clipIsActive_clipIsStopped() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);
        audio.playMusic(sound);

        audio.stopAllSounds();

        awaitShutdown();

        verify(clip).stop();
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void playEffect_volumeHalf_playsEffectOnHalfVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.configuration().setEffectVolume(Percent.half());
        audio.playEffect(sound);

        awaitShutdown();

        verify(audioAdapter).setVolume(clip, Percent.half());
    }

    @Test
    void playMusic_volumeSeventyPercent_playsMusicAtSeventyPercent() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.configuration().setMusicVolume(Percent.of(0.7));
        audio.playMusic(sound);

        awaitShutdown();

        verify(audioAdapter).setVolume(clip, Percent.of(0.7));
    }

    @Test
    void playEffect_effectsMuted_doesntPlayAnySound() {
        audio.configuration().muteEffects();

        audio.playEffect(Sound.dummyEffect());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playMusicmusicMuted_doesntPlayAnySound() {
        audio.configuration().muteMusic();

        audio.playMusic(Sound.dummyEffect());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
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