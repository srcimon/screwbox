package dev.screwbox.core.audio.internal.synth;

import dev.screwbox.core.audio.internal.synth.functions.*;

import java.util.Map;

import static java.util.Map.entry;

public class SynthConfiguration {

    private static final Map<Character, SynthElement> SYNTH_CONFIG = Map.ofEntries(
        // vowel
        entry('A', new SynthElement(new FixedValueFunction(0.92), new WovelPhonemeFunction(2.0, 3.5), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('E', new SynthElement(new FixedValueFunction(1.05), new WovelPhonemeFunction(1.5, 4.0), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('I', new SynthElement(new FixedValueFunction(1.12), new WovelPhonemeFunction(1.2, 4.5), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('O', new SynthElement(new FixedValueFunction(0.85), new WovelPhonemeFunction(1.4, 2.4), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('U', new SynthElement(new FixedValueFunction(0.75), new WovelPhonemeFunction(1.1, 1.8), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('Ä', new SynthElement(new FixedValueFunction(92), new WovelPhonemeFunction(1.8, 4.0), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('Ö', new SynthElement(new FixedValueFunction(0.85), new WovelPhonemeFunction(1.5, 3.2), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('Ü', new SynthElement(new FixedValueFunction(1.12), new WovelPhonemeFunction(1.2, 3.5), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),

        // plosive
        entry('P', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new PlosiveBlipLengthFunction())),
        entry('T', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new PlosiveBlipLengthFunction())),
        entry('K', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new PlosiveBlipLengthFunction())),
        entry('B', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new PlosiveBlipLengthFunction())),
        entry('D', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new PlosiveBlipLengthFunction())),
        entry('G', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new PlosiveBlipLengthFunction())),

        // nasal
        entry('M', new SynthElement(new FixedValueFunction(0.88), new NasalPhonemeFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('N', new SynthElement(new FixedValueFunction(0.88), new NasalPhonemeFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('L', new SynthElement(new FixedValueFunction(0.88), new NasalPhonemeFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('W', new SynthElement(new FixedValueFunction(0.88), new NasalPhonemeFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),
        entry('R', new SynthElement(new FixedValueFunction(0.88), new NasalPhonemeFunction(), new RandomSegmentVolumeFunction(), new RandomFrequencyFunction(), new BlipLengthFunction())),

        // punctuation
        entry('.', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new SentencePauseFunction())),
        entry(',', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new CommaPauseFunction())),
        entry('!', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new SentencePauseFunction())),
        entry('?', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new SentencePauseFunction())),
        entry(';', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new CommaPauseFunction())),
        entry(':', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new CommaPauseFunction())),
        entry('-', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new WordPauseFunction())),

        // space
        entry(' ', new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new BaseFrequencyFunction(), new WordPauseFunction()))
    );

    private static final SynthElement DEFAULT_ELEMENT = new SynthElement(new WovelShiftFunction(), new FixedValueFunction(), new FixedValueFunction(), new RandomFrequencyFunction(), new BlipLengthFunction());

    public static SynthElement findElement(final char character) {
        return SYNTH_CONFIG.getOrDefault(character, DEFAULT_ELEMENT);
    }
}
