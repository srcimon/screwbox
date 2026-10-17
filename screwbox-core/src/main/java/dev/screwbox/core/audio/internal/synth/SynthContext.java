package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.Time;
import dev.screwbox.core.audio.VoiceOptions;

import java.util.Random;

import static java.util.Objects.isNull;

public class SynthContext {

    private final Random random;

    private double runningPhase = 0.0;
    private double pitch;
    private double vowelShift = 1.0;
    private int samplesWritten = 0;
    private double filterState = 0.0;

    //TODO simplify methods
    public SynthContext(final VoiceOptions options) {
        this.pitch = options.baseFreq();
        this.random = new Random(isNull(options.seed())
            ? Time.now().milliseconds()
            : options.seed());
    }

    public double currentVowelShift() {
        return vowelShift;
    }

    public double randomDouble() {
        return random.nextDouble();
    }

    public double currentPitch() {
        return pitch;
    }

    public int totalSamplesWritten() {
        return samplesWritten;
    }

    public double runningPhase() {
        return runningPhase;
    }

    public double filterState() {
        return filterState;
    }

    public void setCurrentPitch(double currentPitch) {
        this.pitch = currentPitch;
    }

    public void setVowelShift(double vowelShift) {
        this.vowelShift = vowelShift;
    }

    public void setSamplesWritten(int samplesWritten) {
        this.samplesWritten = samplesWritten;
    }

    public void setRunningPhase(double runningPhase) {
        this.runningPhase = runningPhase;
    }

    public void setFilterState(double filterState) {
        this.filterState = filterState;
    }
}