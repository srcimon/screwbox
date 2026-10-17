package dev.screwbox.core.audio.internal;

import dev.screwbox.core.audio.VoiceOptions;

import java.util.function.BiFunction;

public interface SynthFunction extends BiFunction<SynthContext, VoiceOptions, Double> {
}
