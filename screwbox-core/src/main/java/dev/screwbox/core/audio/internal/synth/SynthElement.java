package dev.screwbox.core.audio.internal.synth;

public record SynthElement(
    SynthFunction targetWovelShift,
    SynthFunction phoneme,
    SynthFunction segmentVolume,
    SynthFunction segmentFrequency,
    SynthFunction blipLength
) {
}
