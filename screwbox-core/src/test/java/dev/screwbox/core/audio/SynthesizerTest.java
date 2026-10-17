package dev.screwbox.core.audio;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SynthesizerTest {

    @Test
    void generateChatter_sameTextAndPinnedSeed_isIdentical() {
        var firstSpeech = Synthesizer.generateChatter("test", Voice.fixed(121239L));
        var secondSpeech = Synthesizer.generateChatter("test", Voice.fixed(121239L));

        assertThat(firstSpeech.content()).isEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isEqualTo(secondSpeech.duration());
    }

    @Test
    void generateChatter_longerTextSameSeed_isLonger() {
        var firstSpeech = Synthesizer.generateChatter("test", Voice.fixed(121239L));
        var secondSpeech = Synthesizer.generateChatter("tetstst", Voice.fixed(121239L));

        assertThat(firstSpeech.content()).isNotEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration().isLessThan(secondSpeech.duration())).isTrue();
    }

    @Test
    void generateChatter_differentSeeds_areDifferent() {
        var firstSpeech = Synthesizer.generateChatter("test", Voice.fixed(123L));
        var secondSpeech = Synthesizer.generateChatter("test", Voice.fixed(121239L));

        assertThat(firstSpeech.content()).isNotEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isNotEqualTo(secondSpeech.duration());
    }

    @Test
    void generateChatter_fixedSeedAndText_isIdentical() {
        Voice fixed = Voice.fixed();
        var firstSpeech = Synthesizer.generateChatter("test", fixed);
        var secondSpeech = Synthesizer.generateChatter("test", fixed);

        assertThat(firstSpeech.content()).isEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isEqualTo(secondSpeech.duration());
    }

    @Test
    void generateChatter_sameTextVariableSeed_isDifferent() {
        Voice variable = Voice.variable();
        var firstSpeech = Synthesizer.generateChatter("test", variable);
        var secondSpeech = Synthesizer.generateChatter("test", variable);

        assertThat(firstSpeech.content()).isNotEqualTo(secondSpeech.content());
        assertThat(firstSpeech.duration()).isNotEqualTo(secondSpeech.duration());
    }

    @Test
    void generateChatter_complexText_hasValidOutput() {
        String text = "This is a much more complex, yet still not too complex Demo text containing all german umlauts (ä, ö and ü)! And it has multple sentences.";
        var chatter = Synthesizer.generateChatter(text, Voice.fixed(1230L));

        assertThat(chatter.content()).isNotNull();
        assertThat(chatter.duration().milliseconds()).isGreaterThan(2000);
    }
}
