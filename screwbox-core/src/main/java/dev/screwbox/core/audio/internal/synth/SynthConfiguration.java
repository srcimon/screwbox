package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.audio.internal.synth.functions.CurrentWovelShiftFunction;
import dev.screwbox.core.audio.internal.synth.functions.FixedValueFunction;
import dev.screwbox.core.audio.internal.synth.functions.WovelPhonemeFunction;
import dev.screwbox.core.utils.MathUtil;

import java.util.Map;

import static java.util.Map.entry;

public class SynthConfiguration {

    private static final SynthFunction NASAL_PHONEME = (context, options) -> MathUtil.fastSin(context.runningPhase() * 0.5) * options.nasalGain();
    private static final SynthFunction RANDOM_SEGMENT_VOLUME = (context, options) -> 0.85 + context.randomDouble() * 0.15;

    private static final SynthFunction SEGMENT_BASE_FREQUENCY = (context, options) -> options.baseFreq();
    private static final SynthFunction RANDOM_FREQUENCY = (context, options) -> {
        double randomPitchOffset = (context.randomDouble() * 2.0 - 1.0) * options.pitchRandomness();
        return options.baseFreq() * (1.0 + randomPitchOffset);
    };

    private static final SynthFunction SENTENCE_PAUSE_BLIP_LENGTH = (context, options) -> options.sentencePause();
    private static final SynthFunction COMMA_PAUSE_BLIP_LENGTH = (context, options) -> options.commaPause();
    private static final SynthFunction WORD_PAUSE_BLIP_LENGTH = (context, options) -> options.wordPause();
    private static final SynthFunction PLOSIVE_BLIP_LENGTH = (context, options) -> options.baseBlipDuration() * (1.0 + (context.randomDouble() * 2.0 - 1.0) * options.speedRandomness()) * 0.4;
    private static final SynthFunction DEFAULT_BLIP_LENGTH = (context, options) -> options.baseBlipDuration() * (1.0 + (context.randomDouble() * 2.0 - 1.0) * options.speedRandomness());


    //TODO get rid of CharacterType
    private static final Map<Character, SynthElement> SYNTH_CONFIG = Map.ofEntries(
        // vowel
        entry('A', new SynthElement(new FixedValueFunction(0.92), new WovelPhonemeFunction(2.0, 3.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),//TODO MOVE CONFIG INSIDE
        entry('E', new SynthElement(new FixedValueFunction(1.05), new WovelPhonemeFunction(1.5, 4.0), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('I', new SynthElement(new FixedValueFunction(1.12), new WovelPhonemeFunction(1.2, 4.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('O', new SynthElement(new FixedValueFunction(0.85), new WovelPhonemeFunction(1.4, 2.4), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('U', new SynthElement(new FixedValueFunction(0.75), new WovelPhonemeFunction(1.1, 1.8), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('Ä', new SynthElement(new FixedValueFunction(92), new WovelPhonemeFunction(1.8, 4.0), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('Ö', new SynthElement(new FixedValueFunction(0.85), new WovelPhonemeFunction(1.5, 3.2), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('Ü', new SynthElement(new FixedValueFunction(1.12), new WovelPhonemeFunction(1.2, 3.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),

        // plosive
        entry('P', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('T', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('K', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('B', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('D', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('G', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),

        // nasal
        entry('M', new SynthElement(new FixedValueFunction(0.88), NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('N', new SynthElement(new FixedValueFunction(0.88),  NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('L', new SynthElement(new FixedValueFunction(0.88),  NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('W', new SynthElement(new FixedValueFunction(0.88),  NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('R', new SynthElement(new FixedValueFunction(0.88),  NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),

        // punctuation
        entry('.', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry(',', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry('!', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry('?', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry(';', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry(':', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry('-', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, WORD_PAUSE_BLIP_LENGTH)),

        // space
        entry(' ', new SynthElement(new CurrentWovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), SEGMENT_BASE_FREQUENCY, WORD_PAUSE_BLIP_LENGTH))
    );

    public static SynthElement findElement(final char character) {
        return SYNTH_CONFIG.getOrDefault(character, new SynthElement((context, voiceOptions) -> context.currentVowelShift(), new FixedValueFunction(), (context, voiceOptions) -> 0.0, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH));
    }
}
