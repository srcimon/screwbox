package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.SourceDataLine;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    void playbackIsActive_playbackNull_throwsExceptionPlaybacks() {
        assertThatThrownBy(() -> audio.playbackIsActive(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("playback must not be null");
    }

    @Test
    void playbackIsActive_notActive_isFalse() {
        Playback playback = new Playback(UUID.randomUUID(), null, null);

        assertThat(audio.playbackIsActive(playback)).isFalse();
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
        assertThatThrownBy(() -> audio.playSound(sound, null))
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
    void updatePlaybackOptions_playbackIsNotActive_isFalse() {
        Playback playback = new Playback(UUID.randomUUID(), null, null);

        assertThat(audio.updatePlaybackOptions(playback, SoundOptions.playContinuously())).isFalse();
    }

    @Test
    void stopAllPlaybacks_noPlayback_noException() {
        assertThatNoException().isThrownBy(() -> audio.stopAllPlaybacks(sound));
    }

    @Test
    void stopAllPlaybacks_twoPlaybacks_usedLinesFlushedAndActivePlaybackCleared() {
        SourceDataLine line = mock(SourceDataLine.class);
        when(audioLinePool.lines()).thenReturn(List.of(line));
        when(audioLinePool.aquireLine(any())).thenReturn(line);
        audio.playSound(sound);

        audio.stopAllPlaybacks();

        verify(line).flush();
        assertThat(audio.activePlaybacks()).isEmpty();
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }

}