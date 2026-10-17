package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.audio.VoiceOptions;
import dev.screwbox.core.utils.MathUtil;

import java.util.Map;

import static java.util.Map.entry;

public class SynthConfiguration {

    private static final SynthFunction NO_PHONEME = (context, options) -> 0.0;
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


    public record WovelPhoneme(double wovelHeight,
                                double wovelBackness) implements SynthFunction {

        @Override
        public Double apply(SynthContext context, VoiceOptions options) {
            final var phase = context.runningPhase();
            double baseWave = switch (options.waveType()) {
                case SINE -> MathUtil.fastSin(phase);
                // Ultra-weiche Annäherung einer Dreieckswelle für wärmeren Sound
                case SQUARE -> MathUtil.fastSin(MathUtil.fastSin(phase) * 1.2) * 0.8;
                case TRIANGLE -> (Math.abs((phase % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
            };
            // Harmonische Obertöne sehr subtil beimischen (Verhindert Klingeln im Ohr)
            double formant1 = MathUtil.fastSin(phase * wovelHeight) * 0.25;
            double formant2 = MathUtil.fastSin(phase * wovelBackness) * 0.10;

            return MathUtil.fastSin((baseWave + formant1 + formant2) * 0.8) * options.vowelGain();
        }
    }



    //TODO get rid of CharacterType
    private static final Map<Character, SynthElement> SYNTH_CONFIG = Map.ofEntries(
        // vowel
        entry('A', new SynthElement((context, options) -> 0.92, new WovelPhoneme(2.0, 3.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),//TODO MOVE CONFIG INSIDE
        entry('E', new SynthElement((context, options) -> 1.05, new WovelPhoneme(1.5, 4.0), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('I', new SynthElement((context, options) -> 1.12, new WovelPhoneme(1.2, 4.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('O', new SynthElement((context, options) -> 0.85, new WovelPhoneme(1.4, 2.4), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('U', new SynthElement((context, options) -> 0.75, new WovelPhoneme(1.1, 1.8), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('Ä', new SynthElement((context, options) -> 0.92, new WovelPhoneme(1.8, 4.0), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('Ö', new SynthElement((context, options) -> 0.85, new WovelPhoneme(1.5, 3.2), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('Ü', new SynthElement((context, options) -> 1.12, new WovelPhoneme(1.2, 3.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),

        // plosive
        entry('P', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('T', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('K', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('B', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('D', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),
        entry('G', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, PLOSIVE_BLIP_LENGTH)),

        // nasal
        entry('M', new SynthElement((context, options) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('N', new SynthElement((context, options) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('L', new SynthElement((context, options) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('W', new SynthElement((context, options) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),
        entry('R', new SynthElement((context, options) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH)),

        // punctuation
        entry('.', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry(',', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry('!', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry('?', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry(';', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry(':', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry('-', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, WORD_PAUSE_BLIP_LENGTH)),

        // space
        entry(' ', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context, options) -> 0.0, SEGMENT_BASE_FREQUENCY, WORD_PAUSE_BLIP_LENGTH))
    );

    public static SynthElement findElement(final char character) {
        return SYNTH_CONFIG.getOrDefault(character, new SynthElement((context, voiceOptions) -> context.currentVowelShift(), NO_PHONEME, (context, voiceOptions) -> 0.0, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH));
    }
}
