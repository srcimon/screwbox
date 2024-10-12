package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextDrawOptionsTest {

    @Test
    void lineSpacing_negative_throwsException() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS);
        assertThatThrownBy(() -> options.lineSpacing(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("line spacing must be positive");
    }

    @Test
    void charactersPerLine_zero_throwsException() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS);
        assertThatThrownBy(() -> options.charactersPerLine(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("characters per line must be positive");
    }

    @Test
    void newInstance_fontNull_throwsException() {
        assertThatThrownBy(() -> TextDrawOptions.font((Pixelfont) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("font must not be null");
    }

    @Test
    void newInstance_opacityNull_throwsException() {
        var options = TextDrawOptions.font(new Pixelfont());

        assertThatThrownBy(() -> options.opacity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("opacity must not be null");
    }

    @Test
    void newInstance_changedAllProperties_setsAllProperties() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).uppercase()
                .charactersPerLine(30)
                .lineSpacing(4)
                .scale(2.5)
                .alignCenter()
                .padding(3);

        assertThat(options.charactersPerLine()).isEqualTo(30);
        assertThat(options.lineSpacing()).isEqualTo(4);
        assertThat(options.scale()).isEqualTo(2.5);
        assertThat(options.alignment()).isEqualTo(TextDrawOptions.Alignment.CENTER);
        assertThat(options.padding()).isEqualTo(3);
    }

    @Test
    void sizeOf_uppercaseText_returnsSizeOfText() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).uppercase();

        assertThat(options.sizeOf("Some kind of lame text")).isEqualTo(Size.of(120, 8));
    }

    @Test
    void sizeOf_scaledText_returnsSizeOfText() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2);

        assertThat(options.sizeOf("Some kind of lame text")).isEqualTo(Size.of(226, 16));
    }

    @Test
    void widthOf_text_returnsSizeOfText() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2);

        assertThat(options.widthOf("Some kind of lame text")).isEqualTo(226);
    }

    @Test
    void widthOf_multipleLines_returnsSizeOfWidestLine() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2).charactersPerLine(10);

        assertThat(options.widthOf("Some kind of lame text spread over three lines")).isEqualTo(106);
    }

    @Test
    void sizeOf_multipleLines_returnsSizeOfAllLines() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2).charactersPerLine(6);

        assertThat(options.sizeOf("Some kind of lame text")).isEqualTo(Size.of(50, 96));
    }
}
