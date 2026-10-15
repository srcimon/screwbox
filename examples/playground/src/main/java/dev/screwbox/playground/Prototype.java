package dev.screwbox.playground;

import dev.screwbox.core.utils.MathUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Prototype {

    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 1; // Mono

    private final static Random random = new Random();

    private static final int BYTES_PER_SAMPLE = BITS_PER_SAMPLE / 8;
    private static final int WAV_HEADER_SIZE = 44;

    public enum WaveType {
        SINE, SQUARE, TRIANGLE
    }

    public static class VoiceConfig {
        public double baseFreq = 110.0;
        public double baseBlipDuration = 0.08;
        public double pitchRandomness = 0.15;
        public double speedRandomness = 0.25;
        public double vibratoSpeed = 5.5;
        public double vibratoDepth = 0.025;

        public double sentencePause = 0.45;
        public double commaPause = 0.22;
        public double wordPause = 0.15;

        public double vowelGain = 0.75;
        public double nasalGain = 0.40;
        public double plosiveGain = 0.35;
    }

    private static class SoundContext {
        double runningPhase = 0.0;
        double currentPitch;
        double currentVowelShift = 1.0;
        int totalSamplesWritten = 0; // Ersetzt buffer.position() für zeitbasierte Effekte

        public SoundContext(double startPitch) {
            currentPitch = startPitch;
        }
    }

    public static byte[] generateMumbleWavByteArray(String text, WaveType waveType, VoiceConfig config) {
        String sanitizedText = text.toUpperCase();

        // Dynamischer Stream statt festem ByteBuffer
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        var context = new SoundContext(config.baseFreq);

        for (int i = 0; i < sanitizedText.length(); i++) {
            char character = sanitizedText.charAt(i);

            // Jedes Zeichen berechnet seine Länge direkt JETZT
            int currentSamplesPerBlip = calculateSingleBlipLength(character, config);
            boolean isPunctuation = isPunctuation(character);

            double targetVowelShift = determineTargetVowelShift(character, context.currentVowelShift);
            double targetFrequency = calculateSegmentFrequency(config.baseFreq, config.pitchRandomness, character);
            double volumeModifier = calculateSegmentVolume(character);

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                context.currentVowelShift += (targetVowelShift - context.currentVowelShift) * 0.003;
                context.currentPitch += (targetFrequency - context.currentPitch) * 0.002;

                // Globale Zeitberechnung über die absolut geschriebenen Samples
                double globalTime = (double) context.totalSamplesWritten / SAMPLE_RATE;
                double vibrato = MathUtil.fastSin(2.0 * Math.PI * config.vibratoSpeed * globalTime) * config.vibratoDepth;
                double instantFreq = Math.clamp(context.currentPitch * (1.0 + (context.currentVowelShift - 1.0) * 0.3) * (1.0 + vibrato), 40.0, 400.0);

                context.runningPhase = (context.runningPhase + (2.0 * Math.PI * instantFreq) / SAMPLE_RATE) % (2.0 * Math.PI);

                double rawValue = synthesizePhoneme(character, context.runningPhase, currentSamplesPerBlip, s, waveType, config);
                double envelope = isPunctuation ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);
                double sampleValue = Math.clamp(rawValue * envelope * volumeModifier, -1.0, 1.0);

                // Im Little-Endian Format in den Stream schreiben
                short pcmSample = (short) (sampleValue * 32767.0);
                try {
                    dos.writeByte(pcmSample & 0xFF);
                    dos.writeByte((pcmSample >> 8) & 0xFF);
                } catch (IOException ignored) {}

                context.totalSamplesWritten++;
            }
        }

        // Ergebnis-Array holen und den echten WAV-Header davor klatschen
        byte[] audioData = baos.toByteArray();
        byte[] finalWav = new byte[WAV_HEADER_SIZE + audioData.length];

        ByteBuffer headerBuffer = ByteBuffer.wrap(finalWav).order(ByteOrder.LITTLE_ENDIAN);
        writeWavHeader(headerBuffer, audioData.length);

        System.arraycopy(audioData, 0, finalWav, WAV_HEADER_SIZE, audioData.length);
        return finalWav;
    }

    // Isoliert die Längenberechnung für genau EIN Zeichen
    private static int calculateSingleBlipLength(char c, VoiceConfig config) {
        if (".!?".contains(String.valueOf(c))) return (int) (SAMPLE_RATE * config.sentencePause);
        if (",;:".contains(String.valueOf(c))) return (int) (SAMPLE_RATE * config.commaPause);
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
        if (isVowel(c)) {
            return switch (c) {
                case 'A' -> 0.95;
                case 'E' -> 1.05;
                case 'I' -> 1.15;
                case 'O' -> 0.85;
                case 'U' -> 0.75;
                default -> fallbackValue;
            };
        } else if (isNasal(c)) {
            return 0.85;
        }
        return fallbackValue;
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

    private static double synthesizePhoneme(char c, double phase, int totalSamples, int currentSample, WaveType waveType, VoiceConfig config) {
        if (isPunctuation(c) || c == ' ') return 0.0;
        if (isVowel(c)) return fastTanh(generateBaseWave(phase, waveType) * 1.2) * config.vowelGain;

        if (isPlosive(c)) {
            if (currentSample < totalSamples * 0.15) {
                return MathUtil.fastSin(phase * 0.15) * Math.exp(-currentSample / 80.0) * config.plosiveGain;
            }
            return 0.0;
        }

        if (isNasal(c)) {
            return fastTanh(MathUtil.fastSin(phase * 0.5)) * config.nasalGain;
        }
        return 0.0;
    }

    private static double applyClickProtectionEnvelope(int sampleIndex, int totalSamples) {
        int fadeSamples = Math.min(160, totalSamples / 10);
        if (sampleIndex < fadeSamples) return sampleIndex / (double) fadeSamples;
        if (sampleIndex > totalSamples - fadeSamples) return (totalSamples - sampleIndex) / (double) fadeSamples;
        return 1.0;
    }

    private static double fastTanh(double x) {
        return x / (1.0 + Math.abs(x));
    }

    private static double generateBaseWave(double phase, WaveType waveType) {
        return switch (waveType) {
            case SINE -> MathUtil.fastSin(phase);
            case SQUARE -> fastTanh(MathUtil.fastSin(phase) * 2.0) * 0.75;
            case TRIANGLE -> (Math.abs((phase % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
        };
    }

    private static boolean isVowel(char c) { return "AEIOUÄÖÜ".contains(String.valueOf(c)); }
    private static boolean isPlosive(char c) { return "PTKBDG".contains(String.valueOf(c)); }
    private static boolean isNasal(char c) { return "MNLWR".contains(String.valueOf(c)); }
    private static boolean isPunctuation(char c) { return ".,!?;:-".contains(String.valueOf(c)); }

    private static void writeWavHeader(ByteBuffer buffer, int dataSize) {
        buffer.put("RIFF".getBytes());
        buffer.putInt(WAV_HEADER_SIZE + dataSize - 8);
        buffer.put("WAVE".getBytes());
        buffer.put("fmt ".getBytes());
        buffer.putInt(16);
        buffer.putShort((short) 1);
        buffer.putShort((short) CHANNELS);
        buffer.putInt(SAMPLE_RATE);
        buffer.putInt(SAMPLE_RATE * CHANNELS * BYTES_PER_SAMPLE);
        buffer.putShort((short) (CHANNELS * BYTES_PER_SAMPLE));
        buffer.putShort((short) BITS_PER_SAMPLE);
        buffer.put("data".getBytes());
        buffer.putInt(dataSize);
    }

}