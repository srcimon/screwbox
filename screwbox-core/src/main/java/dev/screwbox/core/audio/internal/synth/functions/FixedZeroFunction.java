package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class FixedZeroFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext synthContext, final VoiceOptions options) {
        return 0.0;
    }
}
