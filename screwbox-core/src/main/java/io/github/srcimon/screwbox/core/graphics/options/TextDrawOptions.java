package io.github.srcimon.screwbox.core.graphics.options;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.TextUtil;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Customize the drawing of texts using a {@link Pixelfont}.
 *
 * @param font        the {@link Pixelfont} used for drawing
 * @param padding     the padding between characters
 * @param scale       the scale of the {@link Sprite}
 * @param isUppercase if used, changes all characters to uppercase characters
 * @param opacity     the opacity used for drawing
 * @param alignment   the direction to draw from given offset
 * @param shaderSetup the {@link ShaderSetup} used for drawing
 */
public record TextDrawOptions(Pixelfont font, int padding, double scale, boolean isUppercase, Percent opacity,
                              Alignment alignment, int charactersPerLine, int lineSpacing, ShaderSetup shaderSetup) {

    /**
     * Alignment of the text.
     */
    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public TextDrawOptions {
        requireNonNull(font, "font must not be null");
        requireNonNull(opacity, "opacity must not be null");
        requireNonNull(alignment, "alignment must not be null");
        Validate.zeroOrPositive(lineSpacing, "line spacing must be positive");
        Validate.positive(charactersPerLine, "characters per line must be positive");
    }

    private TextDrawOptions(final Pixelfont font) {
        this(font, 2, 1, false, Percent.max(), Alignment.LEFT, Integer.MAX_VALUE, 4, null);
    }


    /**
     * Creates a new instance with the given {@link Pixelfont}.
     *
     * @see #font(Pixelfont)
     */
    public static TextDrawOptions font(final Supplier<Pixelfont> font) {
        return font(font.get());
    }

    /**
     * Creates a new instance with the given {@link Pixelfont}.
     *
     * @see #font(Supplier)
     */
    public static TextDrawOptions font(final Pixelfont font) {
        return new TextDrawOptions(font);
    }

    /**
     * Creates a new instance with {@link Alignment#RIGHT}.
     */
    public TextDrawOptions alignRight() {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, Alignment.RIGHT, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Creates a new instance with {@link Alignment#CENTER}.
     */
    public TextDrawOptions alignCenter() {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, Alignment.CENTER, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Creates a new instance with given padding.
     */
    public TextDrawOptions padding(final int padding) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Creates a new instance with given scale.
     */
    public TextDrawOptions scale(final double scale) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Creates a new instance with all uppercase characters.
     */
    public TextDrawOptions uppercase() {
        return new TextDrawOptions(font, padding, scale, true, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Creates a new instance with given opacity.
     */
    public TextDrawOptions opacity(final Percent opacity) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Sets a maximum line length. Text will be wrapped when reaching the end of the line.
     */
    public TextDrawOptions charactersPerLine(final int charactersPerLine) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Sets the count of pixels between lines.
     */
    public TextDrawOptions lineSpacing(final int lineSpacing) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Sets the {@link ShaderSetup} used for drawing.
     *
     * @since 2.15.0
     */
    public TextDrawOptions shaderSetup(final ShaderSetup shaderSetup) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment, charactersPerLine, lineSpacing, shaderSetup);
    }

    /**
     * Sets the {@link ShaderSetup} used for drawing.
     *
     * @since 2.15.0
     */
    public TextDrawOptions shaderSetup(final Supplier<ShaderSetup> shaderOptions) {
        return shaderSetup(shaderOptions.get());
    }

    /**
     * Returns the width of the given text rendered with this {@link TextDrawOptions}.
     */
    public double widthOf(final String text) {
        return widthOfLines(TextUtil.lineWrap(text, charactersPerLine));
    }

    /**
     * Returns the {@link Size} of the given text rendered with this {@link TextDrawOptions}.
     */
    public Size sizeOf(final String text) {
        var lines = TextUtil.lineWrap(text, charactersPerLine);
        return Size.of(widthOfLines(lines), heightOf(lines.size()));
    }

    private double widthOfLine(final String text) {
        double totalWidth = 0;
        for (final var sprite : font.spritesFor(isUppercase ? text.toUpperCase() : text)) {
            totalWidth += (sprite.width() + padding) * scale;
        }
        return totalWidth;
    }

    private int heightOf(final int lineCount) {
        final int space = lineCount == 1 ? 0 : (lineCount - 1) * lineSpacing;
        return (int) (lineCount * font.height() * scale) + space;
    }

    private double widthOfLines(final List<String> lines) {
        double maxWidth = 0;
        for (var line : lines) {
            var width = widthOfLine(line);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }
}
