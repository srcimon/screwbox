package dev.screwbox.core.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Prototype {

    public enum WaveType {SINE, SQUARE, TRIANGLE}

    // Festgelegtes Format für maximale macOS-Kompatibilität und Lautstärke
    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 1; // Mono

    private final static Random random = new Random();


    public static byte[] generateMumbleWavByteArray(String text, double baseFreq, WaveType waveType, double baseBlipDurationSeconds) {
        text = text.toUpperCase();
        int textLength = text.length();

        // ---------------------------------------------------------------------
        // 1. TIMING- UND GESCHWINDIGKEITS-BERECHNUNG
        // ---------------------------------------------------------------------
        int baseSamplesPerBlip = (int) (SAMPLE_RATE * baseBlipDurationSeconds);

        int[] blipLengths = new int[textLength];
        int totalSamples = 0;

        for (int i = 0; i < textLength; i++) {
            char c = text.charAt(i);
            double speedModifier = 0.8 + (random.nextDouble() * 0.4);
            if ("PTKBDG".contains(String.valueOf(c))) {
                speedModifier *= 0.35;
            }
            int currentSamplesPerBlip = (int) (baseSamplesPerBlip * speedModifier);
            blipLengths[i] = currentSamplesPerBlip;
            totalSamples += currentSamplesPerBlip;
        }

        int dataSize = totalSamples * (BITS_PER_SAMPLE / 8);
        int totalFileSize = 44 + dataSize;
        byte[] outputBuffer = new byte[totalFileSize];

        ByteBuffer buffer = ByteBuffer.wrap(outputBuffer).order(ByteOrder.LITTLE_ENDIAN);

        // WAV-Header schreiben
        buffer.put("RIFF".getBytes());
        buffer.putInt(totalFileSize - 8);
        buffer.put("WAVE".getBytes());
        buffer.put("fmt ".getBytes());
        buffer.putInt(16);
        buffer.putShort((short) 1);
        buffer.putShort((short) CHANNELS);
        buffer.putInt(SAMPLE_RATE);
        buffer.putInt(SAMPLE_RATE * CHANNELS * (BITS_PER_SAMPLE / 8));
        buffer.putShort((short) (CHANNELS * (BITS_PER_SAMPLE / 8)));
        buffer.putShort((short) BITS_PER_SAMPLE);
        buffer.put("data".getBytes());
        buffer.putInt(dataSize);

        // Variablen für fließende Vokal-Übergänge
        double targetVowelShift = 1.0;
        double currentVowelShift = 1.0;

        // ---------------------------------------------------------------------
        // 2. AUDIO-GENERIERUNG (Seidenweicher Frequenzverlauf)
        // ---------------------------------------------------------------------
        for (int i = 0; i < textLength; i++) {
            char c = text.charAt(i);
            boolean isSpace = (c == ' ');
            int currentSamplesPerBlip = blipLengths[i];

            // Filter zurücksetzen, um Signalstau zu vermeiden
            double noiseLP1 = 0.0;
            double noiseLP2 = 0.0;

            double randomPitchOffset = (random.nextDouble() * 0.24) - 0.12;
            double targetFrequency = baseFreq * (1.0 + randomPitchOffset);

            boolean isPause = random.nextInt(10) == 0 || isSpace;
            double volumeModifier = isPause ? 0.0 : (0.8 + random.nextDouble() * 0.2);

            if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
                targetVowelShift = switch (c) {
                    case 'A' -> 1.0;
                    case 'E' -> 1.3;
                    case 'I' -> 1.6;
                    case 'O' -> 0.8;
                    case 'U' -> 0.6;
                    default -> targetVowelShift;
                };
            } else if ("MNNLRW".contains(String.valueOf(c))) {
                targetVowelShift = 0.7;
            }

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;

                currentVowelShift = (currentVowelShift * 0.97) + (targetVowelShift * 0.03);

                double vibrato = Math.sin(2.0 * Math.PI * 5.2 * time) * 0.012
                                 + Math.cos(2.0 * Math.PI * 8.7 * time) * 0.007;

                double currentFreq = targetFrequency * currentVowelShift * (1.0 + vibrato);
                double angle = 2.0 * Math.PI * currentFreq * time;

                double rawValue = 0.0;

                if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
                    // VOKALE
                    double baseWave = generateBaseWave(angle, waveType);
                    rawValue = Math.tanh(baseWave * 1.4) * 0.85;

                } else if ("SZFCXCHKSST".contains(String.valueOf(c))) {
                    // --- INTEGRATION DES NEUEN STEILEN AKUSTIK-FILTERS ---
                    double rawNoise = (random.nextDouble() * 2.0 - 1.0) * 0.02; // Signalstärke gedrosselt //TODO config value

                    // Stufe 1 dampft die scharfen Ecken des Randoms ein (60% Trägheit)
                    noiseLP1 = (noiseLP1 * 0.60) + (rawNoise * 0.40);
                    // Stufe 2 glättet das Signal final (92% Trägheit) -> Macht ein dunkleres, weiches "Pusten"
                    noiseLP2 = (noiseLP2 * 0.92) + (noiseLP1 * 0.08);

                    rawValue = noiseLP2;

                } else if ("PTKBDG".contains(String.valueOf(c))) {
                    // PLOSIVE
                    if (s < currentSamplesPerBlip * 0.12) {
                        rawValue = Math.sin(2.0 * Math.PI * 65.0 * time) * Math.exp(-s / 120.0) * 0.5;
                    } else {
                        rawValue = 0.0;
                    }

                } else if (!isSpace) {
                    // NASALE
                    double nasalWave = Math.sin(angle * 0.55);
                    rawValue = Math.tanh(nasalWave * 1.1) * 0.45;
                }

                // Knackschutz Hüllkurve
                double envelope = 1.0;
                int decayThreshold = (int) (currentSamplesPerBlip * 0.65);
                if (s < 140) {
                    envelope = s / 140.0;
                } else if (s > decayThreshold) {
                    envelope = 1.0 - ((double) (s - decayThreshold) / (currentSamplesPerBlip - decayThreshold));
                }

                double sampleValue = rawValue * envelope * volumeModifier;

                if (sampleValue > 1.0) sampleValue = 1.0;
                if (sampleValue < -1.0) sampleValue = -1.0;

                short pcm16BitValue = (short) (sampleValue * 32767.0);
                buffer.putShort(pcm16BitValue);
            }
        }

        return outputBuffer;
    }

    private static double generateBaseWave(double angle, WaveType waveType) {//TODO EASE
        return switch (waveType) {
            case SINE -> Math.sin(angle);
            case SQUARE ->
                // Nutzt eine sanft komprimierte Sinuswelle statt einer harten Kante
                Math.tanh(Math.sin(angle) * 3.0) * 0.8;
            case TRIANGLE -> (Math.abs((angle % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
        };
    }

}
