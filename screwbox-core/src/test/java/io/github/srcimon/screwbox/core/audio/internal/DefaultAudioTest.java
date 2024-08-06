package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultAudioTest {

    DefaultAudio audio;

    @Mock
    MicrophoneMonitor microphoneMonitor;

    @Mock
    DynamicSoundSupport dynamicSoundSupport;

    @Mock
    AudioLinePool audioLinePool;

    ExecutorService executor;

    Sound sound;

    @BeforeEach
    void setUp() {
        AudioConfiguration configuration = new AudioConfiguration();
        executor = Executors.newSingleThreadExecutor();
        audio = new DefaultAudio(executor, configuration, dynamicSoundSupport, microphoneMonitor, audioLinePool);
        sound = Sound.fromFile("assets/sounds/PHASER.wav");
    }

    @Test
    void playSound_soundIsNull_throwsException() {
        assertThatThrownBy(() -> audio.playSound((Sound) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sound must not be null");
    }

    @Test
    void playSound_optionsIsNull_throwsException() {
        assertThatThrownBy(() -> audio.playSound(sound, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("options must not be null");
    }

    @Test
    void stopPlayback_playbackIsNull_throwsException() {
        assertThatThrownBy(() -> audio.stopPlayback(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("playback must not be null");
    }

    @Test
    void availableAudioLines_returnsLineCountFromPool() {
        when(audioLinePool.size()).thenReturn(4);

        assertThat(audio.availableAudioLines()).isEqualTo(4);
    }

    @Test
    void updatePlaybackOptions_playbackIsNull_throwsException() {
        assertThatThrownBy(() -> audio.updatePlaybackOptions(null, SoundOptions.playOnce()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("playback must not be null");
    }

    @Test
    void updatePlaybackOptions_optionsIsNull_throwsException() {
        var playback = new Playback(UUID.randomUUID(), null, null);
        assertThatThrownBy(() -> audio.updatePlaybackOptions(playback, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("options must not be null");
    }
//
//    @Test
//    void playSound_positionInRange_appliesPanAndVolume() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//        audio.configuration().setSoundRange(1024);
//
//        when(camera.position()).thenReturn($(-129, 239));
//
//        audio.playSound(sound, $(30, 10));
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter).setVolume(clip, Percent.of(0.7277474054857758));
//        verify(audioAdapter).setPan(clip, 0.2722525945142242d);
//    }
//
//
//    @Test
//    void playSound_positionInRange_addsPlaybackWithPosition() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//        audio.configuration().setSoundRange(1024);
//
//        when(camera.position()).thenReturn($(0, 0));
//
//        audio.playSound(sound, $(80, 10));
//
//        await(() -> audio.activeCount() == 1, ofMillis(500));
//
//        assertThat(audio.activePlaybacks()).hasSize(1);
//
//        Playback playback = audio.activePlaybacks().getFirst();
//        assertThat(playback.position()).contains($(80, 10));
//        assertThat(playback.options().times()).isEqualTo(1);
//        assertThat(playback.options().balance()).isZero();
//    }
//
//    @Test
//    void playSound_positionOutOfRange_doesntPlaySound() {
//        audio.configuration().setSoundRange(1024);
//
//        when(camera.position()).thenReturn($(-129, 239));
//
//        audio.playSound(sound, $(3000, 10));
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(sound);
//    }
//
//    @Test
//    void changeEffectsVolume_afterEffectWasPlayed_changesEffectVolume() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.playSound(sound, playOnce().volume(Percent.quater()));
//
//        TestUtil.shutdown(executor);
//
//        audio.configuration().setEffectVolume(Percent.half());
//
//        verify(audioAdapter).setVolume(clip, Percent.quater());
//        verify(audioAdapter).setVolume(clip, Percent.of(0.125));
//    }
//
//    @Test
//    void changeMusicVolume_afterMusicWasPlayed_changesMusicVolume() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.playSound(sound, playOnce().asMusic());
//
//        TestUtil.shutdown(executor);
//
//        audio.configuration().setMusicVolume(Percent.half());
//
//        verify(audioAdapter).setVolume(clip, Percent.max());
//        verify(audioAdapter).setVolume(clip, Percent.half());
//    }
//
//    @Test
//    void playSound_effectVolumeZero_doesntPlayEffect() {
//        audio.configuration().setEffectVolume(zero());
//        audio.playSound(sound);
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(any());
//    }
//
//    @Test
//    void playSound_effectLoopedButVolumeZero_doesntPlayEffect() {
//        audio.configuration().muteEffects();
//        audio.playSound(sound, playContinuously());
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(any());
//    }
//
//    @Test
//    void playSound_musicVolumeZero_doesntPlayMusic() {
//        audio.configuration().muteMusic();
//        audio.playSound(sound, playOnce().asMusic());
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(any());
//    }
//
//    @Test
//    void playSound_musicLoopedVolumeZero_doesntPlayEffect() {
//        audio.configuration().muteMusic();
//        audio.playSound(sound, playContinuously().asMusic());
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(any());
//    }
//
//    @Test
//    void isActive_noInstanceActive_isFalse() {
//        assertThat(audio.isAlive(sound)).isFalse();
//    }
//
//    @Test
//    void isActive_twoInstanceActive_isTrue() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.playSound(sound, playOnce().asMusic());
//        audio.playSound(sound, playOnce().asMusic());
//
//        TestUtil.shutdown(executor);
//
//        assertThat(audio.isAlive(sound)).isTrue();
//    }
//
//    @Test
//    void activeCount_noneActive_isZero() {
//        assertThat(audio.activeCount()).isZero();
//    }
//
//    @Test
//    void activeCount_noInstanceActive_isZero() {
//        assertThat(audio.activeCount(sound)).isZero();
//    }
//
//    @Test
//    void activeCount_onlyAnotherSoundIsActive_isZero() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.playSound(sound);
//
//        Sound secondSound = Sound.fromFile("assets/sounds/PHASER.wav");
//
//        assertThat(audio.activeCount(secondSound)).isZero();
//    }
//
//    @Test
//    void playSound_invokesMethodsOnClipAndIncreasesActiveCount() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.playSound(sound);
//
//        TestUtil.shutdown(executor);
//
//        verify(clip).loop(0);
//        verify(audioAdapter).setBalance(clip, 0);
//        verify(audioAdapter).setPan(clip, 0);
//        verify(audioAdapter).setVolume(clip, Percent.max());
//
//        assertThat(audio.activeCount(sound)).isEqualTo(1);
//    }
//
//    @Test
//    void playSound_looped_invokesMethodsOnClipAndIncreasesActiveCount() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.playSound(sound, playContinuously().pan(-0.2).balance(0.1));
//
//        TestUtil.shutdown(executor);
//
//        verify(clip).loop(Integer.MAX_VALUE - 1);
//        verify(audioAdapter).setBalance(clip, 0.1);
//        verify(audioAdapter).setPan(clip, -0.2);
//        verify(audioAdapter).setVolume(clip, Percent.max());
//
//        assertThat(audio.activeCount(sound)).isEqualTo(1);
//    }
//
//    @Test
//    void stopAllSounds_clipIsActive_clipIsStopped() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//        audio.playSound(sound, playOnce().asMusic());
//
//        audio.stopAllSounds();
//
//        TestUtil.shutdown(executor);
//
//        verify(clip).stop();
//        assertThat(audio.activeCount()).isZero();
//    }
//
//    @Test
//    void playSound_volumeHalfAndSoundPlayedAt20Percent_playsEffectOnTenPercent() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.configuration().setEffectVolume(Percent.half());
//        audio.playSound(sound, playOnce().volume(Percent.of(0.2)));
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter).setVolume(clip, Percent.of(0.1));
//    }
//
//    @Test
//    void playSound_volumeSeventyPercent_playsMusicAtSeventyPercent() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//
//        audio.configuration().setMusicVolume(Percent.of(0.7));
//        audio.playSound(sound, playOnce().asMusic());
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter).setVolume(clip, Percent.of(0.7));
//    }
//
//    @Test
//    void playSound_effectsMuted_doesntPlayAnySound() {
//        audio.configuration().muteEffects();
//
//        audio.playSound(Sound.fromFile("assets/sounds/PHASER.wav"));
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(any());
//    }
//
//    @Test
//    void playSound_musicMuted_doesntPlayAnySound() {
//        audio.configuration().muteMusic();
//
//        audio.playSound(Sound.fromFile("assets/sounds/PHASER.wav"), playOnce().asMusic());
//
//        TestUtil.shutdown(executor);
//
//        verify(audioAdapter, never()).createClip(any());
//    }
//
//    @Test
//    void stopSound_soundIsPlaying_stopsSound() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//        audio.playSound(sound);
//
//        await(() -> audio.isAlive(sound), ofMillis(500));
//
//        audio.stopSound(sound);
//
//        TestUtil.shutdown(executor);
//        verify(clip).stop();
//    }
//
//    @Test
//    void activePlaybacks_noPlaybacks_isEmpty() {
//        assertThat(audio.activePlaybacks()).isEmpty();
//    }
//
//    @Test
//    void activePlaybacks_onePlayback_containsPlayback() {
//        when(audioAdapter.createClip(sound)).thenReturn(clip);
//        audio.playSound(sound, playOnce().asMusic());
//
//        await(() -> audio.activeCount() == 1, ofMillis(750));
//
//        assertThat(audio.activePlaybacks()).hasSize(1)
//                .anyMatch(playback -> playback.sound().equals(sound))
//                .anyMatch(playback -> playback.options().equals(playOnce().asMusic()));
//    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }

}