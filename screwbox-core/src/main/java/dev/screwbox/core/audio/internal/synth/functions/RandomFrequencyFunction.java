package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class RandomFrequencyFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final Voice voice) {
        return voice.baseFrequency() * (1.0 + (context.randomDouble() * 2.0 - 1.0) * voice.pitchRandomness().value());
    }
}
