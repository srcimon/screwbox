package dev.screwbox.core.audio;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.utils.Validate;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

//TODO document
public record Voice(double baseFrequency,
                    double baseBlipDuration,//TODO optimize or validate type
                    double pitchRandomness,//TODO optimize or validate type
                    double speedRandomness,//TODO optimize or validate type
                    double vibratoSpeed,//TODO optimize or validate type
                    double vibratoDepth,//TODO optimize or validate type
                    double sentencePause,//TODO optimize or validate type
                    Duration commaPause,
                    Duration wordPause,
                    WaveType waveType,//TODO optimize or validate type
                    Percent vowelVolume,
                    Percent nasalVolume,
                    double lowPassCutoff,//TODO optimize or validate type
                    Long seed) {


    public enum WaveType {SINE, SQUARE, TRIANGLE}

    private Voice(final Long seed) {
        this(280.0, 0.04, 0.5, 0.15, 5.0, 0.025, 0.3, Duration.ofMillis(150), Duration.ofMillis(60), WaveType.SINE, Percent.of(0.75), Percent.of(0.35), 0.18, seed);
    }

    public Voice {
        Validate.range(baseFrequency, 10.0, 500.0, "base frequency must be between 10.0 and 500.0");
        requireNonNull(vowelVolume, "vowel volume must not be null");
        requireNonNull(nasalVolume, "nasal volume must not be null");
        requireNonNull(wordPause, "word pause must not be null");
        requireNonNull(commaPause, "comma pause must not be null");
    }

    public Voice vowelVolume(final Percent vowelVolume) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, lowPassCutoff, seed);
    }

    public Voice nasalVolume(final Percent nasalVolume) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, lowPassCutoff, seed);
    }

    public Voice baseFrequency(final double baseFrequency) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, lowPassCutoff, seed);
    }

    public Voice wordPause(final Duration wordPause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, lowPassCutoff, seed);
    }

    public Voice commaPause(final Duration commaPause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, lowPassCutoff, seed);
    }

    public static Voice variable() {
        return new Voice(null);
    }

    public static Voice fixed(final long seed) {
        return new Voice(seed);
    }

    public static Voice fixed() {
        return fixed(Time.now().nanos());
    }
}
