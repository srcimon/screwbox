package dev.screwbox.playground;

import dev.screwbox.core.utils.MathUtil;

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
        public double baseFreq = 110.0;             // Grundtonhöhe (z.B. 110Hz Mann, 200Hz Frau)
        public double baseBlipDuration = 0.08;      // Sprechgeschwindigkeit

        public double pitchRandomness = 0.15;       // Natürliche Mikro-Schwankung der Tonhöhe
        public double speedRandomness = 0.25;       // Natürliche Schwankung des Sprechtempos

        // Menschliches Vibrato (Muskel-Instabilität)
        public double vibratoSpeed = 5.5;           // Zitter-Geschwindigkeit in Hz
        public double vibratoDepth = 0.025;         // Zitter-Stärke

        // Pausenkonfiguration
        public double sentencePause = 0.45;
        public double commaPause = 0.22;
        public double wordPause = 0.15;

        // Mischpult
        public double vowelGain = 0.75;
        public double nasalGain = 0.40;
        public double plosiveGain = 0.35;
    }

    public static byte[] generateMumbleWavByteArray(String text, WaveType waveType, VoiceConfig config) {
        String sanitizedText = text.toUpperCase();
        int[] blipLengths = calculateBlipLengths(sanitizedText, config);
        int dataSize = java.util.stream.IntStream.of(blipLengths).sum() * CHANNELS * BYTES_PER_SAMPLE;

        ByteBuffer buffer = ByteBuffer.wrap(new byte[WAV_HEADER_SIZE + dataSize]).order(ByteOrder.LITTLE_ENDIAN);
        writeWavHeader(buffer, dataSize);

        double runningPhase = 0.0;
        double currentPitch = config.baseFreq;
        double currentVowelShift = 1.0;

        for (int i = 0; i < sanitizedText.length(); i++) {
            char character = sanitizedText.charAt(i);
            int currentSamplesPerBlip = blipLengths[i];
            boolean isPunctuation = isPunctuation(character);

            double targetVowelShift = determineTargetVowelShift(character, currentVowelShift);
            double targetFrequency = calculateSegmentFrequency(config.baseFreq, config.pitchRandomness, character);
            double volumeModifier = calculateSegmentVolume(character);

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                currentVowelShift += (targetVowelShift - currentVowelShift) * 0.003;
                currentPitch += (targetFrequency - currentPitch) * 0.002;

                // Vibrato & Frequenzberechnung kompakt zusammengefasst
                double globalTime = (double) buffer.position() / (BYTES_PER_SAMPLE * SAMPLE_RATE);
                double vibrato = MathUtil.fastSin(2.0 * Math.PI * config.vibratoSpeed * globalTime) * config.vibratoDepth;
                double instantFreq = Math.clamp(currentPitch * (1.0 + (currentVowelShift - 1.0) * 0.3) * (1.0 + vibrato), 40.0, 400.0);

                // Phase inkrementieren und per Modulo begrenzen
                runningPhase = (runningPhase + (2.0 * Math.PI * instantFreq) / SAMPLE_RATE) % (2.0 * Math.PI);

                // Synthese, Hüllkurve und Buffering in 3 knackigen Zeilen
                double rawValue = synthesizePhoneme(character, runningPhase, currentSamplesPerBlip, s, waveType, config);
                double envelope = isPunctuation ? 0.0 : applyClickProtectionEnvelope(s, currentSamplesPerBlip);
                double sampleValue = Math.clamp(rawValue * envelope * volumeModifier, -1.0, 1.0);

                buffer.putShort((short) (sampleValue * 32767.0));
            }
        }
        return buffer.array();
    }

    private static int[] calculateBlipLengths(String text, VoiceConfig config) {
        int baseSamplesPerBlip = (int) (SAMPLE_RATE * config.baseBlipDuration);
        int[] lengths = new int[text.length()];

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (".!?".contains(String.valueOf(c))) {
                lengths[i] = (int) (SAMPLE_RATE * config.sentencePause);
                continue;
            } else if (",;:".contains(String.valueOf(c))) {
                lengths[i] = (int) (SAMPLE_RATE * config.commaPause);
                continue;
            } else if (c == ' ' || c == '-') {
                lengths[i] = (int) (SAMPLE_RATE * config.wordPause);
                continue;
            }

            double deviation = (random.nextDouble() * 2.0 - 1.0) * config.speedRandomness;
            double speedModifier = 1.0 + deviation;

            if (isPlosive(c)) {
                speedModifier *= 0.35;
            }
            lengths[i] = (int) (baseSamplesPerBlip * speedModifier);
        }
        return lengths;
    }

    private static double determineTargetVowelShift(char c, double fallbackValue) {
        if (isVowel(c)) {
            return switch (c) {
                case 'A' -> 0.95; // Weichere, komprimierte Shifts gegen extreme Tonhöhen-Sprünge
                case 'E' -> 1.05;
                case 'I' -> 1.15; // Zuvor 1.4 (Verursachte das extreme Fiepen!)
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
        if (isPunctuation(c) || c == ' ') {
            return 0.0;
        }
        return (0.85 + random.nextDouble() * 0.15);
    }

    private static double synthesizePhoneme(char c, double phase, int totalSamples, int currentSample, WaveType waveType, VoiceConfig config) {
        if (isPunctuation(c) || c == ' ') {
            return 0.0;
        }

        if (isVowel(c)) {
            return fastTanh(generateBaseWave(phase, waveType) * 1.2) * config.vowelGain;
        }

        if (isPlosive(c)) {
            if (currentSample < totalSamples * 0.15) {
                return MathUtil.fastSin(phase * 0.15) * Math.exp(-currentSample / 80.0) * config.plosiveGain;
            }
            return 0.0;
        }

        if (isNasal(c)) {
            double nasalWave = MathUtil.fastSin(phase * 0.5);
            return fastTanh(nasalWave) * config.nasalGain;
        }

        return 0.0;
    }

    private static double applyClickProtectionEnvelope(int sampleIndex, int totalSamples) {
        int fadeSamples = 160;
        if (sampleIndex < fadeSamples) {
            return sampleIndex / (double) fadeSamples;
        } else if (sampleIndex > totalSamples - fadeSamples) {
            return (totalSamples - sampleIndex) / (double) fadeSamples;
        }
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

    private static boolean isVowel(char c) {
        return "AEIOUÄÖÜ".contains(String.valueOf(c));
    }

    private static boolean isFrikative(char c) {
        return "SZFCXCHKSST".contains(String.valueOf(c));
    }

    private static boolean isPlosive(char c) {
        return "PTKBDG".contains(String.valueOf(c));
    }

    private static boolean isNasal(char c) {
        return "MNLWR".contains(String.valueOf(c));
    }

    private static boolean isPunctuation(char c) {
        return ".,!?;:-".contains(String.valueOf(c));
    }

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