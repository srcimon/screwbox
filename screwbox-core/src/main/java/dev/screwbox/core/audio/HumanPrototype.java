package dev.screwbox.core.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class HumanPrototype {
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNELS = 1;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int BYTES_PER_SAMPLE = BITS_PER_SAMPLE / 8;
    private static final int WAV_HEADER_SIZE = 44;

    private static final Random random = new Random();

    // Extrem weiche, unaufdringliche Formantfrequenzen (keine scharfen Spitzen)
    private static final double[][] FORMANTS = {
        {650.0, 1000.0}, // A
        {450.0, 1050.0}, // E
        {280.0, 1400.0}, // I
        {500.0, 750.0},  // O
        {300.0, 650.0}   // U
    };

    public enum WaveType { SINE, SQUARE, TRIANGLE }

    public static byte[] generateMumbleWavByteArray(String text, double baseFreq, WaveType waveType, double baseBlipDurationSeconds) {
        String sanitizedText = text.toUpperCase();
        int textLength = sanitizedText.length();

        int[] blipLengths = calculateBlipLengths(sanitizedText, baseBlipDurationSeconds);
        int totalSamples = java.util.stream.IntStream.of(blipLengths).sum();

        byte[] outputBuffer = new byte[WAV_HEADER_SIZE + totalSamples * BYTES_PER_SAMPLE];
        ByteBuffer buffer = ByteBuffer.wrap(outputBuffer).order(ByteOrder.LITTLE_ENDIAN);
        writeWavHeader(buffer, totalSamples * BYTES_PER_SAMPLE);

        // Fließende Mundbewegungen
        double currentF1 = 500.0, currentF2 = 1200.0;
        double targetF1 = 500.0, targetF2 = 1200.0;

        // Filter-Speicher für die Resonatoren
        double outF1_1 = 0, outF1_2 = 0;
        double outF2_1 = 0, outF2_2 = 0;
        double noiseLP = 0;

        double mainPhase = 0;

        // Sanfter Lautstärkeregler (Verhindert knackende Übergänge)
        double currentVolume = 0.0;
        double targetVolume = 0.0;

        for (int i = 0; i < textLength; i++) {
            char character = sanitizedText.charAt(i);
            int currentSamplesPerBlip = blipLengths[i];

            boolean isVowel = isVowel(character);
            boolean isFrikative = "SZFCXCHKSST".contains(String.valueOf(character));
            boolean isPlosive = "PTKBDG".contains(String.valueOf(character));
            boolean isPunctuation = ".,!?;:- ".contains(String.valueOf(character));

            // Formanten-Ziele setzen oder bei Interpunktion pausieren
            if (isPunctuation) {
                targetVolume = 0.0;
                currentVolume = 0.0; // Sofortiger Stopp der Lautstärke für echte Pausen
            } else if (isVowel) {
                int vowelIndex = getVowelIndex(character);
                targetF1 = FORMANTS[vowelIndex][0];
                targetF2 = FORMANTS[vowelIndex][1];
                targetVolume = 0.6; // Angenehme Standardlautstärke
            } else if (isFrikative) {
                targetVolume = 0.15; // Rauschen stark gedämpft (weniger "noisy")
            } else if (isPlosive) {
                targetVolume = 0.25; // Plosive sanfter ansetzen
            } else {
                targetVolume = 0.4;  // Konsonanten generell weicher
            }

            // Frequenz-Oszillation für lebendigere Tonhöhe (Vibrato)
            double pitchMod = 1.0 + 0.03 * Math.sin(2.0 * Math.PI * 6.0 * (mainPhase / SAMPLE_RATE));
            double currentPitch = baseFreq * pitchMod;

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                // Sanftes Einblenden nach einer Pause / Übergänge glätten
                if (!isPunctuation) {
                    currentVolume += (targetVolume - currentVolume) * 0.01; // Etwas schnelleres Auffächern nach Pausen
                }

                // Formant-Übergänge glätten
                currentF1 += (targetF1 - currentF1) * 0.002;
                currentF2 += (targetF2 - currentF2) * 0.002;

                // 1. Quellensignal
                double source = 0.0;
                if (!isPunctuation) {
                    mainPhase += currentPitch;
                    double angle = 2.0 * Math.PI * mainPhase / SAMPLE_RATE;

                    switch (waveType) {
                        case SINE -> source = Math.sin(angle);
                        case TRIANGLE -> source = Math.abs((mainPhase % SAMPLE_RATE) / SAMPLE_RATE * 4 - 2) - 1;
                        case SQUARE -> source = Math.sin(angle) >= 0 ? 0.5 : -0.5;
                    }
                }

                // 2. Rausch-Generator (Extrem gedämpfter Tiefpass für weiches Flüstern)
                double whiteNoise = random.nextDouble() * 2.0 - 1.0;
                noiseLP += (whiteNoise - noiseLP) * 0.08; // Starker Tiefpassfilter gegen Zischeln

                // Signalmischung je nach Buchstabentyp
                double mixedSignal = source;
                if (isFrikative) {
                    mixedSignal = (source * 0.3) + (noiseLP * 0.7);
                } else if (isPlosive) {
                    double burst = s < (SAMPLE_RATE * 0.01) ? noiseLP * 0.4 : 0.0;
                    mixedSignal = (source * 0.8) + burst;
                }

                // 3. Formant-Filter (Zwei Bandpass-Resonatoren in Reihe)
                double outF1 = applyBandpass(mixedSignal, currentF1, 80.0, outF1_1, outF1_2);
                outF1_2 = outF1_1; outF1_1 = outF1;

                double outF2 = applyBandpass(outF1, currentF2, 120.0, outF2_1, outF2_2);
                outF2_2 = outF2_1; outF2_1 = outF2;

                // Endmix mit Lautstärkeregelung
                double finalSample = outF2 * currentVolume;

                // Hard Limiting gegen Clipping
                if (finalSample > 1.0) finalSample = 1.0;
                if (finalSample < -1.0) finalSample = -1.0;

                short shortSample = (short) (finalSample * 32767);
                buffer.putShort(shortSample);
            }
        }
        return outputBuffer;
    }

    private static double applyBandpass(double input, double frequency, double bandwidth, double y1, double y2) {
        double r = Math.exp(-Math.PI * bandwidth / SAMPLE_RATE);
        double theta = 2.0 * Math.PI * frequency / SAMPLE_RATE;
        double b0 = 1.0 - r;
        double a1 = -2.0 * r * Math.cos(theta);
        double a2 = r * r;
        return b0 * input - a1 * y1 - a2 * y2;
    }

    private static int[] calculateBlipLengths(String text, double baseDuration) {
        int[] lengths = new int[text.length()];
        int defaultSamples = (int) (baseDuration * SAMPLE_RATE);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (".!?".contains(String.valueOf(c))) {
                lengths[i] = (int) (SAMPLE_RATE * 0.4); // Lange Pause bei Satzende (400ms)
            } else if (",;:".contains(String.valueOf(c))) {
                lengths[i] = (int) (SAMPLE_RATE * 0.2); // Kurze Pause bei Komma (200ms)
            } else if (c == ' ') {
                lengths[i] = (int) (SAMPLE_RATE * 0.15); // Wortpause
            } else {
                lengths[i] = defaultSamples;
            }
        }
        return lengths;
    }

    private static boolean isVowel(char c) {
        return "AEIOUÄÖÜ".contains(String.valueOf(c));
    }

    private static int getVowelIndex(char c) {
        return switch (c) {
            case 'E', 'Ö' -> 1;
            case 'I', 'Ü' -> 2;
            case 'O' -> 3;
            case 'U' -> 4;
            default -> 0; // 'A' und standardmäßig Ä
        };
    }

    private static void writeWavHeader(ByteBuffer buffer, int dataSize) {
        buffer.put("RIFF".getBytes());
        buffer.putInt(36 + dataSize);
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