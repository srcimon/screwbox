package dev.screwbox.core.audio;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.utils.Validate;

import static java.util.Objects.requireNonNull;

//TODO document below
public record Voice(double baseFrequency,
                    Duration baseBlipDuration,
                    Percent pitchRandomness,
                    Percent speedRandomness,
                    double vibratoSpeed,
                    Percent vibratoDepth,
                    Duration sentencePause,
                    Duration commaPause,
                    Duration wordPause,
                    WaveType waveType,
                    Percent vowelVolume,
                    Percent nasalVolume,
                    Percent scratchyness,
                    Long seed) {


    public enum WaveType {
        SINE,
        SQUARE,
        TRIANGLE
    }

    private Voice(final Long seed) {
        this(280.0, Duration.ofMillis(40), Percent.of(0.5), Percent.of(0.15), 5.0, Percent.of(0.025), Duration.ofMillis(300), Duration.ofMillis(150), Duration.ofMillis(60), WaveType.SINE, Percent.of(0.75), Percent.of(0.35), Percent.of(0.18), seed);
    }

    public Voice {
        //TODO test validations below
        Validate.range(baseFrequency, 10.0, 500.0, "base frequency must be between 10.0 and 500.0");
        Validate.range(vibratoSpeed, 0.5, 10.0, "vibrato speed must be between 0.5 and 10.0");
        requireNonNull(vowelVolume, "vowel volume must not be null");
        requireNonNull(nasalVolume, "nasal volume must not be null");
        requireNonNull(wordPause, "word pause must not be null");
        requireNonNull(commaPause, "comma pause must not be null");
        requireNonNull(pitchRandomness, "pitch randomness must not be null");
        requireNonNull(speedRandomness, "speed randomness must not be null");
        requireNonNull(scratchyness, "scratchyness must not be null");
        requireNonNull(waveType, "wave type must not be null");
        requireNonNull(vibratoDepth, "vibrato depth must not be null");
    }

    //TODO check for missing methods
    public Voice waveType(final WaveType waveType) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice vibratoSpeed(final double vibratoSpeed) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice vibratoDepth(final Percent vibratoDepth) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }
    public Voice vowelVolume(final Percent vowelVolume) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice speedRandomness(final Percent speedRandomness) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice pitchRandomness(final Percent pitchRandomness) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice scratchyness(final Percent scratchyness) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice nasalVolume(final Percent nasalVolume) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice baseFrequency(final double baseFrequency) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice wordPause(final Duration wordPause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice commaPause(final Duration commaPause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice sentencePause(final Duration sentencePause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    public Voice baseBlipDuration(final Duration baseBlipDuration) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
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
