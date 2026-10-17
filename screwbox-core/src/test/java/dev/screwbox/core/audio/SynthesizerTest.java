package dev.screwbox.core.audio;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SynthesizerTest {

    @Test
    void synthesizeMumbleSpeech_sameTextAndPinnedSeed_isIdentical() {
        var firstSpeech = Synthesizer.synthesizeMumbleSpeech("test", VoiceOptions.fixed(121239L));
        var secondSpeech = Synthesizer.synthesizeMumbleSpeech("test", VoiceOptions.fixed(121239L));

        assertThat(firstSpeech.content()).isEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isEqualTo(secondSpeech.duration());
    }

    @Test
    void synthesizeMumbleSpeech_longerTextSameSeed_isLonger() {
        var firstSpeech = Synthesizer.synthesizeMumbleSpeech("test", VoiceOptions.fixed(121239L));
        var secondSpeech = Synthesizer.synthesizeMumbleSpeech("tetstst", VoiceOptions.fixed(121239L));

        assertThat(firstSpeech.content()).isNotEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration().isLessThan(secondSpeech.duration())).isTrue();
    }

    @Test
    void synthesizeMumbleSpeech_differentSeeds_areDifferent() {
        var firstSpeech = Synthesizer.synthesizeMumbleSpeech("test", VoiceOptions.fixed(123L));
        var secondSpeech = Synthesizer.synthesizeMumbleSpeech("test", VoiceOptions.fixed(121239L));

        assertThat(firstSpeech.content()).isNotEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isNotEqualTo(secondSpeech.duration());
    }

    @Test
    void synthesizeMumbleSpeech_fixedSeedAndText_isIdentical() {
        VoiceOptions fixed = VoiceOptions.fixed();
        var firstSpeech = Synthesizer.synthesizeMumbleSpeech("test", fixed);
        var secondSpeech = Synthesizer.synthesizeMumbleSpeech("test", fixed);

        assertThat(firstSpeech.content()).isEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isEqualTo(secondSpeech.duration());
    }

    @Test
    void synthesizeMumbleSpeech_sameTextVariableSeed_isDifferent() {
        VoiceOptions variable = VoiceOptions.variable();
        var firstSpeech = Synthesizer.synthesizeMumbleSpeech("test", variable);
        var secondSpeech = Synthesizer.synthesizeMumbleSpeech("test", variable);

        assertThat(firstSpeech.content()).isNotEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isNotEqualTo(secondSpeech.duration());
    }
}
