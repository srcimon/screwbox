package dev.screwbox.core.audio;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VoiceTest {

    @Test
    void newInstance_allPropertiesConfigured_hasCorrectValues() {
        var voice = Voice.fixed(140)
            .baseFrequency(40.0)
            .baseBlipDuration(Duration.ofMillis(100))
            .commaPause(Duration.ofMillis(50))
            .wordPause(Duration.ofMillis(110))
            .sentencePause(Duration.ofMillis(210))
            .vibratoDepth(Percent.of(0.6))
            .vibratoSpeed(8.0)
            .nasalVolume(Percent.of(0.1))
            .vowelVolume(Percent.of(0.15))
            .waveType(Voice.WaveType.SINE)
            .pitchRandomness(Percent.of(0.6))
            .scratchyness(Percent.of(0.35))
            .speedRandomness(Percent.of(0.3));

        assertThat(voice.seed()).isEqualTo(140);
        assertThat(voice.baseFrequency()).isEqualTo(40.0);
        assertThat(voice.baseBlipDuration()).isEqualTo(Duration.ofMillis(100));
        assertThat(voice.commaPause()).isEqualTo(Duration.ofMillis(50));
        assertThat(voice.wordPause()).isEqualTo(Duration.ofMillis(110));
        assertThat(voice.sentencePause()).isEqualTo(Duration.ofMillis(210));
        assertThat(voice.vibratoDepth()).isEqualTo(Percent.of(0.6));
        assertThat(voice.vibratoSpeed()).isEqualTo(8.0);
        assertThat(voice.nasalVolume()).isEqualTo(Percent.of(0.1));
        assertThat(voice.vowelVolume()).isEqualTo(Percent.of(0.15));
        assertThat(voice.scratchyness()).isEqualTo(Percent.of(0.35));
        assertThat(voice.waveType()).isEqualTo(Voice.WaveType.SINE);
        assertThat(voice.pitchRandomness()).isEqualTo(Percent.of(0.6));
        assertThat(voice.speedRandomness()).isEqualTo(Percent.of(0.3));
    }

    @Test
    void newInstance_baseFrequencyOutOfRange_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.baseFrequency(700))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("base frequency must be between 10.0 and 500.0 (actual value: 700.0)");
    }

    @Test
    void newInstance_vibratoSpeedOutOfRange_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.vibratoSpeed(12))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("vibrato speed must be between 0.5 and 10.0 (actual value: 12.0)");
    }


    @Test
    void newInstance_baseBlipDurationIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.baseBlipDuration(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("base blip duration must not be null");
    }

    @Test
    void newInstance_vowelVolumeIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.vowelVolume(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("vowel volume must not be null");
    }

    @Test
    void newInstance_nasalVolumeIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.nasalVolume(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("nasal volume must not be null");
    }

    @Test
    void newInstance_wordPauseIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.wordPause(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("word pause must not be null");
    }

    @Test
    void newInstance_commaPauseIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.commaPause(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("comma pause must not be null");
    }

    @Test
    void newInstance_pitchRandomnessIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.pitchRandomness(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("pitch randomness must not be null");
    }

    @Test
    void newInstance_speedRandomnessIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.speedRandomness(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("speed randomness must not be null");
    }

    @Test
    void newInstance_scratchynessIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.scratchyness(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("scratchyness must not be null");
    }

    @Test
    void newInstance_waveTypeIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.waveType(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("wave type must not be null");
    }


    @Test
    void newInstance_vibratoDepthIsNull_throwsException() {
        var voice = Voice.fixed(140);

        assertThatThrownBy(() -> voice.vibratoDepth(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("vibrato depth must not be null");
    }
}
