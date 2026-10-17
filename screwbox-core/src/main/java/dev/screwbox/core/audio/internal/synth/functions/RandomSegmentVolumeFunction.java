package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class RandomSegmentVolumeFunction implements SynthFunction {
    @Override
    public Double apply(final SynthContext context, final VoiceOptions options) {
        return 0.85 + context.randomDouble() * 0.15;
    }
}
