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

    public record Glyph(Offset offset, Sprite sprite, int characterNr) {}

    // Wir speichern das Zeichen und den zugehörigen Modus/Style
    private record StyledChar(char character, Pixelfont font) {}

    private final String text;
    private final TextDrawOptions options;

    public RichTextBlock(final String text, final TextDrawOptions options) {
        this.text = text;
        this.options = options;
    }

    public List<Glyph> glyphs() {
        var font = options.font();
        var alternate = options.font().replaceColor(Color.WHITE, Color.RED);
        var secondaryAlternate = options.font().replaceColor(Color.WHITE, Color.BLUE);

        // 1. Schritt: Text parsen und Styles an die Zeichen binden
        List<StyledChar> styledChars = new ArrayList<>();
        StringBuilder pureTextBuilder = new StringBuilder();
        Mode currentMode = Mode.NORMAL;

        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);

            if (c == '{') {
                if (i + 1 < text.length() && text.charAt(i + 1) == '{') {
                    currentMode = Mode.SECONDARY_ALTERNATE;
                    i += 2;
                } else {
                    currentMode = Mode.ALTERNATE;
                    i++;
                }
                continue;
            }

            if (c == '}') {
                if (i + 1 < text.length() && text.charAt(i + 1) == '}') {
                    currentMode = Mode.NORMAL;
                    i += 2;
                } else {
                    currentMode = Mode.NORMAL;
                    i++;
                }
                continue;
            }

            Pixelfont activeFont = switch (currentMode) {
                case NORMAL -> font;
                case ALTERNATE -> alternate;
                case SECONDARY_ALTERNATE -> secondaryAlternate;
            };

            styledChars.add(new StyledChar(c, activeFont));
            pureTextBuilder.append(c);
            i++;
        }

        // 2. Schritt: Zeilenumbruch berechnen
        var lines = TextUtil.lineWrap(pureTextBuilder.toString(), options.charactersPerLine());

        // 3. Schritt: Glyphen erzeugen mit inhaltsbasiertem Abgleich
        List<Glyph> glyphs = new ArrayList<>();
        int y = 0;
        int characterNr = 0;

        // Dieser Zeiger wandert NUR weiter, wenn ein echtes (sichtbares) Zeichen verarbeitet wurde
        int styledListPointer = 0;

        for (final String line : lines) {
            double x = switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };

            for (int j = 0; j < line.length(); j++) {
                char character = line.charAt(j);

                // Whitespaces (Leerzeichen, Tabs, Newlines) verändern den Textfluss,
                // besitzen aber keine Glyphen und verschieben den Style-Index nicht.
                if (Character.isWhitespace(character)) {
                    // Wir bewegen den Pointer in der Master-Liste vorwärts,
                    // falls die Originalliste hier auch ein Whitespace hat.
                    if (styledListPointer < styledChars.size() &&
                        Character.isWhitespace(styledChars.get(styledListPointer).character())) {
                        styledListPointer++;
                    }

                    // X-Position für Leerzeichen aufbauen (falls nötig, je nach Ihrer Engine)
                    var spaceSprite = font.spriteFor(' ');
                    if (spaceSprite.isPresent()) {
                        x += (spaceSprite.get().width() + options.padding()) * options.scale();
                    }
                    continue;
                }

                // Sicherheits-Check: Falls Zeilen mehr Zeichen enthalten als gedacht
                if (styledListPointer >= styledChars.size()) {
                    break;
                }

                // Synchronisations-Anker: Wir spulen vor, bis wir das passende sichtbare Zeichen finden
                while (styledListPointer < styledChars.size() &&
                       Character.isWhitespace(styledChars.get(styledListPointer).character())) {
                    styledListPointer++;
                }

                if (styledListPointer >= styledChars.size()) {
                    break;
                }

                // Jetzt sind wir garantiert beim exakt passenden Zeichen angekommen
                StyledChar styledChar = styledChars.get(styledListPointer);
                Pixelfont targetFont = styledChar.font();

                var sprite = targetFont.spriteFor(options.isUppercase() ? Character.toUpperCase(character) : character);
                if (sprite.isPresent()) {
                    glyphs.add(new Glyph(Offset.at(x, y), sprite.get(), characterNr));
                    x += (sprite.get().width() + options.padding()) * options.scale();
                    characterNr++;
                }

                // Nur nach einem erfolgreich gematchten, sichtbaren Zeichen erhöhen!
                styledListPointer++;
            }
            y += (int) (1.0 * font.height() * options.scale() + options.lineSpacing());
        }
        return glyphs;
    }
}