package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;

public class CommaPauseFunction implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final Voice voice) {
        return voice.commaPause().exactSeconds();
    }
}
