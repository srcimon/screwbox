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

    //TODO validate brackates
    public List<Glyph> glyphs() {
        var cleaned = text.replace("{","").replace("}", "");

        List<Glyph> glyphs = new ArrayList<>(cleaned.length());

        int y = 0, characterNr = 0, textIdx = 0;
        int depth = 0;

        final Pixelfont baseFont = options.font(0);
        final int fontHeightIncrement = (int) (baseFont.height() * options.scale() + options.lineSpacing());

        for (final String line : TextUtil.lineWrap(cleaned, options.charactersPerLine())) {
            double x = switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };

            int lineLength = line.length();
            for (int i = 0; i < lineLength; i++) {
                char targetChar = line.charAt(i);

                // 1. ANKER: Klammern verarbeiten
                while (textIdx < text.length() && ((text.charAt(textIdx) == '{') || (text.charAt(textIdx) == '}'))) {
                    char brace = text.charAt(textIdx);
                    int count = 0;
                    while (textIdx < text.length() && text.charAt(textIdx) == brace) {
                        count++;
                        textIdx++;
                    }
                    if (brace == '{') {
                        depth += count;
                    } else {
                        depth = depth - count;
                        if (depth < 0) depth = 0;
                    }
                }

                // 2. ANKER: Vorspulen bei Zeichen-Diskrepanz
                while (textIdx < text.length() && text.charAt(textIdx) != targetChar) {
                    char c = text.charAt(textIdx);
                    if (c == '{' || c == '}') {
                        int count = 0;
                        while (textIdx < text.length() && text.charAt(textIdx) == c) {
                            count++;
                            textIdx++;
                        }
                        if (c == '{') {
                            depth += count;
                        } else {
                            depth = depth - count;
                            if (depth < 0) depth = 0;
                        }
                    } else {
                        textIdx++;
                    }
                }

                Pixelfont currentFont = options.font(depth);
                ShaderSetup currentShader = options.shader(depth);

                char renderChar = options.isUppercase() ? Character.toUpperCase(targetChar) : targetChar;
                var sprite = currentFont.spriteFor(renderChar);

                if (sprite.isPresent()) {
                    var spriteGet = sprite.get();
                    if (!Character.isWhitespace(targetChar)) {
                        glyphs.add(new Glyph(Offset.at(x, y), spriteGet, currentShader, characterNr++));
                    }
                    x += (spriteGet.width() + (double) options.padding()) * options.scale();
                }
                textIdx++;
            }
            y += fontHeightIncrement;
        }
        return glyphs;
    }
}