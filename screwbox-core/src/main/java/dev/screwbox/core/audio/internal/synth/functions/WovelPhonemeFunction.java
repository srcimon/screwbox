package dev.screwbox.core.audio.internal.synth.functions;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthFunction;
import dev.screwbox.core.utils.MathUtil;

public record WovelPhonemeFunction(double wovelHeight, double wovelBackness) implements SynthFunction {

    @Override
    public Double apply(final SynthContext context, final VoiceOptions options) {
        final var phase = context.phase();
        double baseWave = switch (options.waveType()) {
            case SINE -> MathUtil.fastSin(phase);
            case SQUARE -> MathUtil.fastSin(MathUtil.fastSin(phase) * 1.2) * 0.8;
            case TRIANGLE -> (Math.abs((phase % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
        };
        final double formant1 = MathUtil.fastSin(phase * wovelHeight) * 0.25;
        final double formant2 = MathUtil.fastSin(phase * wovelBackness) * 0.10;

        return MathUtil.fastSin((baseWave + formant1 + formant2) * 0.8) * options.vowelGain();
    }
}
