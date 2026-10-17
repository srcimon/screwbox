package dev.screwbox.core.audio;

import dev.screwbox.core.Time;

//TODO rename options
//TODO find better data types if possible
public record VoiceOptions(double baseFreq,
                           double baseBlipDuration,
                           double pitchRandomness,
                           double speedRandomness,
                           double vibratoSpeed,
                           double vibratoDepth,
                           double sentencePause,
                           double commaPause,
                           double wordPause,
                           WaveType waveType,
                           double vowelGain,
                           double nasalGain,
                           double plosiveGain,
                           double lowPassCutoff,
                           Long seed) {

    private VoiceOptions(final Long seed) {
        this(180.0, 0.04, 0.5, 0.15, 5.0, 0.025, 0.3, 0.15, 0.06, WaveType.SINE, 0.75, 0.35, 0.45, 0.18, seed);
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

    public enum WaveType {SINE, SQUARE, TRIANGLE}
}
