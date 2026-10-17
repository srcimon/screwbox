package dev.screwbox.core.audio;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SynthesizerTest {

    @Test
    void generateChatter_sameTextAndPinnedSeed_isIdentical() {
        var firstChatter = Synthesizer.generateChatter("test", Voice.fixed(121239L));
        var secondChatter = Synthesizer.generateChatter("test", Voice.fixed(121239L));

        assertThat(firstChatter.content()).isEqualTo(secondChatter.content());
        assertThat(firstChatter.duration()).isEqualTo(secondChatter.duration());
    }

    @Test
    void generateChatter_longerTextSameSeed_isLonger() {
        var firstChatter = Synthesizer.generateChatter("test", Voice.fixed(121239L));
        var secondChatter = Synthesizer.generateChatter("tetstst", Voice.fixed(121239L));

        assertThat(firstChatter.content()).isNotEqualTo(secondChatter.content());
        assertThat(firstChatter.duration().isLessThan(secondChatter.duration())).isTrue();
    }

    @Test
    void generateChatter_differentSeeds_areDifferent() {
        var firstChatter = Synthesizer.generateChatter("test", Voice.fixed(123L));
        var secondChatter = Synthesizer.generateChatter("test", Voice.fixed(121239L));

        assertThat(firstChatter.content()).isNotEqualTo(secondChatter.content());
        assertThat(firstChatter.duration()).isNotEqualTo(secondChatter.duration());
    }

    @Test
    void generateChatter_fixedSeedAndText_isIdentical() {
        Voice fixed = Voice.fixed();
        var firstChatter = Synthesizer.generateChatter("test", fixed);
        var secondChatter = Synthesizer.generateChatter("test", fixed);

        assertThat(firstChatter.content()).isEqualTo(secondChatter.content());
        assertThat(firstChatter.duration()).isEqualTo(secondChatter.duration());
    }

    @Test
    void generateChatter_sameTextVariableSeed_isDifferent() {
        Voice variable = Voice.variable();
        var firstChatter = Synthesizer.generateChatter("test", variable);
        var secondChatter = Synthesizer.generateChatter("test", variable);

        assertThat(firstChatter.content()).isNotEqualTo(secondChatter.content());
        assertThat(firstChatter.duration()).isNotEqualTo(secondChatter.duration());
    }

    @Test
    void generateChatter_complexText_hasValidOutput() {
        String text = "This is a much more complex, yet still not too complex Demo text containing all german umlauts (ä, ö and ü)! And it has multple sentences.";
        var chatter = Synthesizer.generateChatter(text, Voice.fixed(1230L));

        assertThat(chatter.content()).isNotNull();
        assertThat(chatter.duration().milliseconds()).isGreaterThan(2000);
    }
}
