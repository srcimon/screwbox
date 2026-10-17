package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.Time;
import dev.screwbox.core.audio.VoiceOptions;

import java.util.Random;

import static java.util.Objects.isNull;

public class SynthContext {

    private final Random random;

    private double phase = 0.0;
    private double vowelShift = 1.0;
    private int samplesWritten = 0;
    private double filterState = 0.0;
    private double progress = 0.0;
    private double pitch;

    //TODO simplify methods
    public SynthContext(final VoiceOptions options) {
        this.pitch = options.baseFreq();
        this.random = new Random(isNull(options.seed())
            ? Time.now().milliseconds()
            : options.seed());
    }

    public double vowelShift() {
        return vowelShift;
    }

    public double pitch() {
        return pitch;
    }

    public int totalSamplesWritten() {
        return samplesWritten;
    }

    public double phase() {
        return phase;
    }

    public double filterState() {
        return filterState;
    }

    public void setPitch(final double pitch) {
        this.pitch = pitch;
    }

    public void setVowelShift(final double vowelShift) {
        this.vowelShift = vowelShift;
    }

    public void setSamplesWritten(final int samplesWritten) {
        this.samplesWritten = samplesWritten;
    }

    public void setPhase(final double phase) {
        this.phase = phase;
    }

    public void setFilterState(final double filterState) {
        this.filterState = filterState;
    }

    public double randomDouble() {
        return random.nextDouble();
    }

    public void setProgress(final double progress) {
        this.progress = progress;
    }

    public double progress() {
        return progress;
    }
}