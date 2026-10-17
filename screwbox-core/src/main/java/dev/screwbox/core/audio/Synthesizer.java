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

/**
 * Can synthesize mumbled chatter to give NPCs a voice.
 *
 * @since 3.37.0
 */
public class Synthesizer {

    private static final double TO_SIGNED_INTEGER = 32767.0;
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 2, true, false);

    private Synthesizer() {

    }

    /**
     * Synthesize a {@link Sound} from specified text and {@link Voice}. Synthesizing will take 1 to 100 ms based on
     * the system. It's recommended to use {@link Audio#playChatter(String, Voice)} to avoid stuttering.
     */
    public static Sound generateChatter(final String text, final Voice voice) {
        final char[] sanitizedCharacters = text.toUpperCase().toCharArray();
        final var context = new SynthContext(voice);
        try (final var byteArrayOutputStream = new ByteArrayOutputStream(8192);
            final var outputStream = new DataOutputStream(byteArrayOutputStream)) {
            outputStream.write(new byte[WaveSupport.WAV_HEADER_SIZE]);
            for (int i = 0; i < sanitizedCharacters.length; i++) {
                final var element = SynthConfiguration.findElement(sanitizedCharacters[i]);
                context.setProgress((double) i / sanitizedCharacters.length);
                synthesizeElement(element, voice, context, outputStream);
            }
            outputStream.flush();

            final byte[] finalWav = byteArrayOutputStream.toByteArray();
            WaveSupport.writeHeaderToBuffer(finalWav, AUDIO_FORMAT);

            return Sound.fromSoundData(finalWav);
        } catch (final IOException e) {
            throw new IllegalStateException("could not create audio data", e);
        }
    }

    private static void synthesizeElement(final SynthElement element, final Voice voice,
                                          final SynthContext context, final DataOutputStream outputStream) throws IOException {

        final int samplesPerBlip = (int) (AUDIO_FORMAT.getSampleRate() * element.blipLength().apply(context, voice));
        final double targetVowelShift = element.targetWovelShift().apply(context, voice);

        final double intonation = MathUtil.fastSin(context.progress() * Math.PI) * 0.15 - (context.progress() * 0.1);
        final double targetFrequency = element.segmentFrequency().apply(context, voice) * (1.0 + intonation);

        final double segmentVolume = element.segmentVolume().apply(context, voice);

        for (int sampleNr = 0; sampleNr < samplesPerBlip; sampleNr++) {
            context.setVowelShift(context.vowelShift() + (targetVowelShift - context.vowelShift()) * 0.002);
            context.setPitch(context.pitch() + (targetFrequency - context.pitch()) * 0.002);

            final double globalTime = (double) context.samplesWritten() / AUDIO_FORMAT.getSampleRate();

            final double jitter = (context.randomDouble() - 0.5) * 0.002;
            final double vibrato = MathUtil.fastSin(2.0 * Math.PI * voice.vibratoSpeed() * globalTime) * voice.vibratoDepth().value();

            final double instantFreq = Math.clamp(context.pitch() * (1.0 + (context.vowelShift() - 1.0) * 0.20) * (1.0 + vibrato + jitter), 45.0, 350.0);
            context.setPhase((context.phase() + (2.0 * Math.PI * instantFreq) / AUDIO_FORMAT.getSampleRate()) % (2.0 * Math.PI));

            final double rawValue = element.phoneme().apply(context, voice);
            final double envelope = applyClickProtectionEnvelope(sampleNr, samplesPerBlip);

            final double breathShimmer = 1.0 + (MathUtil.fastSin(globalTime * 2.0) * 0.05);
            final double preFilteredSample = Math.clamp(rawValue * envelope * segmentVolume * breathShimmer, -1.0, 1.0);

            context.setFilterState(context.filterState() + voice.scratchyness().value() * (preFilteredSample - context.filterState()));

            final short pcmSample = (short) (context.filterState() * TO_SIGNED_INTEGER);
            outputStream.writeByte(pcmSample & 0xFF);
            outputStream.writeByte((pcmSample >> 8) & 0xFF);
            outputStream.writeByte(pcmSample & 0xFF);
            outputStream.writeByte((pcmSample >> 8) & 0xFF);
            context.setSamplesWritten(context.samplesWritten() + 1);
        }
    }

    private static double applyClickProtectionEnvelope(final int sampleIdx, final int totalSamples) {
        final int fadeSamples = Math.min(120, totalSamples / 10); // soft fade in and out
        if (sampleIdx < fadeSamples) {
            return (double) sampleIdx / fadeSamples;
        }
        if (sampleIdx > totalSamples - fadeSamples) {
            return (double) (totalSamples - sampleIdx) / fadeSamples;
        }
        return 1.0;
    }

}