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

    private record SynthElement(
        BiFunction<SynthContext, VoiceOptions, Double> targetWovelShift,
        BiFunction<SynthContext, VoiceOptions, Double> phonemeCalculation,
        Function<SynthContext, Double> segmentVolume,
        BiFunction<SynthContext, VoiceOptions, Double> segmentFrequency,
        BiFunction<SynthContext, VoiceOptions, Double> blipLength
    ) {
//TODO add constructor without wovelconfig
    }

    //TODO Normalize Bifunctions
    private static final BiFunction<SynthContext, VoiceOptions, Double> NO_PHONEME = (context, options) -> 0.0;
    private static final BiFunction<SynthContext, VoiceOptions, Double> NASAL_PHONEME = (context, options) -> MathUtil.fastSin(context.runningPhase() * 0.5) * options.nasalGain();
    private static final Function<SynthContext, Double> RANDOM_SEGMENT_VOLUME = (context) -> 0.85 + context.randomDouble() * 0.15;

    private static final BiFunction<SynthContext, VoiceOptions, Double> SEGMENT_BASE_FREQUENCY = (context, options) -> options.baseFreq();
    private static final BiFunction<SynthContext, VoiceOptions, Double> RANDOM_FREQUENCY = (context, options) -> {
        double randomPitchOffset = (context.randomDouble() * 2.0 - 1.0) * options.pitchRandomness();
        return options.baseFreq() * (1.0 + randomPitchOffset);
    };

    private static final BiFunction<SynthContext, VoiceOptions, Double> SENTENCE_PAUSE_BLIP_LENGTH = (context, options) -> options.sentencePause();
    private static final BiFunction<SynthContext, VoiceOptions, Double> COMMA_PAUSE_BLIP_LENGTH = (context, options) -> options.commaPause();
    private static final BiFunction<SynthContext, VoiceOptions, Double> WORD_PAUSE_BLIP_LENGTH = (context, options) -> options.wordPause();
    private static final BiFunction<SynthContext, VoiceOptions, Double> PLOSIVE_BLIP_LENGTH = (context, options) -> options.baseBlipDuration() * (1.0 + (context.randomDouble() * 2.0 - 1.0) * options.speedRandomness()) * 0.4;
    private static final BiFunction<SynthContext, VoiceOptions, Double> DEFAULT_BLIP_LENGTH = (context, options) -> options.baseBlipDuration() * (1.0 + (context.randomDouble() * 2.0 - 1.0) * options.speedRandomness());


    private record WovelPhoneme(double wovelHeight,
                                double wovelBackness) implements BiFunction<SynthContext, VoiceOptions, Double> {

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
        entry('.', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry(',', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry('!', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry('?', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, SENTENCE_PAUSE_BLIP_LENGTH)),
        entry(';', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry(':', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, COMMA_PAUSE_BLIP_LENGTH)),
        entry('-', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, WORD_PAUSE_BLIP_LENGTH)),

        // space
        entry(' ', new SynthElement((context, options) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, SEGMENT_BASE_FREQUENCY, WORD_PAUSE_BLIP_LENGTH))
    );

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
                var synthElement = SYNTH_CONFIG.getOrDefault(character, new SynthElement((context, voiceOptions) -> context.currentVowelShift(), NO_PHONEME, (context) -> 0.0, RANDOM_FREQUENCY, DEFAULT_BLIP_LENGTH));
                double textProgress = (double) i / chars.length;

                addSampleForCharacter(options, synthElement, soundContext, dos, textProgress);//TODO move text progress into context
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

    private static void addSampleForCharacter(VoiceOptions options, SynthElement element,
                                              SynthContext context, DataOutputStream dos, double textProgress) {
        int currentSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * element.blipLength().apply(context, options));

        double targetVowelShift = element.targetWovelShift().apply(context, options);

        // Menschliche Intonationskurve: Tonhöhe fällt zum Satzende hin natürlich ab
        double intonation = MathUtil.fastSin(textProgress * Math.PI) * 0.15 - (textProgress * 0.1);
        double targetFrequency = element.segmentFrequency.apply(context, options) * (1.0 + intonation);

        double segmentVolume = element.segmentVolume.apply(context);

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
            double rawValue = element.phonemeCalculation().apply(context, options);
            double envelope = applyClickProtectionEnvelope(s, currentSamplesPerBlip);

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

}