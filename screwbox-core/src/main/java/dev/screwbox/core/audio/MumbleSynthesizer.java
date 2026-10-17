package dev.screwbox.core.audio;

import dev.screwbox.core.audio.internal.SynthContext;
import dev.screwbox.core.audio.internal.WaveSupport;
import dev.screwbox.core.utils.MathUtil;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Map.entry;

//TODO rename to synthesizer
public class MumbleSynthesizer {

    private static final double TO_SIGNED_INTEGER = 32767.0;
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 2, true, false);

    private record SynthElement(CharacterType type,
                                Function<SynthContext, Double> targetWovelShift,
                                BiFunction<Double, VoiceOptions, Double> phonemeCalculation,
                                Function<SynthContext, Double> segmentVolume,
                                BiFunction<VoiceOptions, SynthContext, Double> segmentFrequency
    ) {
//TODO add constructor without wovelconfig
    }

    private static final BiFunction<Double, VoiceOptions, Double> NO_PHONEME = (phase, options) -> 0.0;
    private static final BiFunction<Double, VoiceOptions, Double> NASAL_PHONEME = (phase, options) -> MathUtil.fastSin(phase * 0.5) * options.nasalGain();
    private static final Function<SynthContext, Double> RANDOM_SEGMENT_VOLUME = (context) -> 0.85 + context.randomDouble() * 0.15;

    private static final BiFunction<VoiceOptions, SynthContext, Double> SEGMENT_BASE_FREQUENCY = (options, context) -> options.baseFreq();
    private static final BiFunction<VoiceOptions, SynthContext, Double> RANDOM_FREQUENCY = (options, context) -> {
        double randomPitchOffset = (context.randomDouble() * 2.0 - 1.0) * options.pitchRandomness();
        return options.baseFreq() * (1.0 + randomPitchOffset);
    };

    private record WovelPhoneme(double wovelHeight,
                                double wovelBackness) implements BiFunction<Double, VoiceOptions, Double> {

        @Override
        public Double apply(Double phase, VoiceOptions options) {
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
        entry('A', new SynthElement(CharacterType.VOWEL, (context) -> 0.92, new WovelPhoneme(2.0, 3.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),//TODO MOVE CONFIG INSIDE
        entry('E', new SynthElement(CharacterType.VOWEL, (context) -> 1.05, new WovelPhoneme(1.5, 4.0), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('I', new SynthElement(CharacterType.VOWEL, (context) -> 1.12, new WovelPhoneme(1.2, 4.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('O', new SynthElement(CharacterType.VOWEL, (context) -> 0.85, new WovelPhoneme(1.4, 2.4), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('U', new SynthElement(CharacterType.VOWEL, (context) -> 0.75, new WovelPhoneme(1.1, 1.8), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('Ä', new SynthElement(CharacterType.VOWEL, (context) -> 0.92, new WovelPhoneme(1.8, 4.0), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('Ö', new SynthElement(CharacterType.VOWEL, (context) -> 0.85, new WovelPhoneme(1.5, 3.2), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('Ü', new SynthElement(CharacterType.VOWEL, (context) -> 1.12, new WovelPhoneme(1.2, 3.5), RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),

        entry('P', new SynthElement(CharacterType.PLOSIVE, SynthContext::currentVowelShift, NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('T', new SynthElement(CharacterType.PLOSIVE, SynthContext::currentVowelShift, NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('K', new SynthElement(CharacterType.PLOSIVE, SynthContext::currentVowelShift, NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('B', new SynthElement(CharacterType.PLOSIVE, SynthContext::currentVowelShift, NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('D', new SynthElement(CharacterType.PLOSIVE, SynthContext::currentVowelShift, NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('G', new SynthElement(CharacterType.PLOSIVE, SynthContext::currentVowelShift, NO_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),

        entry('M', new SynthElement(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('N', new SynthElement(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('L', new SynthElement(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('W', new SynthElement(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),
        entry('R', new SynthElement(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME, RANDOM_SEGMENT_VOLUME, RANDOM_FREQUENCY)),

        entry('.', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),
        entry(',', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),
        entry('!', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),
        entry('?', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),
        entry(';', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),
        entry(':', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),
        entry('-', new SynthElement(CharacterType.PUCTUATION, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY)),

        entry(' ', new SynthElement(CharacterType.SPACE, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY))
    );

    //TODO get rid off enum
    enum CharacterType {
        VOWEL,
        PLOSIVE,
        NASAL,
        PUCTUATION,
        SPACE,
        UNKNOWM
    }

    public static Sound synthesizeMumbleSpeech(final String text, final VoiceOptions options) {
        final String sanitizedText = text.toUpperCase();
        try (final var baos = new ByteArrayOutputStream(8192);
            DataOutputStream dos = new DataOutputStream(baos)) {

            byte[] dummyHeader = new byte[WaveSupport.WAV_HEADER_SIZE];
            dos.write(dummyHeader);


            var soundContext = new SynthContext(options);
            char[] chars = sanitizedText.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                char character = chars[i];
                var synthElement = SYNTH_CONFIG.getOrDefault(character, new SynthElement(CharacterType.UNKNOWM, SynthContext::currentVowelShift, NO_PHONEME, (context) -> 0.0, RANDOM_FREQUENCY));
                double textProgress = (double) i / chars.length;

                addSampleForCharacter(options, character, synthElement, soundContext, dos, textProgress);
            }
            dos.flush();

            byte[] finalWav = baos.toByteArray();
            int audioDataLength = finalWav.length - WaveSupport.WAV_HEADER_SIZE;

            ByteBuffer headerBuffer = ByteBuffer.wrap(finalWav).order(ByteOrder.LITTLE_ENDIAN);
            WaveSupport.writeHeaderToBuffer(audioDataLength, AUDIO_FORMAT, headerBuffer);

            return Sound.fromSoundData(finalWav);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addSampleForCharacter(VoiceOptions options, char character, SynthElement config,
                                              SynthContext context, DataOutputStream dos, double textProgress) {
        int currentSamplesPerBlip = calculateSingleBlipLength(character, config, options, context);

        double targetVowelShift = config.targetWovelShift().apply(context);

        // Menschliche Intonationskurve: Tonhöhe fällt zum Satzende hin natürlich ab
        double intonation = MathUtil.fastSin(textProgress * Math.PI) * 0.15 - (textProgress * 0.1);
        double targetFrequency = config.segmentFrequency.apply(options, context) * (1.0 + intonation);

        double segmentVolume = config.segmentVolume.apply(context);

        for (int s = 0; s < currentSamplesPerBlip; s++) {
            // Extrem glatte Übergänge für organischen Redefluss
            context.setVowelShift(context.currentVowelShift() + (targetVowelShift - context.currentVowelShift()) * 0.002);
            context.setCurrentPitch(context.currentPitch() + (targetFrequency - context.currentPitch()) * 0.002);

            double globalTime = (double) context.totalSamplesWritten() / AUDIO_FORMAT.getSampleRate();

            // Shimmer & Jitter: Winzige, unvorhersehbare Muskelunreinheiten der Stimme
            double jitter = (context.randomDouble() - 0.5) * 0.002;
            double vibrato = MathUtil.fastSin(2.0 * Math.PI * options.vibratoSpeed() * globalTime) * options.vibratoDepth();

            double instantFreq = Math.clamp(context.currentPitch() * (1.0 + (context.currentVowelShift() - 1.0) * 0.20) * (1.0 + vibrato + jitter), 45.0, 350.0);
            context.setRunningPhase((context.runningPhase() + (2.0 * Math.PI * instantFreq) / AUDIO_FORMAT.getSampleRate()) % (2.0 * Math.PI));

            // Synthese und Hüllkurve gegen Knacken (Fade-In / Fade-Out pro Blip)
            double rawValue = config.phonemeCalculation().apply(context.runningPhase(), options);
            double envelope = CharacterType.PUCTUATION == config.type() ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);

            // Menschliche Amplitudenschwankung (Atem)
            double breathShimmer = 1.0 + (MathUtil.fastSin(globalTime * 2.0) * 0.05);
            double preFilteredSample = Math.clamp(rawValue * envelope * segmentVolume * breathShimmer, -1.0, 1.0);

            // Tiefpassfilterung (Getrennte Zustände für Stereo-Breite, falls gewünscht)
            context.setFilterState(context.filterState() + options.lowPassCutoff() * (preFilteredSample - context.filterState()));

            short pcmSampleLeft = (short) (context.filterState() * TO_SIGNED_INTEGER);
            short pcmSampleRight = (short) (context.filterState() * TO_SIGNED_INTEGER);

            try {
                dos.writeByte(pcmSampleLeft & 0xFF);
                dos.writeByte((pcmSampleLeft >> 8) & 0xFF);
                dos.writeByte(pcmSampleRight & 0xFF);
                dos.writeByte((pcmSampleRight >> 8) & 0xFF);
            } catch (IOException ignored) {
            }

            context.setSamplesWritten(context.totalSamplesWritten() + 1);
        }
    }


    private static double applyClickProtectionEnvelope(int sampleIdx, int totalSamples) {
        int fadeSamples = Math.min(120, totalSamples / 10); // Sanftes Ein-/Ausblenden (ca. 2-3ms)
        if (sampleIdx < fadeSamples) return (double) sampleIdx / fadeSamples;
        if (sampleIdx > totalSamples - fadeSamples) return (double) (totalSamples - sampleIdx) / fadeSamples;
        return 1.0;
    }

    private static int calculateSingleBlipLength(char c, SynthElement config, VoiceOptions options, SynthContext context) {
        if (c == '.' || c == '!' || c == '?') {
            return (int) (AUDIO_FORMAT.getSampleRate() * options.sentencePause());
        }
        if (c == ',' || c == ';' || c == ':') {
            return (int) (AUDIO_FORMAT.getSampleRate() * options.commaPause());
        }
        if (c == ' ' || c == '-') {
            return (int) (AUDIO_FORMAT.getSampleRate() * options.wordPause());
        }

        int baseSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * options.baseBlipDuration());
        double deviation = (context.randomDouble() * 2.0 - 1.0) * options.speedRandomness();

        double speedModifier = 1.0 + deviation;
        if (config.type() == CharacterType.PLOSIVE) {
            speedModifier *= 0.4; // Kürzere Artikulation
        }

        return (int) (baseSamplesPerBlip * speedModifier);
    }

}