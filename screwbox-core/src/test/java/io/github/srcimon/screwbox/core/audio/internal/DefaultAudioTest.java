package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Percent.zero;
import static io.github.srcimon.screwbox.core.audio.SoundOptions.playLooped;
import static io.github.srcimon.screwbox.core.audio.SoundOptions.playOnce;
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

    @Mock
    Graphics graphics;

    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        audio = new DefaultAudio(executor, audioAdapter, graphics);
    }

    @Test
    void changeEffectsVolume_afterEffectWasPlayed_changesEffectVolume() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playSound(sound, playOnce().volume(Percent.quater()));

        awaitShutdown();

        audio.configuration().setEffectVolume(Percent.half());

        verify(audioAdapter).setVolume(clip, Percent.quater());
        verify(audioAdapter).setVolume(clip, Percent.of(0.125));
    }

    @Test
    void changeMusicVolume_afterMusicWasPlayed_changesMusicVolume() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);


        audio.playSound(sound, playOnce().asMusic());

        awaitShutdown();

        audio.configuration().setMusicVolume(Percent.half());

        verify(audioAdapter).setVolume(clip, Percent.max());
        verify(audioAdapter).setVolume(clip, Percent.half());
    }

    @Test
    void playSound_effectVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.dummyEffect();

        audio.configuration().setEffectVolume(zero());
        audio.playSound(sound);

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playSound_effectLoopedButVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.dummyEffect();

        audio.configuration().muteEffects();
        audio.playSound(sound, playLooped());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playSound_musicVolumeZero_doesntPlayMusic() {
        Sound sound = Sound.dummyEffect();

        audio.configuration().muteMusic();
        audio.playSound(sound, playOnce().asMusic());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playSound_musicLoopedVolumeZero_doesntPlayEffect() {
        Sound sound = Sound.dummyEffect();

        audio.configuration().muteMusic();
        audio.playSound(sound, playOnce().asMusic());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void isActive_noInstanceActive_isFalse() {
        Sound sound = Sound.dummyEffect();

        assertThat(audio.isActive(sound)).isFalse();
    }

    @Test
    void isActive_twoInstanceActive_isTrue() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playSound(sound, playOnce().asMusic());
        audio.playSound(sound, playOnce().asMusic());

        awaitShutdown();

        assertThat(audio.isActive(sound)).isTrue();
    }

    @Test
    void activeCount_noneActive_isZero() {
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void activeCount_noInstanceActive_isZero() {
        Sound sound = Sound.dummyEffect();

        assertThat(audio.activeCount(sound)).isZero();
    }

    @Test
    void activeCount_onlyAnotherSoundIsActive_isZero() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playSound(sound);

        Sound secondSound = Sound.dummyEffect();

        assertThat(audio.activeCount(secondSound)).isZero();
    }

    @Test
    void playSound_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playSound(sound);

        awaitShutdown();

        verify(clip).loop(0);
        verify(audioAdapter).setBalance(clip, 0);
        verify(audioAdapter).setPan(clip, 0);
        verify(audioAdapter).setVolume(clip, Percent.max());

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void playSound_looped_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.playSound(sound, playLooped().pan(-0.2).balance(0.1));

        awaitShutdown();

        verify(clip).loop(Integer.MAX_VALUE - 1);
        verify(audioAdapter).setBalance(clip, 0.1);
        verify(audioAdapter).setPan(clip, -0.2);
        verify(audioAdapter).setVolume(clip, Percent.max());

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void stopAllSounds_clipIsActive_clipIsStopped() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);
        audio.playSound(sound, playOnce().asMusic());

        audio.stopAllSounds();

        awaitShutdown();

        verify(clip).stop();
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void playSound_volumeHalfAndSoundPlayedAt20Percent_playsEffectOnTenPercent() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.configuration().setEffectVolume(Percent.half());
        audio.playSound(sound, playOnce().volume(Percent.of(0.2)));

        awaitShutdown();

        verify(audioAdapter).setVolume(clip, Percent.of(0.1));
    }

    @Test
    void playSound_volumeSeventyPercent_playsMusicAtSeventyPercent() {
        Sound sound = Sound.dummyEffect();
        when(audioAdapter.createClip(sound)).thenReturn(clip);

        audio.configuration().setMusicVolume(Percent.of(0.7));
        audio.playSound(sound, playOnce().asMusic());

        awaitShutdown();

        verify(audioAdapter).setVolume(clip, Percent.of(0.7));
    }

    @Test
    void playSound_effectsMuted_doesntPlayAnySound() {
        audio.configuration().muteEffects();

        audio.playSound(Sound.dummyEffect());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
    }

    @Test
    void playSound_musicMuted_doesntPlayAnySound() {
        audio.configuration().muteMusic();

        audio.playSound(Sound.dummyEffect(), playOnce().asMusic());

        awaitShutdown();

        verify(audioAdapter, never()).createClip(any());
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