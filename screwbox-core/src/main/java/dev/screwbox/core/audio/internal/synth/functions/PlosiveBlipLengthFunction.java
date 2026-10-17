package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class PlosiveBlipLengthFunction implements SynthFunction {

    private static final BlipLengthFunction BLIP_LENGTH = new BlipLengthFunction();

    @Override
    public Double apply(final SynthContext context, final Voice voice) {
        return BLIP_LENGTH.apply(context, voice) * 0.4;
    }
}
