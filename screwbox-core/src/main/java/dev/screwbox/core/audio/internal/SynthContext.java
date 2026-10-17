package dev.screwbox.core.audio.internal;

import java.util.Random;

//TODO rename all properties
public class SynthContext {
    private double runningPhase = 0.0;
    private double pitch;
    private double currentVowelShift = 1.0;
    private int totalSamplesWritten = 0;
    private double filterStateLeft = 0.0;
    private double filterStateRight = 0.0;
    private final Random random;

    public SynthContext(final double pitch, final long seed) {
        this.pitch = pitch;
        this.random = new Random(seed);
    }

    public double currentVowelShift() {
        return currentVowelShift;
    }

    public double randomDouble() {
        return random.nextDouble();
    }

    public double currentPitch() {
        return pitch;
    }

    public int totalSamplesWritten() {
        return totalSamplesWritten;
    }

    public double runningPhase() {
        return runningPhase;
    }
    //TODO combine filter states!
    public double filterStateLeft() {
        return filterStateLeft;
    }

    public double filterStateRight() {
        return filterStateRight;
    }

    public void setCurrentPitch(double currentPitch) {
        this.pitch = currentPitch;
    }

    public void setCurrentVowelShift(double currentVowelShift) {
        this.currentVowelShift = currentVowelShift;
    }

    public void setTotalSamplesWritten(int totalSamplesWritten) {
        this.totalSamplesWritten = totalSamplesWritten;
    }

    public void setRunningPhase(double runningPhase ) {
        this.runningPhase = runningPhase;
    }

    public void setFilterStateLeft(double filterStateLeft) {
        this.filterStateLeft = filterStateLeft;
    }

    public void setFilterStateRight(double filterStateRight) {
        this.filterStateRight = filterStateRight;
    }
}