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

    // Biologisch kalibrierte, extrem weiche Formantfrequenzen (gedämpfte Obertöne)
    private static final double[][] FORMANTS = {
        {700.0, 1050.0}, // A
        {500.0, 1150.0}, // E
        {300.0, 1700.0}, // I (Nochmals abgerundet gegen jegliches Fiepen)
        {550.0, 800.0},  // O
        {320.0, 700.0}   // U
    };

    public enum WaveType {SINE, SQUARE, TRIANGLE}

    public static byte[] generateMumbleWavByteArray(String text, double baseFreq, dev.screwbox.core.audio.Prototype.WaveType waveType, double baseBlipDurationSeconds) {
        String sanitizedText = text.toUpperCase();
        int textLength = sanitizedText.length();

        int[] blipLengths = calculateBlipLengths(sanitizedText, baseBlipDurationSeconds);
        int totalSamples = java.util.stream.IntStream.of(blipLengths).sum();

        byte[] outputBuffer = new byte[WAV_HEADER_SIZE + totalSamples * BYTES_PER_SAMPLE];
        ByteBuffer buffer = ByteBuffer.wrap(outputBuffer).order(ByteOrder.LITTLE_ENDIAN);
        writeWavHeader(buffer, totalSamples * BYTES_PER_SAMPLE);

        // Fließende Mundbewegungen (Filter-Zustände über die gesamte Sprachausgabe hinweg)
        double currentF1 = 500.0, currentF2 = 1500.0;
        double targetF1 = 500.0, targetF2 = 1500.0;

        // Filter-Speicher für die Resonatoren
        double outF1_1 = 0, outF1_2 = 0;
        double outF2_1 = 0, outF2_2 = 0;
        double noiseLP = 0;

        double mainPhase = 0;

        // Globale Lautstärke-Glättung (Verhindert Amplituden-Sprünge zwischen Buchstaben)
        double currentVolume = 0.0;
        double targetVolume = 0.0;

        for (int i = 0; i < textLength; i++) {
            char character = sanitizedText.charAt(i);
            int currentSamplesPerBlip = blipLengths[i];

            boolean isVowel = isVowel(character);
            boolean isFrikative = "SZFCXCHKSST".contains(String.valueOf(character));
            boolean isPlosive = "PTKBDG".contains(String.valueOf(character));

            // Sanfte Formanten-Ziele zuweisen
            if (isVowel) {
                int idx = "AEIOU".indexOf(character);
                targetF1 = FORMANTS[idx][0];
                targetF2 = FORMANTS[idx][1];
            } else if ("MNNLRW".contains(String.valueOf(character))) {
                targetF1 = 260.0;
                targetF2 = 600.0; // Äußerst dumpfer, weicher Nasalraum
            } else {
                targetF1 = 400.0;
                targetF2 = 1000.0; // Neutrale Brückenstellung
            }

            // Fließende Intonationskurve (Menschliche Sprachmelodie)
            double targetFrequency = baseFreq * (1.0 + (Math.sin(i * 0.35) * 0.05) + (random.nextDouble() * 0.01 - 0.005));
            targetVolume = (character == ' ') ? 0.0 : (0.8 + random.nextDouble() * 0.15);

            for (int s = 0; s < currentSamplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;

                // --- MAXIMUM SMOOTHNESS INTERPOLATION ---
                // Formanten gleiten jetzt extrem träge und flüssig (Co-Artikulation)
                currentF1 = (currentF1 * 0.994) + (targetF1 * 0.006);
                currentF2 = (currentF2 * 0.994) + (targetF2 * 0.006);

                // Lautstärke-Übergänge zwischen Buchstaben komplett glätten
                currentVolume = (currentVolume * 0.992) + (targetVolume * 0.008);

                // --- 1. BIOLOGISCHER KEHLKOPF-PULS (Glottis) ---
                mainPhase += (2.0 * Math.PI * targetFrequency) / SAMPLE_RATE;
                if (mainPhase > 2.0 * Math.PI) mainPhase -= 2.0 * Math.PI;

                // Extrem abgerundeter, weicher Druckimpuls ohne scharfe Abrisskanten
                double glottisSource = (mainPhase < Math.PI * 0.7)
                    ? Math.sin(mainPhase * (1.0 / 0.7))
                    : -Math.exp(-(mainPhase - Math.PI * 0.7)) * 0.15;

                double filteredSignal = 0.0;

                // --- 2. PHONETISCHE AUDIO-SYNTHESE ---
                if (isVowel || "MNNLRW".contains(String.valueOf(character))) {

                    // Erhöhte Bandbreitendämpfung (75Hz & 140Hz) simuliert weiches Gewebe im Mundraum
                    double r1 = Math.exp(-Math.PI * 75.0 / SAMPLE_RATE);
                    double c1 = 2.0 * r1 * Math.cos(2.0 * Math.PI * currentF1 / SAMPLE_RATE);
                    double outF1 = glottisSource + c1 * outF1_1 - (r1 * r1) * outF1_2;
                    outF1_2 = outF1_1;
                    outF1_1 = outF1;

                    double r2 = Math.exp(-Math.PI * 140.0 / SAMPLE_RATE);
                    double c2 = 2.0 * r2 * Math.cos(2.0 * Math.PI * currentF2 / SAMPLE_RATE);
                    double outF2 = glottisSource + c2 * outF2_1 - (r2 * r2) * outF2_2;
                    outF2_2 = outF2_1;
                    outF2_1 = outF2;

                    // Harmonische Gewichtung für einen wärmeren Grundton
                    filteredSignal = (outF1 * 0.45) + (outF2 * 0.2);

                } else if (isFrikative) {
                    // Seidenweich gefilterter Atemhauch ohne scharfe Frequenzen
                    double rawNoise = (random.nextDouble() * 2.0 - 1.0) * 0.06;
                    noiseLP = (noiseLP * 0.96) + (rawNoise * 0.04);
                    filteredSignal = noiseLP;

                } else if (isPlosive) {
                    // Warmer, unaufdringlicher Lippen-Pop
                    if (s < currentSamplesPerBlip * 0.10) {
                        filteredSignal = Math.sin(2.0 * Math.PI * 60.0 * time) * Math.exp(-s / 80.0) * 0.25;
                    }
                }

                // Knackschutz an den Rändern des Segments
                double finalSample = filteredSignal * xFadeProtection(s, currentSamplesPerBlip) * currentVolume;

                // Mathematisch samtige Begrenzung (Verhindert Übersteuerungen komplett)
                finalSample = Math.tanh(finalSample * 1.1) * 0.8;

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
            double speedModifier = 0.85 + (random.nextDouble() * 0.3);
            if ("PTKBDG".contains(String.valueOf(text.charAt(i)))) speedModifier *= 0.35;
            lengths[i] = (int) (baseSamplesPerBlip * speedModifier);
        }
        return lengths;
    }

    private static double xFadeProtection(int s, int total) {
        int decay = (int) (total * 0.65);
        if (s < 140) return s / 140.0;
        if (s > decay) return 1.0 - ((double) (s - decay) / (total - decay));
        return 1.0;
    }

    private static void writeWavHeader(ByteBuffer buffer, int dataSize) {
        buffer.put("RIFF".getBytes());
        buffer.putInt(44 + dataSize - 8);
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

    private static boolean isVowel(char c) {
        return "AEIOU".indexOf(c) != -1;
    }


}
