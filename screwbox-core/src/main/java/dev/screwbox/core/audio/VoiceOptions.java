package dev.screwbox.core.audio;

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
                           MumbleSynthesizer.WaveType waveType,
                           double vowelGain,
                           double nasalGain,
                           double plosiveGain,
                           double lowPassCutoff,
                           Long seed) {

    private VoiceOptions() {
        this(140.0, 0.04, 0.6, 0.15, 5.0, 0.015, 0.5, 0.25, 0.12, MumbleSynthesizer.WaveType.SQUARE, 0.75, 0.35, 0.45, 0.18, null);
    }

    //TODO why would male be the default
    public static VoiceOptions male() {
        return new VoiceOptions();
    }
}
