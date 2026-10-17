package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;
import dev.screwbox.core.utils.MathUtil;

public class NasalPhonemeFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final VoiceOptions options) {
        return MathUtil.fastSin(context.runningPhase() * 0.5) * options.nasalGain();
    }
}
