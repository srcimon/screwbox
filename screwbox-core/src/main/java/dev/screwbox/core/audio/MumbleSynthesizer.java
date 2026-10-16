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

    private final static Random random = new Random();

    private static final int WAV_HEADER_SIZE = 44;

    public enum WaveType {
        SINE, SQUARE, TRIANGLE
    }

    public static class VoiceOptions {
        public double baseFreq = 110.0;
        public double baseBlipDuration = 0.08;
        public double pitchRandomness = 0.15;
        public double speedRandomness = 0.25;
        public double vibratoSpeed = 5.5;
        public double vibratoDepth = 0.025;

        public double sentencePause = 0.45;
        public double commaPause = 0.22;
        public double wordPause = 0.15;

        public WaveType waveType = WaveType.SINE;

        public double vowelGain = 0.75;
        public double nasalGain = 0.40;
        public double plosiveGain = 0.35;

        // Filter-Koeffizient: 1.0 = ungefiltert, 0.1 = extrem dumpf/warm.
        // 0.25 ist der Sweet-Spot für eine klare, aber absolut kratzfreie Stimme.
        public double lowPassCutoff = 0.15;

    }

    private static class SoundContext {
        double runningPhase = 0.0;
        double currentPitch;
        double currentVowelShift = 1.0;
        int totalSamplesWritten = 0;

        // Zustandsspeicher für den Tiefpassfilter
        double filterState = 0.0;

        public SoundContext(double startPitch) {
            currentPitch = startPitch;
        }
    }

    public static Sound synthesizeMumbleSpeech(final String text, final VoiceOptions options) {
        String sanitizedText = text.toUpperCase();

        // Verwende ein anpassbares ByteArrayOutputStream, um direkt auf den internen Puffer zuzugreifen
        try (final var baos = new ByteArrayOutputStream(4096);
            DataOutputStream dos = new DataOutputStream(baos)) {

            // 1. Platzhalter für den 44-Byte-Header schreiben
            byte[] dummyHeader = new byte[WAV_HEADER_SIZE];
            dos.write(dummyHeader);

            var soundContext = new SoundContext(options.baseFreq);
            for (char character : sanitizedText.toCharArray()) {
                var type = fetchType(character);
                addSampleForChaacter(options, character, type, soundContext, dos);//TODO add type to context?
            }
            dos.flush();

            byte[] finalWav = baos.toByteArray();
            int audioDataLength = finalWav.length - WAV_HEADER_SIZE;

            // 3. Den Header nachträglich direkt im finalen Array korrigieren
            ByteBuffer headerBuffer = ByteBuffer.wrap(finalWav).order(ByteOrder.LITTLE_ENDIAN);
            writeWavHeader(headerBuffer, audioDataLength, AUDIO_FORMAT);

            return Sound.fromSoundData(finalWav);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addSampleForChaacter(VoiceOptions config, char character, CharacterType type, SoundContext context, DataOutputStream dos) {
        int currentSamplesPerBlip = calculateSingleBlipLength(character, type, config);
        boolean isPunctuation = CharacterType.PUCTUATION == type;

        double targetVowelShift = determineTargetVowelShift(character, context.currentVowelShift, type);
        // Stark geglättete Pitch-Änderungen pro Segment, um Frequenzsprünge zu mindern
        double targetFrequency = calculateSegmentFrequency(config.baseFreq, config.pitchRandomness, character, type);
        double volumeModifier = calculateSegmentVolume(character, type);


        for (int s = 0; s < currentSamplesPerBlip; s++) {
            // Übergänge verlangsamt (0.001 statt 0.003), um Knackser bei Vokalwechseln zu eliminieren
            context.currentVowelShift += (targetVowelShift - context.currentVowelShift) * 0.001;
            context.currentPitch += (targetFrequency - context.currentPitch) * 0.001;

            // Wichtig: Da wir zwei Stereo-Frames pro Loop schreiben, entspricht globalTime weiterhin
            // den geschriebenen Samples dividiert durch die Sample-Rate
            double globalTime = (double) context.totalSamplesWritten / AUDIO_FORMAT.getSampleRate();
            double vibrato = MathUtil.fastSin(2.0 * Math.PI * config.vibratoSpeed * globalTime) * config.vibratoDepth;
            double instantFreq = Math.clamp(context.currentPitch * (1.0 + (context.currentVowelShift - 1.0) * 0.25) * (1.0 + vibrato), 40.0, 400.0);

            context.runningPhase = (context.runningPhase + (2.0 * Math.PI * instantFreq) / AUDIO_FORMAT.getSampleRate()) % (2.0 * Math.PI);

            double rawValue = synthesizePhoneme(character, type, context.runningPhase, currentSamplesPerBlip, s, config);
            double envelope = isPunctuation ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);
            double preFilteredSample = Math.clamp(rawValue * envelope * volumeModifier, -1.0, 1.0);

            // TIEFPASSFILTER (One-Pole IIR Filter): Glättet das Signal in Echtzeit
            context.filterState += config.lowPassCutoff * (preFilteredSample - context.filterState);
            double finalSampleValue = context.filterState;

            // Kanäle trennen und Stereo-Lautstärke anwenden
            short pcmSampleLeft = (short) (finalSampleValue * 32767.0);
            short pcmSampleRight = (short) (finalSampleValue * 32767.0);

            try {
                // Linker Kanal (Zuerst)
                dos.writeByte(pcmSampleLeft & 0xFF);
                dos.writeByte((pcmSampleLeft >> 8) & 0xFF);

                // Rechter Kanal (Danach)
                dos.writeByte(pcmSampleRight & 0xFF);
                dos.writeByte((pcmSampleRight >> 8) & 0xFF);
            } catch (IOException ignored) {
            }

            context.totalSamplesWritten++;
        }
    }

    private static int calculateSingleBlipLength(char c, CharacterType type, VoiceOptions config) {
        if (c == '.' || c == '!' || c == '?' ) return (int) (AUDIO_FORMAT.getSampleRate() * config.sentencePause);
        if (c == ',' || c == ';' || c == ':' ) return (int) (AUDIO_FORMAT.getSampleRate() * config.commaPause);
        if (c == ' ' || c == '-' ) return (int) (AUDIO_FORMAT.getSampleRate() * config.wordPause);

        int baseSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * config.baseBlipDuration);
        double deviation = (random.nextDouble() * 2.0 - 1.0) * config.speedRandomness;
        double speedModifier = 1.0 + deviation;

        if (CharacterType.PLOSIVE == type) {
            speedModifier *= 0.35;
        }
        return (int) (baseSamplesPerBlip * speedModifier);
    }

    private static double determineTargetVowelShift(char c, double fallbackValue, CharacterType type) {
        return switch (c) {
            case 'A' -> 0.95;
            case 'E' -> 1.02;
            case 'I' -> 1.08;
            case 'O' -> 0.88;
            case 'U' -> 0.78;
            default -> CharacterType.NASAL == type ? 0.85 : fallbackValue;
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
    private static final Map<Character, FormantMult> VOWEL_MAP = Map.of(
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

            return Math.sin((baseWave + formant1 + formant2) * 0.8) * config.vowelGain;
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

    private static double applyClickProtectionEnvelope(int sampleIndex, int totalSamples) {
        // Größeres Fade-Fenster (12% statt 10%), um die Zeichen extrem sanft ineinander übergehen zu lassen
        int fadeSamples = Math.min(220, totalSamples / 8);
        if (sampleIndex < fadeSamples) return sampleIndex / (double) fadeSamples;
        if (sampleIndex > totalSamples - fadeSamples) return (totalSamples - sampleIndex) / (double) fadeSamples;
        return 1.0;
    }

    private static double generateBaseWave(double phase, WaveType waveType) {
        return switch (waveType) {
            case SINE -> MathUtil.fastSin(phase);
            // Ultra-weiche Annäherung einer Dreieckswelle für wärmeren Sound
            case SQUARE -> Math.sin(MathUtil.fastSin(phase) * 1.2) * 0.8;
            case TRIANGLE -> (Math.abs((phase % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
        };
    }

    enum CharacterType {
        VOWEL,
        PLOSIVE,
        NASAL,
        PUCTUATION,
        UNKNOWM
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

    private static void writeWavHeader(ByteBuffer buffer, int audioDataLength, AudioFormat format) {
        // 1. Berechnungen basierend auf dem AudioFormat
        int channels = format.getChannels();
        int sampleRate = (int) format.getSampleRate();
        int bitsPerSample = format.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;

        int byteRate = sampleRate * channels * bytesPerSample;
        short blockAlign = (short) (channels * bytesPerSample);

        // 2. WAV-Header schreiben
        buffer.put("RIFF".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(44 - 8 + audioDataLength); // 44 ist die Standard-WAV-Header-Größe
        buffer.put("WAVE".getBytes(StandardCharsets.US_ASCII));
        buffer.put("fmt ".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(16); // Subchunk1Size für PCM
        buffer.putShort((short) 1); // AudioFormat (1 = unkomprimiertes PCM)
        buffer.putShort((short) channels);
        buffer.putInt(sampleRate);
        buffer.putInt(byteRate);
        buffer.putShort(blockAlign);
        buffer.putShort((short) bitsPerSample);
        buffer.put("data".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(audioDataLength);
    }
}