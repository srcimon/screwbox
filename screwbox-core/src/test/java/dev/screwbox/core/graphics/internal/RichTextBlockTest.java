package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RichTextBlockTest {

    @Test
    void glyphs_noStyles_isSameAsTextLength() {
        var block = new RichTextBlock("This is a Test", TextDrawOptions.font(FontBundle.BOLDZILLA));

        assertThat(block.glyphs()).hasSize(14)
            .anyMatch(glyph -> glyph.characterNr() == 0);
    }

    @Test
    void glyphs_twoStyles_isSameAsTextLengthWithoutMarkers() {
        var block = new RichTextBlock("This {is} a {{Test}}", TextDrawOptions.font(FontBundle.BOLDZILLA));

        assertThat(block.glyphs()).hasSize(14)
            .anyMatch(glyph -> glyph.characterNr() == 0);
    }
}
