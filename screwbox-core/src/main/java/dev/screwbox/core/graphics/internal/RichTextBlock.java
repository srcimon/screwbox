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
        final var strippedText = text.replace("{", "").replace("}", "");
        final List<Glyph> glyphs = new ArrayList<>(strippedText.length());

        int y = 0;
        int characterNr = 0;

        // Wir nutzen einen globalen Tracker für den originalen String, um die Tiefe exakt zu verfolgen
        int globalTextIdx = 0;
        int depth = 0;

        final int fontHeightIncrement = (int) (options.font().height() * options.scale() + options.lineSpacing());

        for (final String line : TextUtil.lineWrap(strippedText, options.charactersPerLine())) {
            double x = initialHorizontalOffset(line);

            for (int i = 0; i < line.length(); i++) {
                final char targetChar = line.charAt(i);

                // Überspringe alle Klammern im Originaltext und aktualisiere die Tiefe,
                // bis wir wieder auf das aktuelle Zeichen der gewrappten Zeile stoßen.
                while (globalTextIdx < text.length()) {
                    char origChar = text.charAt(globalTextIdx);
                    if (origChar == '{') {
                        depth++;
                        globalTextIdx++;
                    } else if (origChar == '}') {
                        depth = Math.max(0, depth - 1);
                        globalTextIdx++;
                    } else if (origChar == targetChar) {
                        // Zeichen matcht! Schleife abbrechen, um das Zeichen zu verarbeiten.
                        break;
                    } else {
                        // Fallback für ignorierte/modifizierte Whitespaces durch das Wrapping
                        globalTextIdx++;
                    }
                }

                char renderChar = options.isUppercase() ? Character.toUpperCase(targetChar) : targetChar;
                var sprite = fetchFont(depth).spriteFor(renderChar);

                if (sprite.isPresent()) {
                    var spriteGet = sprite.get();
                    glyphs.add(new Glyph(Offset.at(x, y), spriteGet, fetchShader(depth), characterNr++));
                    x += (spriteGet.width() + (double) options.padding()) * options.scale();
                }

                globalTextIdx++;
            }
            y += fontHeightIncrement;
        }
        return glyphs;
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