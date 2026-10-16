package dev.screwbox.core.audio;

import dev.screwbox.core.utils.MathUtil;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class MumbleSynthesizer {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 2, true, false);
    private static final Random random = new Random();
    private static final int WAV_HEADER_SIZE = 44;

    public enum WaveType { SINE, SQUARE, TRIANGLE }
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

        // Letztes Sample für Crossfading, um Knackser komplett zu verhindern
        double lastSampleValue = 0.0;

        public SoundContext(double startPitch) {
            currentPitch = startPitch;
        }
    }

    public static Sound synthesizeMumbleSpeech(final String text, final VoiceOptions options) {
        String sanitizedText = text.toUpperCase();

        try (final var baos = new ByteArrayOutputStream(8192);
            DataOutputStream dos = new DataOutputStream(baos)) {

            byte[] dummyHeader = new byte[WAV_HEADER_SIZE];
            dos.write(dummyHeader);

            var soundContext = new SoundContext(options.baseFreq);
            char[] chars = sanitizedText.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                char character = chars[i];
                var type = fetchType(character);

                // Berechnung der globalen Text-Position für die Satzmelodie (0.0 bis 1.0)
                double textProgress = (double) i / chars.length;

                addSampleForCharacter(options, character, type, soundContext, dos, textProgress);
            }
            dos.flush();

            byte[] finalWav = baos.toByteArray();
            int audioDataLength = finalWav.length - WAV_HEADER_SIZE;

            ByteBuffer headerBuffer = ByteBuffer.wrap(finalWav).order(ByteOrder.LITTLE_ENDIAN);
            writeWavHeader(headerBuffer, audioDataLength);

            return Sound.fromSoundData(finalWav);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addSampleForCharacter(VoiceOptions config, char character, CharacterType type,
                                              SoundContext context, DataOutputStream dos, double textProgress) {
        int currentSamplesPerBlip = calculateSingleBlipLength(character, type, config);
        boolean isPunctuation = CharacterType.PUCTUATION == type;

        double targetVowelShift = determineTargetVowelShift(character, context.currentVowelShift, type);

        // Menschliche Intonationskurve: Tonhöhe fällt zum Satzende hin natürlich ab
        double intonation = MathUtil.fastSin(textProgress * Math.PI) * 0.15 - (textProgress * 0.1);
        double targetFrequency = calculateSegmentFrequency(config.baseFreq, config.pitchRandomness, character, type) * (1.0 + intonation);

        double volumeModifier = calculateSegmentVolume(character, type);

        for (int s = 0; s < currentSamplesPerBlip; s++) {
            // Extrem glatte Übergänge für organischen Redefluss
            context.currentVowelShift += (targetVowelShift - context.currentVowelShift) * 0.002;
            context.currentPitch += (targetFrequency - context.currentPitch) * 0.002;

            double globalTime = (double) context.totalSamplesWritten / AUDIO_FORMAT.getSampleRate();

            // Shimmer & Jitter: Winzige, unvorhersehbare Muskelunreinheiten der Stimme
            double jitter = (random.nextDouble() - 0.5) * 0.002;
            double vibrato = MathUtil.fastSin(2.0 * Math.PI * config.vibratoSpeed * globalTime) * config.vibratoDepth;

            double instantFreq = Math.clamp(context.currentPitch * (1.0 + (context.currentVowelShift - 1.0) * 0.20) * (1.0 + vibrato + jitter), 45.0, 350.0);
            context.runningPhase = (context.runningPhase + (2.0 * Math.PI * instantFreq) / AUDIO_FORMAT.getSampleRate()) % (2.0 * Math.PI);

            // Synthese und Hüllkurve gegen Knacken (Fade-In / Fade-Out pro Blip)
            double rawValue = synthesizePhoneme(character, type, context.runningPhase, currentSamplesPerBlip, s, config);
            double envelope = isPunctuation ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);

            // Menschliche Amplitudenschwankung (Atem)
            double breathShimmer = 1.0 + (MathUtil.fastSin(globalTime * 2.0) * 0.05);
            double preFilteredSample = Math.clamp(rawValue * envelope * volumeModifier * breathShimmer, -1.0, 1.0);

            // Tiefpassfilterung (Getrennte Zustände für Stereo-Breite, falls gewünscht)
            context.filterStateLeft += config.lowPassCutoff * (preFilteredSample - context.filterStateLeft);
            context.filterStateRight += config.lowPassCutoff * (preFilteredSample - context.filterStateRight);

            short pcmSampleLeft = (short) (context.filterStateLeft * 32767.0);
            short pcmSampleRight = (short) (context.filterStateRight * 32767.0);

            try {
                dos.writeByte(pcmSampleLeft & 0xFF);
                dos.writeByte((pcmSampleLeft >> 8) & 0xFF);
                dos.writeByte(pcmSampleRight & 0xFF);
                dos.writeByte((pcmSampleRight >> 8) & 0xFF);
            } catch (IOException ignored) {}

            context.totalSamplesWritten++;
        }
    }


    private static double applyClickProtectionEnvelope(int sampleIdx, int totalSamples) {
        int fadeSamples = Math.min(120, totalSamples / 10); // Sanftes Ein-/Ausblenden (ca. 2-3ms)
        if (sampleIdx < fadeSamples) return (double) sampleIdx / fadeSamples;
        if (sampleIdx > totalSamples - fadeSamples) return (double) (totalSamples - sampleIdx) / fadeSamples;
        return 1.0;
    }

    private static int calculateSingleBlipLength(char c, CharacterType type, VoiceOptions config) {
        if (c == '.' || c == '!' || c == '?') return (int) (AUDIO_FORMAT.getSampleRate() * config.sentencePause);
        if (c == ',' || c == ';' || c == ':') return (int) (AUDIO_FORMAT.getSampleRate() * config.commaPause);
        if (c == ' ' || c == '-') return (int) (AUDIO_FORMAT.getSampleRate() * config.wordPause);

        int baseSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * config.baseBlipDuration);
        double deviation = (random.nextDouble() * 2.0 - 1.0) * config.speedRandomness;

        double speedModifier = 1.0 + deviation;
        if (type == CharacterType.PLOSIVE) speedModifier *= 0.4; // Kürzere Artikulation

        return (int) (baseSamplesPerBlip * speedModifier);
    }

    private static double determineTargetVowelShift(char c, double fallbackValue, CharacterType type) {
        return switch (c) {
            case 'A', 'Ä' -> 0.92;
            case 'E' -> 1.05;
            case 'I', 'Ü' -> 1.12;
            case 'O', 'Ö' -> 0.85;
            case 'U' -> 0.75;
            default -> type == CharacterType.NASAL ? 0.88 : fallbackValue;
        };
    }

    private static double calculateSegmentFrequency(double baseFreq, double pitchRandomness, char c, CharacterType type) {
        if (CharacterType.PUCTUATION == type || c == ' ' ) return baseFreq;
        double randomPitchOffset = (random.nextDouble() * 2.0 - 1.0) * pitchRandomness;
        return baseFreq * (1.0 + randomPitchOffset);
    }

    private static double calculateSegmentVolume(char c, CharacterType type) {
        if (CharacterType.PUCTUATION == type || c == ' ' ) return 0.0;
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

    private static double synthesizePhoneme(char c, CharacterType type, double phase, int totalSamples, int currentSample, VoiceOptions config) {

        if (CharacterType.PUCTUATION == type || c == ' ' ) return 0.0;//TODO TYPE SPACE?

        if (CharacterType.VOWEL == type) {
            double baseWave = generateBaseWave(phase, config.waveType);

            // Schneller Lookup statt komplexem Switch mit Seiteneffekten
            FormantMult mult = VOWEL_MAP.getOrDefault(c, DEFAULT_MULT);

            // Harmonische Obertöne sehr subtil beimischen (Verhindert Klingeln im Ohr)
            double formant1 = MathUtil.fastSin(phase * mult.f1()) * 0.25;
            double formant2 = MathUtil.fastSin(phase * mult.f2()) * 0.10;

            return MathUtil.fastSin((baseWave + formant1 + formant2) * 0.8) * config.vowelGain;
        }

        if (CharacterType.PLOSIVE == type) {
            if (currentSample < totalSamples * 0.10) {
                // Sehr tiefer, sanfter Pop-Laut ohne Klickgeräusch
                return MathUtil.fastSin(phase * 0.08) * Math.exp(-currentSample / 120.0) * config.plosiveGain;
            }
            return 0.0;
        }

        if (CharacterType.NASAL == type) {
            return MathUtil.fastSin(phase * 0.5) * config.nasalGain;
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



    private static final Map<Character, CharacterType> TYPE_MAP = Map.ofEntries(
        Map.entry('A', CharacterType.VOWEL),
        Map.entry('E', CharacterType.VOWEL),
        Map.entry('I', CharacterType.VOWEL),
        Map.entry('O', CharacterType.VOWEL),
        Map.entry('U', CharacterType.VOWEL),
        Map.entry('Ä', CharacterType.VOWEL),
        Map.entry('Ö', CharacterType.VOWEL),
        Map.entry('Ü', CharacterType.VOWEL),

        Map.entry('P', CharacterType.PLOSIVE),
        Map.entry('T', CharacterType.PLOSIVE),
        Map.entry('K', CharacterType.PLOSIVE),
        Map.entry('B', CharacterType.PLOSIVE),
        Map.entry('D', CharacterType.PLOSIVE),
        Map.entry('G', CharacterType.PLOSIVE),

        Map.entry('M', CharacterType.NASAL),
        Map.entry('N', CharacterType.NASAL),
        Map.entry('L', CharacterType.NASAL),
        Map.entry('W', CharacterType.NASAL),
        Map.entry('R', CharacterType.NASAL),

        Map.entry('.', CharacterType.PUCTUATION),
        Map.entry(',', CharacterType.PUCTUATION),
        Map.entry('!', CharacterType.PUCTUATION),
        Map.entry('?', CharacterType.PUCTUATION),
        Map.entry(';', CharacterType.PUCTUATION),
        Map.entry(':', CharacterType.PUCTUATION),
        Map.entry('-', CharacterType.PUCTUATION)
    );

    private static CharacterType fetchType(char c) {
        return TYPE_MAP.getOrDefault(c, CharacterType.UNKNOWM);
    }

    private static void writeWavHeader(ByteBuffer buffer, int audioDataLength) {
        int sampleRate = (int) AUDIO_FORMAT.getSampleRate();
        int bitsPerSample = AUDIO_FORMAT.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;

        buffer.put("RIFF".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(44 - 8 + audioDataLength); // 44 ist die Standard-WAV-Header-Größe
        buffer.put("WAVE".getBytes(StandardCharsets.US_ASCII));
        buffer.put("fmt ".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(16); // Subchunk1Size für PCM
        buffer.putShort((short) 1); // AudioFormat (1 = unkomprimiertes PCM)
        buffer.putShort((short) AUDIO_FORMAT.getChannels());
        buffer.putInt(sampleRate);
        buffer.putInt(sampleRate * AUDIO_FORMAT.getChannels() * bytesPerSample);
        buffer.putShort((short) (AUDIO_FORMAT.getChannels() * bytesPerSample));
        buffer.putShort((short) bitsPerSample);
        buffer.put("data".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(audioDataLength);
    }
}