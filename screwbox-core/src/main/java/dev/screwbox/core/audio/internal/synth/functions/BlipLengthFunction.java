package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class BlipLengthFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final VoiceOptions options) {
        return options.baseBlipDuration() * (1.0 + (context.randomDouble() * 2.0 - 1.0) * options.speedRandomness());
    }
}
