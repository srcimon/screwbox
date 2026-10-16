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

public class MumbleSynthesizer {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 2, true, false);
    private static final Random random = new Random();

    private record CharConfig(CharacterType type, Double targetWovelShift) {

    }

    private static final Map<Character, CharConfig> TYPE_MAP = Map.ofEntries(
        Map.entry('A', new CharConfig(CharacterType.VOWEL, 0.92)),
        Map.entry('E', new CharConfig(CharacterType.VOWEL, 1.05)),
        Map.entry('I', new CharConfig(CharacterType.VOWEL, 1.12)),
        Map.entry('O', new CharConfig(CharacterType.VOWEL, 0.85)),
        Map.entry('U', new CharConfig(CharacterType.VOWEL, 0.75)),
        Map.entry('Ä', new CharConfig(CharacterType.VOWEL, 0.92)),
        Map.entry('Ö', new CharConfig(CharacterType.VOWEL, 0.85)),
        Map.entry('Ü', new CharConfig(CharacterType.VOWEL, 1.12)),

        Map.entry('P', new CharConfig(CharacterType.PLOSIVE, null)),
        Map.entry('T', new CharConfig(CharacterType.PLOSIVE, null)),
        Map.entry('K', new CharConfig(CharacterType.PLOSIVE, null)),
        Map.entry('B', new CharConfig(CharacterType.PLOSIVE, null)),
        Map.entry('D', new CharConfig(CharacterType.PLOSIVE, null)),
        Map.entry('G', new CharConfig(CharacterType.PLOSIVE, null)),

        Map.entry('M', new CharConfig(CharacterType.NASAL, 0.88)),
        Map.entry('N', new CharConfig(CharacterType.NASAL, 0.88)),
        Map.entry('L', new CharConfig(CharacterType.NASAL, 0.88)),
        Map.entry('W', new CharConfig(CharacterType.NASAL, 0.88)),
        Map.entry('R', new CharConfig(CharacterType.NASAL, 0.88)),

        Map.entry('.', new CharConfig(CharacterType.PUCTUATION, null)),
        Map.entry(',', new CharConfig(CharacterType.PUCTUATION, null)),
        Map.entry('!', new CharConfig(CharacterType.PUCTUATION, null)),
        Map.entry('?', new CharConfig(CharacterType.PUCTUATION, null)),
        Map.entry(';', new CharConfig(CharacterType.PUCTUATION, null)),
        Map.entry(':', new CharConfig(CharacterType.PUCTUATION, null)),
        Map.entry('-', new CharConfig(CharacterType.PUCTUATION, null))
    );

    private static CharConfig fetchConfig(char c) {
        return TYPE_MAP.getOrDefault(c, new CharConfig(CharacterType.UNKNOWM, null));
    }

    public enum WaveType {SINE, SQUARE, TRIANGLE}

    enum CharacterType {
        VOWEL,
        PLOSIVE,
        NASAL,
        PUCTUATION,
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
                var type = fetchConfig(character);

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

    private static void addSampleForCharacter(VoiceOptions options, char character, CharConfig config,
                                              SoundContext context, DataOutputStream dos, double textProgress) {
        int currentSamplesPerBlip = calculateSingleBlipLength(character, config, options);
        boolean isPunctuation = CharacterType.PUCTUATION == config.type();

        double targetVowelShift = config.targetWovelShift() == null
            ? context.currentVowelShift
            : config.targetWovelShift();

        // Menschliche Intonationskurve: Tonhöhe fällt zum Satzende hin natürlich ab
        double intonation = MathUtil.fastSin(textProgress * Math.PI) * 0.15 - (textProgress * 0.1);
        double targetFrequency = calculateSegmentFrequency(options.baseFreq, options.pitchRandomness, character, config) * (1.0 + intonation);

        double volumeModifier = calculateSegmentVolume(character, config);

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
            double rawValue = synthesizePhoneme(character, config, context.runningPhase, currentSamplesPerBlip, s, options);
            double envelope = isPunctuation ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);

            // Menschliche Amplitudenschwankung (Atem)
            double breathShimmer = 1.0 + (MathUtil.fastSin(globalTime * 2.0) * 0.05);
            double preFilteredSample = Math.clamp(rawValue * envelope * volumeModifier * breathShimmer, -1.0, 1.0);

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

    private static int calculateSingleBlipLength(char c, CharConfig config, VoiceOptions options) {
        if (c == '.' || c == '!' || c == '?') return (int) (AUDIO_FORMAT.getSampleRate() * options.sentencePause);
        if (c == ',' || c == ';' || c == ':') return (int) (AUDIO_FORMAT.getSampleRate() * options.commaPause);
        if (c == ' ' || c == '-') return (int) (AUDIO_FORMAT.getSampleRate() * options.wordPause);

        int baseSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * options.baseBlipDuration);
        double deviation = (random.nextDouble() * 2.0 - 1.0) * options.speedRandomness;

        double speedModifier = 1.0 + deviation;
        if (config.type() == CharacterType.PLOSIVE) speedModifier *= 0.4; // Kürzere Artikulation

        return (int) (baseSamplesPerBlip * speedModifier);
    }

    private static double calculateSegmentFrequency(double baseFreq, double pitchRandomness, char c, CharConfig config) {
        if (CharacterType.PUCTUATION == config.type() || c == ' ') return baseFreq;
        double randomPitchOffset = (random.nextDouble() * 2.0 - 1.0) * pitchRandomness;
        return baseFreq * (1.0 + randomPitchOffset);
    }

    private static double calculateSegmentVolume(char c, CharConfig config) {
        if (CharacterType.PUCTUATION == config.type() || c == ' ') return 0.0;
        return (0.85 + random.nextDouble() * 0.15);
    }

    private record FormantMult(double f1, double f2) {
    }

    private static final FormantMult DEFAULT_MULT = new FormantMult(1.0, 2.0);
    private static final Map<Character, FormantMult> VOWEL_MAP = Map.of(//TODO Combine with other map
        'A', new FormantMult(2.0, 3.5),
        'E', new FormantMult(1.5, 4.0),
        'I', new FormantMult(1.2, 4.5),
        'O', new FormantMult(1.4, 2.4),
        'U', new FormantMult(1.1, 1.8),
        'Ä', new FormantMult(1.8, 4.0),
        'Ö', new FormantMult(1.5, 3.2),
        'Ü', new FormantMult(1.2, 3.5)
    );

    private static double synthesizePhoneme(char c, CharConfig config, double phase, int totalSamples, int currentSample, VoiceOptions options) {

        if (CharacterType.PUCTUATION == config.type() || c == ' ') {
            return 0.0;//TODO TYPE SPACE?
        }

        if (CharacterType.VOWEL == config.type()) {
            double baseWave = generateBaseWave(phase, options.waveType);

            // Schneller Lookup statt komplexem Switch mit Seiteneffekten
            FormantMult mult = VOWEL_MAP.getOrDefault(c, DEFAULT_MULT);

            // Harmonische Obertöne sehr subtil beimischen (Verhindert Klingeln im Ohr)
            double formant1 = MathUtil.fastSin(phase * mult.f1()) * 0.25;
            double formant2 = MathUtil.fastSin(phase * mult.f2()) * 0.10;

            return MathUtil.fastSin((baseWave + formant1 + formant2) * 0.8) * options.vowelGain;
        }

        if (CharacterType.PLOSIVE == config.type()) {
            if (currentSample < totalSamples * 0.10) {
                // Sehr tiefer, sanfter Pop-Laut ohne Klickgeräusch
                return MathUtil.fastSin(phase * 0.08) * Math.exp(-currentSample / 120.0) * options.plosiveGain;
            }
            return 0.0;
        }

        if (CharacterType.NASAL == config.type()) {
            return MathUtil.fastSin(phase * 0.5) * options.nasalGain;
        }
        return 0.0;
    }

    private static double generateBaseWave(double phase, WaveType waveType) {
        return switch (waveType) {
            case SINE -> MathUtil.fastSin(phase);
            // Ultra-weiche Annäherung einer Dreieckswelle für wärmeren Sound
            case SQUARE -> MathUtil.fastSin(MathUtil.fastSin(phase) * 1.2) * 0.8;
            case TRIANGLE -> (Math.abs((phase % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
        };
    }


}