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
        {280.0, 1400.0}, // I (Radikal tief angesetzt gegen hohes Zischeln)
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

            // Formanten-Ziele weich zuweisen
            if (isVowel) {
                int idx = "AEIOU".indexOf(character);
                targetF1 = FORMANTS[idx][0];
                targetF2 = FORMANTS[idx][1];
            } else if ("MNNLRW".contains(String.valueOf(character))) {
                targetF1 = 250.0; targetF2 = 550.0; // Sehr dumpfer, sanfter Nasalraum
            } else {
                targetF1 = 400.0; targetF2 = 900.0;  // Neutrale Brückenstellung
            }

            // Ruhige, gleitende Melodielinie (kein unruhiges Zittern)
            double targetFrequency = baseFreq * (1.0 + (Math.sin(i * 0.3) * 0.04));
            targetVolume = (character == ' ') ? 0.0 : (0.4 + random.nextDouble() * 0.1); // Lautstärke halbiert!

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;

                // Formanten und Lautstärke gleiten butterweich ineinander über
                currentF1 = (currentF1 * 0.995) + (targetF1 * 0.005);
                currentF2 = (currentF2 * 0.995) + (targetF2 * 0.005);
                currentVolume = (currentVolume * 0.99) + (targetVolume * 0.01);

                // --- 1. BIOLOGISCHER KEHLKOPF-PULS ---
                mainPhase += (2.0 * Math.PI * targetFrequency) / SAMPLE_RATE;
                if (mainPhase > 2.0 * Math.PI) mainPhase -= 2.0 * Math.PI;

                // Sanfter, abgerundeter Impuls
                double glottisSource = (mainPhase < Math.PI * 0.7)
                    ? Math.sin(mainPhase * (1.0 / 0.7))
                    : -Math.exp(-(mainPhase - Math.PI * 0.7)) * 0.1;

                // FIX: Eingangssignal stark dämpfen, um Filter-Rauschen im Keim zu ersticken
                double inputSource = glottisSource * 0.05 * xFadeProtection(s, currentSamplesPerBlip) * currentVolume;

                double filteredSignal = 0.0;

                // --- 2. AUDIO-SYNTHESE ---
                if (isVowel || "MNNLRW".contains(String.valueOf(character))) {

                    // Breite Bandbreiten (150Hz & 300Hz) schlucken aggressive Resonanzen und Rauschen vollständig
                    double r1 = Math.exp(-Math.PI * 150.0 / SAMPLE_RATE);
                    double c1 = 2.0 * r1 * Math.cos(2.0 * Math.PI * currentF1 / SAMPLE_RATE);
                    double outF1 = inputSource + c1 * outF1_1 - (r1 * r1) * outF1_2;
                    outF1_2 = outF1_1; outF1_1 = outF1;

                    double r2 = Math.exp(-Math.PI * 300.0 / SAMPLE_RATE);
                    double c2 = 2.0 * r2 * Math.cos(2.0 * Math.PI * currentF2 / SAMPLE_RATE);
                    double outF2 = inputSource + c2 * outF2_1 - (r2 * r2) * outF2_2;
                    outF2_2 = outF2_1; outF2_1 = outF2;

                    filteredSignal = (outF1 * 0.5) + (outF2 * 0.25);

                } else if (isFrikative) {
                    // Kaum wahrnehmbares, extrem tiefes Flüstern für S-Laute
                    double rawNoise = (random.nextDouble() * 2.0 - 1.0) * 0.015;
                    noiseLP = (noiseLP * 0.97) + (rawNoise * 0.03);
                    filteredSignal = noiseLP * currentVolume * xFadeProtection(s, currentSamplesPerBlip);

                } else if (isPlosive) {
                    // Sehr dezenter, tiefer Pop
                    if (s < currentSamplesPerBlip * 0.08) {
                        filteredSignal = Math.sin(2.0 * Math.PI * 55.0 * time) * Math.exp(-s / 60.0) * 0.08 * currentVolume;
                    }
                }

                // Finale Normalisierung und sanfte Begrenzung
                double finalSample = Math.tanh(filteredSignal * 20.0) * 0.5;

                if (finalSample > 1.0) finalSample = 1.0;
                else if (finalSample < -1.0) finalSample = -1.0;

                buffer.putShort((short) (finalSample * 32767.0));
            }
        }

        return outputBuffer;
    }

    private static int[] calculateBlipLengths(String text, double baseBlipDurationSeconds) {
        int baseSamplesPerBlip = (int) (SAMPLE_RATE * baseBlipDurationSeconds);
        int[] lengths = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            double speedModifier = 0.9 + (random.nextDouble() * 0.2);
            if ("PTKBDG".contains(String.valueOf(text.charAt(i)))) speedModifier *= 0.35;
            lengths[i] = (int) (baseSamplesPerBlip * speedModifier);
        }
        return lengths;
    }

    private static double xFadeProtection(int s, int total) {
        int decay = (int) (total * 0.7);
        if (s < 200) return s / 200.0; // Sanfteres Einblenden
        if (s > decay) return 1.0 - ((double)(s - decay) / (total - decay)); // Sanfteres Ausblenden
        return 1.0;
    }

    private static void writeWavHeader(ByteBuffer buffer, int dataSize) {
        buffer.put("RIFF".getBytes()); buffer.putInt(44 + dataSize - 8); buffer.put("WAVE".getBytes());
        buffer.put("fmt ".getBytes()); buffer.putInt(16); buffer.putShort((short) 1); buffer.putShort((short) CHANNELS);
        buffer.putInt(SAMPLE_RATE); buffer.putInt(SAMPLE_RATE * CHANNELS * BYTES_PER_SAMPLE);
        buffer.putShort((short) (CHANNELS * BYTES_PER_SAMPLE)); buffer.putShort((short) BITS_PER_SAMPLE);
        buffer.put("data".getBytes()); buffer.putInt(dataSize);
    }

    private static boolean isVowel(char c) { return "AEIOU".indexOf(c) != -1; }


}
