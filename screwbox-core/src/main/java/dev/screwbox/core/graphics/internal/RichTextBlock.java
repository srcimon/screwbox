package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Pixelfont;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

public record RichTextBlock(String text, TextDrawOptions options) {

    public record Glyph(Offset offset, Sprite sprite, ShaderSetup shader, int characterNr) {
    }

    public List<Glyph> glyphs() {
        final var strippedText = text.replace("{", "").replace("}", "").replace("\n", "").replace("\r", "");
        int y = 0;
        int characterNr = 0;

        final var depthTracker = new DepthTracker();
        final int fontHeightIncrement = (int) (options.font().height() * options.scale() + options.lineSpacing());

        final List<Glyph> glyphs = new ArrayList<>(strippedText.length());
        for (final String line : TextUtil.lineWrap(strippedText, options.charactersPerLine())) {
            double x = initialHorizontalOffset(line);

            for (int i = 0; i < line.length(); i++) {
                final char targetChar = line.charAt(i);

                final int depth = depthTracker.advanceToCharacter(text, targetChar);

                final char renderChar = options.isUppercase() ? Character.toUpperCase(targetChar) : targetChar;
                var sprite = fetchFont(depth).spriteFor(renderChar);

                if (sprite.isPresent()) {
                    var spriteGet = sprite.get();
                    glyphs.add(new Glyph(Offset.at(x, y), spriteGet, fetchShader(depth), characterNr++));
                    x += (spriteGet.width() + (double) options.padding()) * options.scale();
                }
            }
            y += fontHeightIncrement;
        }
        return glyphs;
    }

    private static final class DepthTracker {
        private int index = 0;
        private int depth = 0;

        public int advanceToCharacter(final String text, final char targetChar) {
            while (index < text.length()) {
                final char origChar = text.charAt(index++);
                if (origChar == '{') {
                    depth++;
                } else if (origChar == '}') {
                    depth = Math.max(0, depth - 1);
                } else if (origChar == targetChar) {
                    return depth;
                }
            }
            return depth;
        }
    }

    private Pixelfont fetchFont(final int depth) {
        return depth == 0 ? options.font() : options.alternateFonts().getOrDefault(depth, options.font());
    }

    private ShaderSetup fetchShader(final int depth) {
        return depth == 0 ? options.shader() : options.alternateShaders().getOrDefault(depth, options.shader());
    }

    private double initialHorizontalOffset(final String line) {
        return switch (options.alignment()) {
            case LEFT -> 0;
            case CENTER -> -options.widthOf(line) / 2.0;
            case RIGHT -> -options.widthOf(line);
        };
    }
}