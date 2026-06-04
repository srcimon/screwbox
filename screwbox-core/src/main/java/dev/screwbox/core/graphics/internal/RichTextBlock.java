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

public class RichTextBlock {

    public record Glyph(Offset offset, Sprite sprite, ShaderSetup shader, int characterNr) {
    }

    private final String text;
    private final TextDrawOptions options;

    public RichTextBlock(final String text, final TextDrawOptions options) {
        //TODO validate brackets
        this.text = text;
        this.options = options;
    }

    public List<Glyph> glyphs() {
        var cleaned = text.replace("{","").replace("}", "");
        var lines = TextUtil.lineWrap(cleaned, options.charactersPerLine());

        List<Glyph> glyphs = new ArrayList<>(cleaned.length());

        int y = 0, characterNr = 0, textIdx = 0;
        int depth = 0; // Performance-Optimierung 3: Primitives int statt Heap-Objekt (ParseState)

        final boolean isUppercase = options.isUppercase();
        final double scale = options.scale();
        final double padding = options.padding();
        final Pixelfont baseFont = options.font(0);
        final int fontHeightIncrement = (int) (baseFont.height() * scale + options.lineSpacing());

        for (String line : lines) {
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

                char renderChar = isUppercase ? Character.toUpperCase(targetChar) : targetChar;
                var sprite = currentFont.spriteFor(renderChar);

                if (sprite.isPresent()) {
                    var spriteGet = sprite.get();
                    if (!Character.isWhitespace(targetChar)) {
                        glyphs.add(new Glyph(Offset.at(x, y), spriteGet, currentShader, characterNr++));
                    }
                    x += (spriteGet.width() + padding) * scale;
                }
                textIdx++;
            }
            y += fontHeightIncrement;
        }
        return glyphs;
    }
}