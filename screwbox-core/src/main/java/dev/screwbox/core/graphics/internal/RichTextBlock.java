package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
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
        // 1. Berechne die Tiefe für jedes Zeichen im Originaltext (ohne Klammern)
        final var depthMap = PrecomputedDepth.from(text);
        final List<Glyph> glyphs = new ArrayList<>(depthMap.strippedText().length());

        int y = 0;
        int characterNr = 0;
        int processedCharsCount = 0; // Trackt den globalen Index im bereinigten Text

        final int fontHeightIncrement = (int) (options.font().height() * options.scale() + options.lineSpacing());

        // 2. Line-Wrapping und Layout (Völlig befreit von Klammer-Logik)
        for (final String line : TextUtil.lineWrap(depthMap.strippedText(), options.charactersPerLine())) {
            double x = initialHorizontalOffset(line);

            for (int i = 0; i < line.length(); i++) {
                final char targetChar = line.charAt(i);
                final int depth = depthMap.getDepthAt(processedCharsCount++);

                // Zeichen transformieren und Sprite laden
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

    private record PrecomputedDepth(String strippedText, int[] depths) {

        public static PrecomputedDepth from(String originalText) {
            final var sb = new StringBuilder(originalText.length());
            final int[] depthArray = new int[originalText.length()];

            int currentDepth = 0;
            int strippedIdx = 0;

            for (int i = 0; i < originalText.length(); i++) {
                char c = originalText.charAt(i);
                if (c == '{') {
                    currentDepth++;
                } else if (c == '}') {
                    currentDepth = Math.max(0, currentDepth - 1);
                } else {
                    sb.append(c);
                    depthArray[strippedIdx++] = currentDepth;
                }
            }
            return new PrecomputedDepth(sb.toString(), depthArray);
        }

        public int getDepthAt(int index) {
            // Schutz gegen Indexverschiebungen durch Whitespace-Änderungen im Line-Wrapper
            if (index >= depths.length) return 0;
            return depths[index];
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