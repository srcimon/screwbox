package dev.screwbox.core.audio.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.audio.AudioConfiguration;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.sound.sampled.SourceDataLine;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(1)
@MockitoSettings
class DefaultAudioTest {

    private static final Sound SOUND = Sound.fromFile("assets/sounds/PHASER.wav");

    @InjectMocks
    DefaultAudio audio;

    @Mock
    MicrophoneMonitor microphoneMonitor;

    @Mock
    DynamicSoundSupport dynamicSoundSupport;

    @Mock
    AudioLinePool audioLinePool;

    @Mock
    ExecutorService executor;

    @Spy
    AudioConfiguration configuration = new AudioConfiguration();

    @Test
    void playSound_soundIsNull_throwsException() {
        assertThatThrownBy(() -> audio.playSound((Sound) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sound must not be null");
    }

    @Test
    void playSound_optionsIsNull_throwsException() {
        assertThatThrownBy(() -> audio.playSound(SOUND, null))
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
    void lineCount_returnsLineCountFromPool() {
        when(audioLinePool.size()).thenReturn(4);

        assertThat(audio.lineCount()).isEqualTo(4);
    }

    @Test
    void updatePlaybackOptions_playbackIsNull_throwsException() {
        var options = SoundOptions.playOnce();
        assertThatThrownBy(() -> audio.updatePlaybackOptions(null, options))
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

    @Test
    void isPlaybackActive_playbackNull_throwsExceptionPlaybacks() {
        assertThatThrownBy(() -> audio.isPlaybackActive(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("playback must not be null");
    }

    @Test
    void isPlaybackActive_notActive_isFalse() {
        Playback playback = new Playback(UUID.randomUUID(), null, null);

        assertThat(audio.isPlaybackActive(playback)).isFalse();
    }

    @Test
    void playSound_soundNull_throwsException() {
        var options = SoundOptions.playContinuously();
        assertThatThrownBy(() -> audio.playSound((Sound) null, options))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sound must not be null");
    }

    @Test
    void playSound_optionsNull_throwsException() {
        assertThatThrownBy(() -> audio.playSound(SOUND, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("options must not be null");
    }

    @Test
    void activePlaybackCount_twoSoundsPlayed_isTwo() {
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());

        assertThat(audio.activePlaybackCount()).isEqualTo(2);
    }

    @Test
    void activePlaybacks_twoPlaybacks_returnsBoth() {
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());
        audio.playSound(SoundBundle.JUMP, SoundOptions.playTimes(2));

        assertThat(audio.activePlaybacks()).hasSize(2)
                .anyMatch(playback -> playback.options().equals(SoundOptions.playContinuously()))
                .anyMatch(playback -> playback.options().equals(SoundOptions.playTimes(2)))
                .allMatch(playback -> playback.sound().equals(SoundBundle.JUMP.get()));
    }

    @Test
    void activePlaybacksMatching_onePlaybackOfTwoMatches_containsMatching() {
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());
        audio.playSound(SoundBundle.SPLASH, SoundOptions.playTimes(2));

        final Predicate<Playback> condition = p -> p.sound().equals(SoundBundle.JUMP.get());
        assertThat(audio.activePlaybacksMatching(condition)).hasSize(1);
    }

    @Test
    void activePlaybacksMatching_noneMatches_isEmpty() {
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());
        audio.playSound(SoundBundle.JUMP, SoundOptions.playTimes(2));

        final Predicate<Playback> condition = p -> p.sound().equals(SoundBundle.SPLASH.get());
        assertThat(audio.activePlaybacksMatching(condition)).isEmpty();
    }

    @Test
    void hasActivePlaybacksMatching_onePlaybackOfTwoMatches_containsMatching() {
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());
        audio.playSound(SoundBundle.SPLASH, SoundOptions.playTimes(2));

        final Predicate<Playback> condition = p -> p.sound().equals(SoundBundle.JUMP.get());
        assertThat(audio.hasActivePlaybacksMatching(condition)).isTrue();
    }

    @Test
    void hasActivePlaybacksMatching_noneMatches_isEmpty() {
        audio.playSound(SoundBundle.JUMP, SoundOptions.playContinuously());
        audio.playSound(SoundBundle.JUMP, SoundOptions.playTimes(2));

        final Predicate<Playback> condition = p -> p.sound().equals(SoundBundle.SPLASH.get());
        assertThat(audio.hasActivePlaybacksMatching(condition)).isFalse();
    }

    @Test
    void updatePlaybackOptions_playbackIsNotActive_isFalse() {
        Playback playback = new Playback(UUID.randomUUID(), null, null);

        assertThat(audio.updatePlaybackOptions(playback, SoundOptions.playContinuously())).isFalse();
    }

    @Test
    void updatePlaybackOptions_activePlaybackHasDistinctSpeed_throwsException() {
        var playback = audio.playSound(SOUND, SoundOptions.playOnce().speed(2));
        var newOptions = SoundOptions.playContinuously();

        assertThatThrownBy(() -> audio.updatePlaybackOptions(playback, newOptions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot change speed of playback once it has started");
    }

    @Test
    void stopAllPlaybacks_noPlayback_noException() {
        assertThatNoException().isThrownBy(() -> audio.stopAllPlaybacks(SOUND));
    }

    @Test
    void stopAllPlaybacks_twoPlaybacks_usedLinesFlushedAndActivePlaybackCleared() {
        SourceDataLine line = mock(SourceDataLine.class);
        when(audioLinePool.lines()).thenReturn(List.of(line));
        audio.playSound(SOUND);

        audio.stopAllPlaybacks();

        verify(line).flush();
        assertThat(audio.activePlaybacks()).isEmpty();
    }

    @Test
    void microphoneLevel_levelHalf_returnsHalf() {
        when(microphoneMonitor.level()).thenReturn(Percent.half());

        assertThat(audio.microphoneLevel()).isEqualTo(Percent.half());
    }

    @Test
    void isMicrophoneActive_isActive_isTrue() {
        when(microphoneMonitor.isActive()).thenReturn(true);

        assertThat(audio.isMicrophoneActive()).isTrue();
    }

    @Test
    void completedPlaybackCount_noSoundPlayed_isZero() {
        assertThat(audio.completedPlaybackCount()).isZero();
    }

    @Test
    void soundsPlayedCount_noSoundPlayed_isZero() {
        assertThat(audio.soundsPlayedCount()).isZero();
    }

}