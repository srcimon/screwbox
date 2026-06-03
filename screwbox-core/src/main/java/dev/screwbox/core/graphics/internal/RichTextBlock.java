package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class RichTextBlock {

    public record Part(Offset offset, Sprite sprite, int characterNr) {

    }

    private final String text;
    private final TextDrawOptions options;
    private final List<String> lines;

    public RichTextBlock(final String text, final TextDrawOptions options) {
        this.text = text;
        this.options = options;
        this.lines = TextUtil.lineWrap(text, options.charactersPerLine());
    }

    public List<String> lines() {
        return lines;
    }

    public List<Part> parts() {
        List<Part> parts = new ArrayList<>();

        int y = 0;
        int characterNr = 0;
        final var textBlock = new RichTextBlock(text, options);
        for (final String line : textBlock.lines()) {
            double x = switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };
            final List<Sprite> allSprites = options.font().spritesFor(options.isUppercase() ? line.toUpperCase() : line);
            for (final var sprite : allSprites) {
                parts.add(new Part(Offset.at(x, y), sprite, characterNr));
                x += (sprite.width() + options.padding()) * options.scale();
                characterNr++;
            }
            y += (int) (1.0 * options.font().height() * options.scale() + options.lineSpacing());
        }
        return parts;
    }
}
