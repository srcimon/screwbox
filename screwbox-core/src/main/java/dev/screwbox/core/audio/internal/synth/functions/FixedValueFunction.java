package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public record FixedValueFunction(double value) implements SynthFunction {

    public FixedValueFunction() {
        this(0.0);
    }

    @Override
    public Double apply(final SynthContext context, final VoiceOptions options) {
        return value;
    }
}
