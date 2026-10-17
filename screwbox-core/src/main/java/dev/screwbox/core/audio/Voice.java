package dev.screwbox.core.audio;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.utils.Validate;

import static java.util.Objects.requireNonNull;

/**
 * Configures a voice used by {@link Synthesizer} and {@link Audio#playChatter(String, Voice)}.
 *
 * @param baseFrequency    The fundamental frequency of the voice in Hz.
 * @param baseBlipDuration The duration of an individual sound blip.
 * @param pitchRandomness  The amount of random variation applied to the pitch.
 * @param speedRandomness  The amount of random variation applied to the speech speed.
 * @param vibratoSpeed     The frequency or speed of the vibrato effect.
 * @param vibratoDepth     The intensity or depth of the vibrato effect.
 * @param sentencePause    The duration of the pause at the end of a sentence.
 * @param commaPause       The duration of the pause at a comma.
 * @param wordPause        The duration of the pause between words.
 * @param waveType         The type of sound wave used for synthesis.
 * @param vowelVolume      The volume level specifically for vowel sounds.
 * @param nasalVolume      The volume level specifically for nasal sounds.
 * @param scratchyness     The amount of allowed distortion or scratchiness in the voice. Minimum is 0.01 to avoid muting the sound.
 * @param seed             The random seed used for generating consistent voice characteristics.
 * @since 3.37.0
 */
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

    /**
     * Type of wave used for synthesis.
     */
    public enum WaveType {

        /**
         * Sinus wave
         */
        SINE,

        /**
         * Square wave
         */
        SQUARE,

        /**
         * Triangles
         */
        TRIANGLE
    }

    private Voice(final Long seed) {
        this(280.0, Duration.ofMillis(40), Percent.of(0.5), Percent.of(0.15), 5.0, Percent.of(0.025), Duration.ofMillis(300), Duration.ofMillis(150), Duration.ofMillis(60), WaveType.SINE, Percent.of(0.75), Percent.of(0.35), Percent.of(0.18), seed);
    }

    public Voice {
        requireNonNull(scratchyness, "scratchyness must not be null");
        Validate.range(scratchyness.value(), 0.01, 1.0, "scratchyness must be between 0.01 and 1.0");
        Validate.range(baseFrequency, 10.0, 500.0, "base frequency must be between 10.0 and 500.0");
        Validate.range(vibratoSpeed, 0.5, 20, "vibrato speed must be between 0.5 and 20.0");
        requireNonNull(baseBlipDuration, "base blip duration must not be null");
        requireNonNull(vowelVolume, "vowel volume must not be null");
        requireNonNull(nasalVolume, "nasal volume must not be null");
        requireNonNull(wordPause, "word pause must not be null");
        requireNonNull(commaPause, "comma pause must not be null");
        requireNonNull(pitchRandomness, "pitch randomness must not be null");
        requireNonNull(speedRandomness, "speed randomness must not be null");
        requireNonNull(waveType, "wave type must not be null");
        requireNonNull(vibratoDepth, "vibrato depth must not be null");
    }

    /**
     * Set the fundamental frequency of the voice in Hz. Valid range is 10.0 to 500.0.
     */
    public Voice baseFrequency(final double baseFrequency) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the type of sound wave used for synthesis.
     */
    public Voice waveType(final WaveType waveType) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the frequency or speed of the vibrato effect.
     */
    public Voice vibratoSpeed(final double vibratoSpeed) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the intensity or depth of the vibrato effect.
     */
    public Voice vibratoDepth(final Percent vibratoDepth) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the volume level specifically for vowel sounds.
     */
    public Voice vowelVolume(final Percent vowelVolume) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the volume level specifically for nasal sounds.
     */
    public Voice nasalVolume(final Percent nasalVolume) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the amount of random variation applied to the speech speed.
     */
    public Voice speedRandomness(final Percent speedRandomness) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the amount of random variation applied to the pitch.
     */
    public Voice pitchRandomness(final Percent pitchRandomness) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the amount of allowed distortion or scratchiness in the voice. Minimum is 0.01 to avoid muting the sound.
     */
    public Voice scratchyness(final Percent scratchyness) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the duration of the pause between words.
     */
    public Voice wordPause(final Duration wordPause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the duration of the pause at a comma.
     */
    public Voice commaPause(final Duration commaPause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the duration of the pause at the end of a sentence.
     */
    public Voice sentencePause(final Duration sentencePause) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Set the duration of an individual sound blip.
     */
    public Voice baseBlipDuration(final Duration baseBlipDuration) {
        return new Voice(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, scratchyness, seed);
    }

    /**
     * Create a new instance that will create slightly different results on every usage.
     */
    public static Voice variable() {
        return new Voice(null);
    }

    /**
     * Create a new instance that will create fixed results dependend on the specified seed.
     */
    public static Voice fixed(final long seed) {
        return new Voice(seed);
    }

    /**
     * Create a new instance that will create fixed results.
     */
    public static Voice fixed() {
        return fixed(Time.now().nanos());
    }
}
