package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.audio.Voice;

import java.util.function.BiFunction;

@FunctionalInterface
public interface SynthFunction extends BiFunction<SynthContext, Voice, Double> {
}
