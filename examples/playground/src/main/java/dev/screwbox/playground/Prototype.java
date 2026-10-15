package dev.screwbox.playground;

import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.utils.MathUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Prototype {

    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 2; // Stereo

    private final static Random random = new Random();

    private static final int BYTES_PER_SAMPLE = BITS_PER_SAMPLE / 8;
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

        public WaveType waveType;

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

    public static Sound createMumbleSound(final String text, final VoiceOptions options) {
        String sanitizedText = text.toUpperCase();

        try (final var baos = new ByteArrayOutputStream()) {
            DataOutputStream dos = new DataOutputStream(baos);

            var soundContext = new SoundContext(options.baseFreq);

            for (char character : sanitizedText.toCharArray()) {
                addSampleForChaacter(options, character, soundContext, dos);
            }

            byte[] audioData = baos.toByteArray();
            byte[] finalWav = new byte[WAV_HEADER_SIZE + audioData.length];

            ByteBuffer headerBuffer = ByteBuffer.wrap(finalWav).order(ByteOrder.LITTLE_ENDIAN);
            writeWavHeader(headerBuffer, audioData.length);
            System.arraycopy(audioData, 0, finalWav, WAV_HEADER_SIZE, audioData.length);
            return Sound.fromSoundData(finalWav);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addSampleForChaacter(VoiceOptions config, char character, SoundContext context, DataOutputStream dos) {
        int currentSamplesPerBlip = calculateSingleBlipLength(character, config);
        boolean isPunctuation = isPunctuation(character);

        double targetVowelShift = determineTargetVowelShift(character, context.currentVowelShift);
        // Stark geglättete Pitch-Änderungen pro Segment, um Frequenzsprünge zu mindern
        double targetFrequency = calculateSegmentFrequency(config.baseFreq, config.pitchRandomness, character);
        double volumeModifier = calculateSegmentVolume(character);


        for (int s = 0; s < currentSamplesPerBlip; s++) {
            // Übergänge verlangsamt (0.001 statt 0.003), um Knackser bei Vokalwechseln zu eliminieren
            context.currentVowelShift += (targetVowelShift - context.currentVowelShift) * 0.001;
            context.currentPitch += (targetFrequency - context.currentPitch) * 0.001;

            // Wichtig: Da wir zwei Stereo-Frames pro Loop schreiben, entspricht globalTime weiterhin
            // den geschriebenen Samples dividiert durch die Sample-Rate
            double globalTime = (double) context.totalSamplesWritten / SAMPLE_RATE;
            double vibrato = MathUtil.fastSin(2.0 * Math.PI * config.vibratoSpeed * globalTime) * config.vibratoDepth;
            double instantFreq = Math.clamp(context.currentPitch * (1.0 + (context.currentVowelShift - 1.0) * 0.25) * (1.0 + vibrato), 40.0, 400.0);

            context.runningPhase = (context.runningPhase + (2.0 * Math.PI * instantFreq) / SAMPLE_RATE) % (2.0 * Math.PI);

            double rawValue = synthesizePhoneme(character, context.runningPhase, currentSamplesPerBlip, s, config);
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

    private static int calculateSingleBlipLength(char c, VoiceOptions config) {
        if (c == '.' || c == '!' || c == '?') return (int) (SAMPLE_RATE * config.sentencePause);
        if (c == ',' || c == ';' || c == ':') return (int) (SAMPLE_RATE * config.commaPause);
        if (c == ' ' || c == '-') return (int) (SAMPLE_RATE * config.wordPause);

        int baseSamplesPerBlip = (int) (SAMPLE_RATE * config.baseBlipDuration);
        double deviation = (random.nextDouble() * 2.0 - 1.0) * config.speedRandomness;
        double speedModifier = 1.0 + deviation;

        if (isPlosive(c)) {
            speedModifier *= 0.35;
        }
        return (int) (baseSamplesPerBlip * speedModifier);
    }

    private static double determineTargetVowelShift(char c, double fallbackValue) {
        return switch (c) {
            case 'A' -> 0.95;
            case 'E' -> 1.02;
            case 'I' -> 1.08;
            case 'O' -> 0.88;
            case 'U' -> 0.78;
            default -> isNasal(c) ? 0.85 : fallbackValue;
        };
    }

    private static double calculateSegmentFrequency(double baseFreq, double pitchRandomness, char c) {
        if (isPunctuation(c) || c == ' ') return baseFreq;
        double randomPitchOffset = (random.nextDouble() * 2.0 - 1.0) * pitchRandomness;
        return baseFreq * (1.0 + randomPitchOffset);
    }

    private static double calculateSegmentVolume(char c) {
        if (isPunctuation(c) || c == ' ') return 0.0;
        return (0.85 + random.nextDouble() * 0.15);
    }

    private static double synthesizePhoneme(char c, double phase, int totalSamples, int currentSample, VoiceOptions config) {
        if (isPunctuation(c) || c == ' ') return 0.0;

        if (isVowel(c)) {
            double baseWave = generateBaseWave(phase, config.waveType);

            double f1Mult = 1.0;
            double f2Mult = switch (c) {
                case 'A' -> {
                    f1Mult = 2.0;
                    yield 3.5;
                }
                case 'E' -> {
                    f1Mult = 1.5;
                    yield 4.0;
                }
                case 'I' -> {
                    f1Mult = 1.2;
                    yield 4.5;
                }
                case 'O' -> {
                    f1Mult = 1.4;
                    yield 2.4;
                }
                case 'U' -> {
                    f1Mult = 1.1;
                    yield 1.8;
                }
                case 'Ä' -> {
                    f1Mult = 1.8;
                    yield 4.0;
                }
                case 'Ö' -> {
                    f1Mult = 1.5;
                    yield 3.2;
                }
                case 'Ü' -> {
                    f1Mult = 1.2;
                    yield 3.5;
                }
                default -> 2.0;
            };

            // Harmonische Obertöne sehr subtil beimischen (Verhindert Klingeln im Ohr)
            double formant1 = MathUtil.fastSin(phase * f1Mult) * 0.25;
            double formant2 = MathUtil.fastSin(phase * f2Mult) * 0.10;

            return Math.sin((baseWave + formant1 + formant2) * 0.8) * config.vowelGain;
        }

        if (isPlosive(c)) {
            if (currentSample < totalSamples * 0.10) {
                // Sehr tiefer, sanfter Pop-Laut ohne Klickgeräusch
                return MathUtil.fastSin(phase * 0.08) * Math.exp(-currentSample / 120.0) * config.plosiveGain;
            }
            return 0.0;
        }

        if (isNasal(c)) {
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

    private static boolean isVowel(char c) {
        return switch (c) {
            case 'A', 'E', 'I', 'O', 'U', 'Ä', 'Ö', 'Ü' -> true;
            default -> false;
        };
    }

    private static boolean isPlosive(char c) {
        return switch (c) {
            case 'P', 'T', 'K', 'B', 'D', 'G' -> true;
            default -> false;
        };
    }

    private static boolean isNasal(char c) {
        return switch (c) {
            case 'M', 'N', 'L', 'W', 'R' -> true;
            default -> false;
        };
    }

    private static boolean isPunctuation(char c) {
        return switch (c) {
            case '.', ',', '!', '?', ';', ':', '-' -> true;
            default -> false;
        };
    }

    private static void writeWavHeader(ByteBuffer buffer, int audioDataLength) {
        buffer.put("RIFF".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(WAV_HEADER_SIZE - 8 + audioDataLength);
        buffer.put("WAVE".getBytes(StandardCharsets.US_ASCII));
        buffer.put("fmt ".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(16); // Subchunk1Size
        buffer.putShort((short) 1); // AudioFormat (PCM)
        buffer.putShort((short) CHANNELS); // Stereo
        buffer.putInt(SAMPLE_RATE);
        buffer.putInt(SAMPLE_RATE * CHANNELS * BYTES_PER_SAMPLE); // ByteRate
        buffer.putShort((short) (CHANNELS * BYTES_PER_SAMPLE)); // BlockAlign
        buffer.putShort((short) BITS_PER_SAMPLE);
        buffer.put("data".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(audioDataLength);
    }
}