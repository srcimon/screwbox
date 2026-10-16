package dev.screwbox.core.audio;

import dev.screwbox.core.audio.internal.WaveSupport;
import dev.screwbox.core.utils.MathUtil;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Map.entry;

public class MumbleSynthesizer {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 2, true, false);
    private static final Random random = new Random();

    private record SynthConfig(CharacterType type, Function<SoundContext, Double> targetWovelShift,
                               BiFunction<Double, VoiceOptions, Double> phonemeCalculation) {
//TODO add constructor without wovelconfig
    }

    private static final BiFunction<Double, VoiceOptions, Double> NO_PHONEME = (phase, options) -> 0.0;
    private static final BiFunction<Double, VoiceOptions, Double> NASAL_PHONEME = (phase, options) -> MathUtil.fastSin(phase * 0.5) * options.nasalGain;

    private record WovelPhoneme(double wovelHeight,
                                double wovelBackness) implements BiFunction<Double, VoiceOptions, Double> {

        @Override
        public Double apply(Double phase, VoiceOptions options) {
            double baseWave = switch (options.waveType) {
                case SINE -> MathUtil.fastSin(phase);
                // Ultra-weiche Annäherung einer Dreieckswelle für wärmeren Sound
                case SQUARE -> MathUtil.fastSin(MathUtil.fastSin(phase) * 1.2) * 0.8;
                case TRIANGLE -> (Math.abs((phase % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
            };
            // Harmonische Obertöne sehr subtil beimischen (Verhindert Klingeln im Ohr)
            double formant1 = MathUtil.fastSin(phase * wovelHeight) * 0.25;
            double formant2 = MathUtil.fastSin(phase * wovelBackness) * 0.10;

            return MathUtil.fastSin((baseWave + formant1 + formant2) * 0.8) * options.vowelGain;
        }
    }

    private static final Map<Character, SynthConfig> SYNTH_CONFIG = Map.ofEntries(
        entry('A', new SynthConfig(CharacterType.VOWEL, (context) -> 0.92, new WovelPhoneme(2.0, 3.5))),//TODO MOVE CONFIG INSIDE
        entry('E', new SynthConfig(CharacterType.VOWEL, (context) -> 1.05, new WovelPhoneme(1.5, 4.0))),
        entry('I', new SynthConfig(CharacterType.VOWEL, (context) -> 1.12, new WovelPhoneme(1.2, 4.5))),
        entry('O', new SynthConfig(CharacterType.VOWEL, (context) -> 0.85, new WovelPhoneme(1.4, 2.4))),
        entry('U', new SynthConfig(CharacterType.VOWEL, (context) -> 0.75, new WovelPhoneme(1.1, 1.8))),
        entry('Ä', new SynthConfig(CharacterType.VOWEL, (context) -> 0.92, new WovelPhoneme(1.8, 4.0))),
        entry('Ö', new SynthConfig(CharacterType.VOWEL, (context) -> 0.85, new WovelPhoneme(1.5, 3.2))),
        entry('Ü', new SynthConfig(CharacterType.VOWEL, (context) -> 1.12, new WovelPhoneme(1.2, 3.5))),

        entry('P', new SynthConfig(CharacterType.PLOSIVE, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('T', new SynthConfig(CharacterType.PLOSIVE, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('K', new SynthConfig(CharacterType.PLOSIVE, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('B', new SynthConfig(CharacterType.PLOSIVE, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('D', new SynthConfig(CharacterType.PLOSIVE, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('G', new SynthConfig(CharacterType.PLOSIVE, (context) -> context.currentVowelShift, NO_PHONEME)),

        entry('M', new SynthConfig(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME)),
        entry('N', new SynthConfig(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME)),
        entry('L', new SynthConfig(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME)),
        entry('W', new SynthConfig(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME)),
        entry('R', new SynthConfig(CharacterType.NASAL, (context) -> 0.88, NASAL_PHONEME)),

        entry('.', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry(',', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('!', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('?', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry(';', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry(':', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),
        entry('-', new SynthConfig(CharacterType.PUCTUATION, (context) -> context.currentVowelShift, NO_PHONEME)),

        entry(' ', new SynthConfig(CharacterType.SPACE, (context) -> context.currentVowelShift, NO_PHONEME))
    );

    public enum WaveType {SINE, SQUARE, TRIANGLE}

    enum CharacterType {
        VOWEL,
        PLOSIVE,
        NASAL,
        PUCTUATION,
        SPACE,
        UNKNOWM
    }

    public static class VoiceOptions {
        public double baseFreq = 110.0;
        public double baseBlipDuration = 0.07; // Leicht verkürzt für knackigeres Sprechtempo
        public double pitchRandomness = 0.08;   // Reduziert, da Satzmelodie jetzt steuert
        public double speedRandomness = 0.15;
        public double vibratoSpeed = 5.0;
        public double vibratoDepth = 0.015;

        public double sentencePause = 0.50;
        public double commaPause = 0.25;
        public double wordPause = 0.12;

        public WaveType waveType = WaveType.SINE;

        public double vowelGain = 0.75;
        public double nasalGain = 0.35;
        public double plosiveGain = 0.45;
        public double lowPassCutoff = 0.18;
    }

    private static class SoundContext {
        double runningPhase = 0.0;
        double currentPitch;
        double currentVowelShift = 1.0;
        int totalSamplesWritten = 0;
        double filterStateLeft = 0.0;
        double filterStateRight = 0.0;

        public SoundContext(double startPitch) {
            currentPitch = startPitch;
        }
    }

    public static Sound synthesizeMumbleSpeech(final String text, final VoiceOptions options) {
        String sanitizedText = text.toUpperCase();

        try (final var baos = new ByteArrayOutputStream(8192);
            DataOutputStream dos = new DataOutputStream(baos)) {

            byte[] dummyHeader = new byte[WaveSupport.WAV_HEADER_SIZE];
            dos.write(dummyHeader);

            var soundContext = new SoundContext(options.baseFreq);
            char[] chars = sanitizedText.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                char character = chars[i];
                var type = SYNTH_CONFIG.getOrDefault(character, new SynthConfig(CharacterType.UNKNOWM, (context) -> context.currentVowelShift, NO_PHONEME));

                // Berechnung der globalen Text-Position für die Satzmelodie (0.0 bis 1.0)
                double textProgress = (double) i / chars.length;

                addSampleForCharacter(options, character, type, soundContext, dos, textProgress);
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

    private static void addSampleForCharacter(VoiceOptions options, char character, SynthConfig config,
                                              SoundContext context, DataOutputStream dos, double textProgress) {
        int currentSamplesPerBlip = calculateSingleBlipLength(character, config, options);

        double targetVowelShift = config.targetWovelShift().apply(context);

        // Menschliche Intonationskurve: Tonhöhe fällt zum Satzende hin natürlich ab
        double intonation = MathUtil.fastSin(textProgress * Math.PI) * 0.15 - (textProgress * 0.1);
        double targetFrequency = calculateSegmentFrequency(options.baseFreq, options.pitchRandomness, config) * (1.0 + intonation);

        double segmentVolume = calculateSegmentVolume(config);

        for (int s = 0; s < currentSamplesPerBlip; s++) {
            // Extrem glatte Übergänge für organischen Redefluss
            context.currentVowelShift += (targetVowelShift - context.currentVowelShift) * 0.002;
            context.currentPitch += (targetFrequency - context.currentPitch) * 0.002;

            double globalTime = (double) context.totalSamplesWritten / AUDIO_FORMAT.getSampleRate();

            // Shimmer & Jitter: Winzige, unvorhersehbare Muskelunreinheiten der Stimme
            double jitter = (random.nextDouble() - 0.5) * 0.002;
            double vibrato = MathUtil.fastSin(2.0 * Math.PI * options.vibratoSpeed * globalTime) * options.vibratoDepth;

            double instantFreq = Math.clamp(context.currentPitch * (1.0 + (context.currentVowelShift - 1.0) * 0.20) * (1.0 + vibrato + jitter), 45.0, 350.0);
            context.runningPhase = (context.runningPhase + (2.0 * Math.PI * instantFreq) / AUDIO_FORMAT.getSampleRate()) % (2.0 * Math.PI);

            // Synthese und Hüllkurve gegen Knacken (Fade-In / Fade-Out pro Blip)
            double rawValue = config.phonemeCalculation().apply(context.runningPhase, options);
            double envelope = CharacterType.PUCTUATION == config.type() ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);

            // Menschliche Amplitudenschwankung (Atem)
            double breathShimmer = 1.0 + (MathUtil.fastSin(globalTime * 2.0) * 0.05);
            double preFilteredSample = Math.clamp(rawValue * envelope * segmentVolume * breathShimmer, -1.0, 1.0);

            // Tiefpassfilterung (Getrennte Zustände für Stereo-Breite, falls gewünscht)
            context.filterStateLeft += options.lowPassCutoff * (preFilteredSample - context.filterStateLeft);
            context.filterStateRight += options.lowPassCutoff * (preFilteredSample - context.filterStateRight);

            short pcmSampleLeft = (short) (context.filterStateLeft * 32767.0);
            short pcmSampleRight = (short) (context.filterStateRight * 32767.0);

            try {
                dos.writeByte(pcmSampleLeft & 0xFF);
                dos.writeByte((pcmSampleLeft >> 8) & 0xFF);
                dos.writeByte(pcmSampleRight & 0xFF);
                dos.writeByte((pcmSampleRight >> 8) & 0xFF);
            } catch (IOException ignored) {
            }

            context.totalSamplesWritten++;
        }
    }


    private static double applyClickProtectionEnvelope(int sampleIdx, int totalSamples) {
        int fadeSamples = Math.min(120, totalSamples / 10); // Sanftes Ein-/Ausblenden (ca. 2-3ms)
        if (sampleIdx < fadeSamples) return (double) sampleIdx / fadeSamples;
        if (sampleIdx > totalSamples - fadeSamples) return (double) (totalSamples - sampleIdx) / fadeSamples;
        return 1.0;
    }

    private static int calculateSingleBlipLength(char c, SynthConfig config, VoiceOptions options) {
        if (c == '.' || c == '!' || c == '?') {
            return (int) (AUDIO_FORMAT.getSampleRate() * options.sentencePause);
        }
        if (c == ',' || c == ';' || c == ':') {
            return (int) (AUDIO_FORMAT.getSampleRate() * options.commaPause);
        }
        if (c == ' ' || c == '-') {
            return (int) (AUDIO_FORMAT.getSampleRate() * options.wordPause);
        }

        int baseSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * options.baseBlipDuration);
        double deviation = (random.nextDouble() * 2.0 - 1.0) * options.speedRandomness;

        double speedModifier = 1.0 + deviation;
        if (config.type() == CharacterType.PLOSIVE) {
            speedModifier *= 0.4; // Kürzere Artikulation
        }

        return (int) (baseSamplesPerBlip * speedModifier);
    }

    private static double calculateSegmentFrequency(double baseFreq, double pitchRandomness, SynthConfig config) {
        if (CharacterType.PUCTUATION == config.type() || CharacterType.SPACE == config.type()) return baseFreq;
        double randomPitchOffset = (random.nextDouble() * 2.0 - 1.0) * pitchRandomness;
        return baseFreq * (1.0 + randomPitchOffset);
    }

    private static double calculateSegmentVolume(SynthConfig config) {
        if (CharacterType.PUCTUATION == config.type() || CharacterType.SPACE == config.type()) return 0.0;
        return (0.85 + random.nextDouble() * 0.15);
    }

}