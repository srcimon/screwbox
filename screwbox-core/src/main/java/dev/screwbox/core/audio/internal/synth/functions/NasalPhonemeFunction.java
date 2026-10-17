package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;
import dev.screwbox.core.utils.MathUtil;

public class NasalPhonemeFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final Voice voice) {
        return MathUtil.fastSin(context.phase() * 0.5) * voice.nasalVolume().value();
    }
}
