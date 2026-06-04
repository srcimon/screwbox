package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Pixelfont;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class RichTextBlock {

    private enum Mode {
        NORMAL,
        ALTERNATE,
        SECONDARY_ALTERNATE
    }

    public record Glyph(Offset offset, Sprite sprite, int characterNr) {

    }

    private final String text;
    private final TextDrawOptions options;

    public RichTextBlock(final String text, final TextDrawOptions options) {
        this.text = text;
        this.options = options;
    }

    private String pureText() {
        return text.replace("{", "").replace("}", "");
    }


    public List<Glyph> glyphs() {
        var font = options.font();
        var alternate = options.font().replaceColor(Color.WHITE, Color.RED);
        var secondaryAlternate = options.font().replaceColor(Color.WHITE, Color.BLUE);
        List<Glyph> glyphs = new ArrayList<>();
        var lines = TextUtil.lineWrap(pureText(), options.charactersPerLine());
        int y = 0;
        int characterNr = 0;
        for (final String line : lines) {
            double x = switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };
            for (Character character : line.toCharArray()) {
                var sprite = font.spriteFor(options.isUppercase() ? Character.toUpperCase(character) : character);
                if (sprite.isPresent()) {
                    glyphs.add(new Glyph(Offset.at(x, y), sprite.get(), characterNr));
                    x += (sprite.get().width() + options.padding()) * options.scale();
                    characterNr++;
                }
            }
            y += (int) (1.0 * font.height() * options.scale() + options.lineSpacing());
        }
        return glyphs;
    }
}
