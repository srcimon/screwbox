package dev.screwbox.core.audio;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;

import java.util.Objects;

//TODO rename options
public record VoiceOptions(double baseFrequency,//TODO optimize or validate type
                           double baseBlipDuration,//TODO optimize or validate type
                           double pitchRandomness,//TODO optimize or validate type
                           double speedRandomness,//TODO optimize or validate type
                           double vibratoSpeed,//TODO optimize or validate type
                           double vibratoDepth,//TODO optimize or validate type
                           double sentencePause,//TODO optimize or validate type
                           double commaPause,//TODO optimize or validate type
                           double wordPause,//TODO optimize or validate type
                           WaveType waveType,//TODO optimize or validate type
                           Percent vowelVolume,
                           Percent nasalVolume,
                           Percent plosiveVolume,
                           double lowPassCutoff,//TODO optimize or validate type
                           Long seed) {


    public enum WaveType {SINE, SQUARE, TRIANGLE}

    private VoiceOptions(final Long seed) {
        this(180.0, 0.04, 0.5, 0.15, 5.0, 0.025, 0.3, 0.15, 0.06, WaveType.SINE, Percent.of(0.75), Percent.of(0.35), Percent.of(0.45), 0.18, seed);
    }

    public VoiceOptions {
        Objects.requireNonNull(vowelVolume, "vowel volume must not be null");
        Objects.requireNonNull(nasalVolume, "nasal volume must not be null");
        Objects.requireNonNull(plosiveVolume, "plosive volume must not be null");
    }

    public VoiceOptions vowelVolume(final Percent vowelVolume) {
        return new VoiceOptions(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, plosiveVolume, lowPassCutoff, seed);
    }

    public VoiceOptions nasalVolume(final Percent nasalVolume) {
        return new VoiceOptions(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, plosiveVolume, lowPassCutoff, seed);
    }

    public VoiceOptions plosiveVolume(final Percent plosiveVolume) {
        return new VoiceOptions(baseFrequency, baseBlipDuration, pitchRandomness, speedRandomness, vibratoSpeed, vibratoDepth, sentencePause, commaPause, wordPause, waveType, vowelVolume, nasalVolume, plosiveVolume, lowPassCutoff, seed);
    }

    public static VoiceOptions variable() {
        return new VoiceOptions(null);
    }

    public static VoiceOptions fixed(final long seed) {
        return new VoiceOptions(seed);
    }

    public static VoiceOptions fixed() {
        return fixed(Time.now().nanos());
    }
}
