package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
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
        this.text = text;
        this.options = options;
    }

    public List<Glyph> glyphs() {
        var baseFont = options.font();
        var baseShader = options.shader();
        var alternate = options.alternativeFont();
        var alternateShader = options.alternativeShader();
        var secondary = options.secondaryAlternativeFont();
        var secondaryShader = options.secondaryAlternativeShader();

        // Zeilenumbrüche basierend auf reinem Text ohne Klammern berechnen
        var cleanText = text.replace("{", "").replace("}", "");
        var lines = TextUtil.lineWrap(cleanText, options.charactersPerLine());

        List<Glyph> glyphs = new ArrayList<>();
        int y = 0, characterNr = 0, textIdx = 0;
        Pixelfont currentFont = baseFont;
        var currentShader = baseShader; // Tracker für den aktiven Shader

        for (String line : lines) {
            double x = switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };

            // Jede Zeile Zeichen für Zeichen durchgehen
            for (int i = 0; i < line.length(); i++) {
                char targetChar = line.charAt(i);

                // Im Originaltext vorspulen und Formatierungsklammern live verarbeiten
                while (textIdx < text.length() && (text.charAt(textIdx) == '{' || text.charAt(textIdx) == '}')) {
                    char brace = text.charAt(textIdx);
                    boolean isDouble = (textIdx + 1 < text.length() && text.charAt(textIdx + 1) == brace);

                    if (brace == '{') {
                        currentFont = isDouble ? secondary : alternate;
                        currentShader = isDouble ? baseShader : alternateShader; // Shader anpassen
                    } else {
                        currentFont = baseFont;
                        currentShader = baseShader; // Zurücksetzen bei '}' oder '}}'
                    }
                    textIdx += isDouble ? 2 : 1;
                }

                // Zeichen im Originaltext überspringen (z. B. von TextUtil entfernte Whitespaces)
                if (textIdx < text.length() && text.charAt(textIdx) != targetChar) {
                    textIdx++;
                }

                // Glyphe für das sichtbare Zeichen erzeugen
                char renderChar = options.isUppercase() ? Character.toUpperCase(targetChar) : targetChar;
                var sprite = currentFont.spriteFor(renderChar);

                if (sprite.isPresent()) {
                    if (!Character.isWhitespace(targetChar)) {
                        // Der ermittelte Shader wird hier direkt an den Glyph-Record übergeben
                        glyphs.add(new Glyph(Offset.at(x, y), sprite.get(), currentShader, characterNr++));
                    }
                    x += (sprite.get().width() + options.padding()) * options.scale();
                }
                textIdx++;
            }
            y += (int) (baseFont.height() * options.scale() + options.lineSpacing());
        }
        return glyphs;
    }
}