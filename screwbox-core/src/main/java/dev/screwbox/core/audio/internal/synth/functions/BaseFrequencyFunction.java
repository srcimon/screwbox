package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class BaseFrequencyFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final VoiceOptions options) {
        return options.baseFrequency();
    }
}
