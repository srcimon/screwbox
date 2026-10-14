package dev.screwbox.core.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Prototype {

    // Festgelegtes Format für maximale macOS-Kompatibilität und Lautstärke
    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 1; // Mono

    private final static Random random = new Random();


    // Global konfigurierbare Konstanten (Können auch in Config-Klasse ausgelagert werden)
    private static final int BYTES_PER_SAMPLE = BITS_PER_SAMPLE / 8;
    private static final int WAV_HEADER_SIZE = 44;

    // Phonetische Lautstärken- und Filter-Konfigurationen
    private static final double NOISE_VOLUME_GAIN = 0.02; // Aufgelöstes TODO: Config Value
    private static final double VOWEL_VOLUME_GAIN = 0.85;
    private static final double NASAL_VOLUME_GAIN = 0.45;
    private static final double PLOSIVE_VOLUME_GAIN = 0.5;


    public enum WaveType {
        SINE, SQUARE, TRIANGLE
    }

    /**
     * Generiert ein einkanaliges 16-Bit PCM WAV-Byte-Array, das eine prozedurale,
     * melodische Retro-Sprachausgabe (Mumble-Sound) emuliert.
     */
    public static byte[] generateMumbleWavByteArray(String text, double baseFreq, WaveType waveType, double baseBlipDurationSeconds) {
        String sanitizedText = text.toUpperCase();

        // 1. Längenberechnung aller einzelnen Lautsegmente (Blips)
        int[] blipLengths = calculateBlipLengths(sanitizedText, baseBlipDurationSeconds);
        int totalSamples = java.util.stream.IntStream.of(blipLengths).sum();

        // 2. Speicherallokation & WAV-Header initialisieren
        int dataSize = totalSamples * CHANNELS * BYTES_PER_SAMPLE;
        byte[] outputBuffer = new byte[WAV_HEADER_SIZE + dataSize];

        ByteBuffer buffer = ByteBuffer.wrap(outputBuffer).order(ByteOrder.LITTLE_ENDIAN);
        writeWavHeader(buffer, dataSize);

        // 3. Audio-Syntheseschleife über alle Zeichen
        double currentVowelShift = 1.0;
        for (int i = 0; i < sanitizedText.length(); i++) {
            char character = sanitizedText.charAt(i);
            int currentSamplesPerBlip = blipLengths[i];

            // Ziel-Formant-Verschiebung basierend auf der Phonem-Gruppe ermitteln
            double targetVowelShift = determineTargetVowelShift(character, currentVowelShift);

            // Einzigartige, zufällige Stimmvariation pro Lautsegment berechnen
            double targetFrequency = calculateSegmentFrequency(baseFreq);
            double volumeModifier = calculateSegmentVolume(character);

            // Filterzustand pro Buchstabe nullen (verhindert tieffrequentes Rumpeln im Hintergrund)
            double[] noiseFilterState = new double[2]; // [0] = noiseLP1, [1] = noiseLP2

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;

                // Co-Artikulation: Formanten gleiten organisch ineinander über
                currentVowelShift = (currentVowelShift * 0.97) + (targetVowelShift * 0.03);

                // Phase & Frequenzmodulation (Doppel-Vibrato simuliert biologische Muskeln)
                double angle = calculateVocalAngle(time, targetFrequency, currentVowelShift);

                // Rohes Audiosignal je nach phonetischer Lautklasse generieren
                double rawValue = synthesizePhoneme(character, angle, time, currentSamplesPerBlip, s, waveType, noiseFilterState);

                // Dynamische Amplituden-Hüllkurve gegen Knackgeräusche (Fade In / Fade Out)
                double envelope = applyClickProtectionEnvelope(s, currentSamplesPerBlip);
                double sampleValue = rawValue * envelope * volumeModifier;

                // Hard-Clipping Schutz
                sampleValue = Math.max(-1.0, Math.min(1.0, sampleValue));

                buffer.putShort((short) (sampleValue * 32767.0));
            }
        }

        return outputBuffer;
    }

    private static int[] calculateBlipLengths(String text, double baseBlipDurationSeconds) {
        int baseSamplesPerBlip = (int) (SAMPLE_RATE * baseBlipDurationSeconds);
        int[] lengths = new int[text.length()];

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            double speedModifier = 0.8 + (random.nextDouble() * 0.4);

            if (isPlosive(c)) {
                speedModifier *= 0.35; // Plosive explodieren sehr schnell
            }
            lengths[i] = (int) (baseSamplesPerBlip * speedModifier);
        }
        return lengths;
    }

    private static double determineTargetVowelShift(char c, double fallbackValue) {
        if (isVowel(c)) {
            return switch (c) {
                case 'A' -> 1.0;
                case 'E' -> 1.3;
                case 'I' -> 1.6;
                case 'O' -> 0.8;
                case 'U' -> 0.6;
                default  -> fallbackValue;
            };
        } else if (isNasal(c)) {
            return 0.7;
        }
        return fallbackValue;
    }

    private static double calculateSegmentFrequency(double baseFreq) {
        double randomPitchOffset = (random.nextDouble() * 0.24) - 0.12; // +/- 12% Abweichung
        return baseFreq * (1.0 + randomPitchOffset);
    }

    private static double calculateSegmentVolume(char c) {
        boolean isPause = random.nextInt(10) == 0 || c == ' ';
        return isPause ? 0.0 : (0.8 + random.nextDouble() * 0.2);
    }

    private static double calculateVocalAngle(double time, double targetFrequency, double vowelShift) {
        double vibrato = Math.sin(2.0 * Math.PI * 5.2 * time) * 0.012
                         + Math.cos(2.0 * Math.PI * 8.7 * time) * 0.007;
        double currentFreq = targetFrequency * vowelShift * (1.0 + vibrato);
        return 2.0 * Math.PI * currentFreq * time;
    }

    private static double synthesizePhoneme(char c, double angle, double time, int totalSamples, int currentSample, WaveType waveType, double[] filterState) {
        if (isVowel(c)) {
            return Math.tanh(generateBaseWave(angle, waveType) * 1.4) * VOWEL_VOLUME_GAIN;
        }

        if (isFrikative(c)) {
            double rawNoise = (random.nextDouble() * 2.0 - 1.0) * NOISE_VOLUME_GAIN;
            // 2-Stufen-Tiefpass-Filter (Butterworth/RC-Kaskade gegen das Kratzen)
            filterState[0] = (filterState[0] * 0.60) + (rawNoise * 0.40);
            filterState[1] = (filterState[1] * 0.92) + (filterState[0] * 0.08);
            return filterState[1];
        }

        if (isPlosive(c)) {
            if (currentSample < totalSamples * 0.12) {
                return Math.sin(2.0 * Math.PI * 65.0 * time) * Math.exp(-currentSample / 120.0) * PLOSIVE_VOLUME_GAIN;
            }
            return 0.0;
        }

        if (c != ' ') { // Nasale und liquide Reibelaut-Klasse (M, N, L...)
            double nasalWave = Math.sin(angle * 0.55);
            return Math.tanh(nasalWave * 1.1) * NASAL_VOLUME_GAIN;
        }

        return 0.0; // Standardstille für Leerzeichen
    }

    private static double applyClickProtectionEnvelope(int sampleIndex, int totalSamples) {
        int decayThreshold = (int) (totalSamples * 0.65);
        if (sampleIndex < 140) {
            return sampleIndex / 140.0; // Einblenden
        } else if (sampleIndex > decayThreshold) {
            return 1.0 - ((double) (sampleIndex - decayThreshold) / (totalSamples - decayThreshold)); // Ausblenden
        }
        return 1.0;
    }

    private static double generateBaseWave(double angle, WaveType waveType) {
        return switch (waveType) {
            case SINE     -> Math.sin(angle);
            case SQUARE   -> Math.tanh(Math.sin(angle) * 3.0) * 0.8; // Aufgelöstes TODO: Analog geformte Kurve statt clippender Kante
            case TRIANGLE -> (Math.abs((angle % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
        };
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

    // --- Phonetische Validierungs-Hilfsmethoden für bessere Lesbarkeit ---
    private static boolean isVowel(char c) {
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    private static boolean isFrikative(char c) {
        return "SZFCXCHKSST".indexOf(c) != -1;
    }

    private static boolean isPlosive(char c) {
        return "PTKBDG".indexOf(c) != -1;
    }

    private static boolean isNasal(char c) {
        return "MNNLRW".indexOf(c) != -1;
    }

}
