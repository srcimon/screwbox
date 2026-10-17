package dev.screwbox.core.audio.internal.synth;

public record SynthElement(
    SynthFunction targetWovelShift,
    SynthFunction phonemeCalculation,
    SynthFunction segmentVolume,
    SynthFunction segmentFrequency,
    SynthFunction blipLength
) {
    //TODO insert calc methods here
}
