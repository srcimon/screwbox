package dev.screwbox.core.audio;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SynthesizerTest {

    @Test
    void generateMumble_sameTextAndPinnedSeed_isIdentical() {
        var firstMumble = Synthesizer.generateMumble("test", Voice.fixed(121239L));
        var secondMumbe = Synthesizer.generateMumble("test", Voice.fixed(121239L));

        assertThat(firstMumble.content()).isEqualTo(secondMumbe.content());
        assertThat(firstMumble.duration()).isEqualTo(secondMumbe.duration());
    }

    @Test
    void generateMumble_longerTextSameSeed_isLonger() {
        var firstMumble = Synthesizer.generateMumble("test", Voice.fixed(121239L));
        var secondMumbe = Synthesizer.generateMumble("tetstst", Voice.fixed(121239L));

        assertThat(firstMumble.content()).isNotEqualTo(secondMumbe.content());
        assertThat(firstMumble.duration().isLessThan(secondMumbe.duration())).isTrue();
    }

    @Test
    void generateMumble_differentSeeds_areDifferent() {
        var firstMumble = Synthesizer.generateMumble("test", Voice.fixed(123L));
        var secondMumbe = Synthesizer.generateMumble("test", Voice.fixed(121239L));

        assertThat(firstMumble.content()).isNotEqualTo(secondMumbe.content());
        assertThat(firstMumble.duration()).isNotEqualTo(secondMumbe.duration());
    }

    @Test
    void generateMumble_fixedSeedAndText_isIdentical() {
        Voice fixed = Voice.fixed();
        var firstMumble = Synthesizer.generateMumble("test", fixed);
        var secondMumbe = Synthesizer.generateMumble("test", fixed);

        assertThat(firstMumble.content()).isEqualTo(secondMumbe.content());
        assertThat(firstMumble.duration()).isEqualTo(secondMumbe.duration());
    }

    @Test
    void generateMumble_sameTextVariableSeed_isDifferent() {
        Voice variable = Voice.variable();
        var firstMumble = Synthesizer.generateMumble("test", variable);
        var secondMumbe = Synthesizer.generateMumble("test", variable);

        assertThat(firstMumble.content()).isNotEqualTo(secondMumbe.content());
    }

    @Test
    void generateMumble_complexText_hasValidOutput() {
        String text = "This is a much more complex, yet still not too complex Demo text containing all german umlauts (ä, ö and ü)! And it has multple sentences.";
        var mumble = Synthesizer.generateMumble(text, Voice.fixed(1230L));

        assertThat(mumble.content()).isNotNull();
        assertThat(mumble.duration().milliseconds()).isGreaterThan(2000);
    }
}
