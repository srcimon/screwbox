package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class RichTextBlock {

    public record Glyph(Offset offset, Sprite sprite, int characterNr) {

    }

    private final String text;
    private final TextDrawOptions options;

    public RichTextBlock(final String text, final TextDrawOptions options) {
        this.text = text;
        this.options = options;

    }

    public List<Glyph> glyphs() {
        List<Glyph> glyphs = new ArrayList<>();
        var lines = TextUtil.lineWrap(text, options.charactersPerLine());
        int y = 0;
        int characterNr = 0;
        for (final String line : lines) {
            double x = switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };
            final List<Sprite> allSprites = options.font().spritesFor(options.isUppercase() ? line.toUpperCase() : line);
            for (final var sprite : allSprites) {
                glyphs.add(new Glyph(Offset.at(x, y), sprite, characterNr));
                x += (sprite.width() + options.padding()) * options.scale();
                characterNr++;
            }
            y += (int) (1.0 * options.font().height() * options.scale() + options.lineSpacing());
        }
        return glyphs;
    }
}
