package dev.screwbox.core.audio;

import dev.screwbox.core.audio.internal.WaveSupport;
import dev.screwbox.core.audio.internal.synth.SynthConfiguration;
import dev.screwbox.core.audio.internal.synth.SynthContext;
import dev.screwbox.core.audio.internal.synth.SynthElement;
import dev.screwbox.core.utils.MathUtil;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


//TODO rename to synthesizer
public class Synthesizer {

    private static final double TO_SIGNED_INTEGER = 32767.0;
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 2, true, false);

    public static Sound synthesizeMumbleSpeech(final String text, final VoiceOptions options) {
        final String sanitizedText = text.toUpperCase();
        try (final var byteArrayOutputStream = new ByteArrayOutputStream(8192);
            final DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream)) {

            byte[] dummyHeader = new byte[WaveSupport.WAV_HEADER_SIZE];
            outputStream.write(dummyHeader);


            var soundContext = new SynthContext(options);
            char[] chars = sanitizedText.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                char character = chars[i];
                final var element = SynthConfiguration.findElement(character);
                double textProgress = (double) i / chars.length;

                synthesizeElement(element, options, soundContext, outputStream, textProgress);//TODO move text progress into context
            }
            outputStream.flush();

            final byte[] finalWav = byteArrayOutputStream.toByteArray();
            WaveSupport.writeHeaderToBuffer(finalWav, AUDIO_FORMAT);

            return Sound.fromSoundData(finalWav);
        } catch (IOException e) {
            throw new IllegalStateException("could not create audio data", e);
        }
    }

    private static void synthesizeElement(final SynthElement element, final VoiceOptions options,
                                          final SynthContext context, final DataOutputStream dos, final double textProgress) {
        int currentSamplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * element.blipLength().apply(context, options));

        double targetVowelShift = element.targetWovelShift().apply(context, options);

        // Menschliche Intonationskurve: Tonhöhe fällt zum Satzende hin natürlich ab
        double intonation = MathUtil.fastSin(textProgress * Math.PI) * 0.15 - (textProgress * 0.1);
        double targetFrequency = element.segmentFrequency().apply(context, options) * (1.0 + intonation);

        double segmentVolume = element.segmentVolume().apply(context, options);

        for (int s = 0; s < currentSamplesPerBlip; s++) {
            // Extrem glatte Übergänge für organischen Redefluss
            context.setVowelShift(context.vowelShift() + (targetVowelShift - context.vowelShift()) * 0.002);
            context.setPitch(context.pitch() + (targetFrequency - context.pitch()) * 0.002);

            double globalTime = (double) context.totalSamplesWritten() / AUDIO_FORMAT.getSampleRate();

            // Shimmer & Jitter: Winzige, unvorhersehbare Muskelunreinheiten der Stimme
            double jitter = (context.randomDouble() - 0.5) * 0.002;
            double vibrato = MathUtil.fastSin(2.0 * Math.PI * options.vibratoSpeed() * globalTime) * options.vibratoDepth();

            double instantFreq = Math.clamp(context.pitch() * (1.0 + (context.vowelShift() - 1.0) * 0.20) * (1.0 + vibrato + jitter), 45.0, 350.0);
            context.setPhase((context.phase() + (2.0 * Math.PI * instantFreq) / AUDIO_FORMAT.getSampleRate()) % (2.0 * Math.PI));

            // Synthese und Hüllkurve gegen Knacken (Fade-In / Fade-Out pro Blip)
            double rawValue = element.phonemeCalculation().apply(context, options);
            double envelope = applyClickProtectionEnvelope(s, currentSamplesPerBlip);

            // Menschliche Amplitudenschwankung (Atem)
            double breathShimmer = 1.0 + (MathUtil.fastSin(globalTime * 2.0) * 0.05);
            double preFilteredSample = Math.clamp(rawValue * envelope * segmentVolume * breathShimmer, -1.0, 1.0);

            // Tiefpassfilterung (Getrennte Zustände für Stereo-Breite, falls gewünscht)
            context.setFilterState(context.filterState() + options.lowPassCutoff() * (preFilteredSample - context.filterState()));

            short pcmSampleLeft = (short) (context.filterState() * TO_SIGNED_INTEGER);
            short pcmSampleRight = (short) (context.filterState() * TO_SIGNED_INTEGER);

            try {
                dos.writeByte(pcmSampleLeft & 0xFF);
                dos.writeByte((pcmSampleLeft >> 8) & 0xFF);
                dos.writeByte(pcmSampleRight & 0xFF);
                dos.writeByte((pcmSampleRight >> 8) & 0xFF);
            } catch (IOException ignored) {
            }

            context.setSamplesWritten(context.totalSamplesWritten() + 1);
        }
    }

    private static double applyClickProtectionEnvelope(int sampleIdx, int totalSamples) {
        int fadeSamples = Math.min(120, totalSamples / 10); // Sanftes Ein-/Ausblenden (ca. 2-3ms)
        if (sampleIdx < fadeSamples) return (double) sampleIdx / fadeSamples;
        if (sampleIdx > totalSamples - fadeSamples) return (double) (totalSamples - sampleIdx) / fadeSamples;
        return 1.0;
    }

}