package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.audio.internal.SynthFunction;

public record SynthElement(
    SynthFunction targetWovelShift,
    SynthFunction phonemeCalculation,
    SynthFunction segmentVolume,
    SynthFunction segmentFrequency,
    SynthFunction blipLength
) {
    //TODO insert calc methods here
}
