package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import static java.util.Objects.requireNonNull;

/**
 * Used to store color information used in {@link Graphics}.
 */
public final class Color implements Serializable {

    private static final Random RANDOM = new Random();

    @Serial
    private static final long serialVersionUID = 1L;

    private final int r;
    private final int g;
    private final int b;
    private final Percent opacity;

    /**
     * The color grey.
     */
    public static final Color GREY = Color.rgb(128, 128, 128);

    /**
     * The color black.
     */
    public static final Color BLACK = Color.rgb(0, 0, 0);

    /**
     * The color white.
     */
    public static final Color WHITE = Color.rgb(255, 255, 255);

    /**
     * The color red.
     */
    public static final Color RED = Color.rgb(255, 0, 0);

    /**
     * The color green.
     */
    public static final Color GREEN = Color.rgb(0, 255, 0);

    /**
     * The color dark green.
     */
    public static final Color DARK_GREEN = Color.rgb(0, 128, 0);

    /**
     * The color blue.
     */
    public static final Color BLUE = Color.rgb(0, 0, 255);

    /**
     * The color dark blue.
     */
    public static final Color DARK_BLUE = Color.rgb(0, 0, 128);

    /**
     * The color yellow.
     */
    public static final Color YELLOW = Color.rgb(255, 255, 0);

    /**
     * The color orange.
     */
    public static final Color ORANGE = Color.rgb(255, 165, 0);

    /**
     * A transparent color.
     */
    public static final Color TRANSPARENT = Color.rgb(0, 0, 0, Percent.zero());

    /**
     * Creates a random {@link Color} with full {@link #opacity()}.
     */
    public static Color random() {
        return rgb(RANDOM.nextInt(0, 255), RANDOM.nextInt(0, 255), RANDOM.nextInt(0, 255));
    }

    /**
     * Creates a color based on RGB-components with full {@link #opacity()}.
     */
    public static Color rgb(final int r, final int g, final int b) {
        return new Color(r, g, b);
    }

    /**
     * Creates a {@link Color} based on RGB-components and custom {@link #opacity()}.
     */
    public static Color rgb(final int r, final int g, final int b, final Percent opacity) {
        return new Color(r, g, b, opacity);
    }

    /**
     * Creates a {@link Color} based on the given hexadecimal value. Supports Format #RRGGBB and #AARRGGBB.
     * Example values: #D200A1, #00D2A1A2
     */
    public static Color hex(final String hexValue) {
        requireNonNull(hexValue, "hex value must not be NULL");
        if (!hexValue.startsWith("#")) {
            throw new IllegalArgumentException("hex value must start with '#'");
        }
        if (hexValue.length() == 7) {
            return Color.rgb(
                    parseHex(hexValue.substring(1, 3)),
                    parseHex(hexValue.substring(3, 5)),
                    parseHex(hexValue.substring(5, 7)));
        }
        if (hexValue.length() == 9) {
            return Color.rgb(
                    parseHex(hexValue.substring(3, 5)),
                    parseHex(hexValue.substring(5, 7)),
                    parseHex(hexValue.substring(7, 9)),
                    Percent.of(parseHex(hexValue.substring(1, 3)))
            );
        }
        throw new IllegalArgumentException("unknown hex format: " + hexValue);
    }

    private static int parseHex(String hex) {
        try {
            return Integer.valueOf(hex, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("hex value contains non hexadecimal value: " + hex, e);
        }
    }

    /**
     * Creates a new instance with same RGB-components, but custom
     * {@link #opacity()}.
     *
     * @see #opacity(Percent)
     */
    public Color opacity(final double opacity) {
        return opacity(Percent.of(opacity));
    }

    /**
     * Creates a new instance with same RGB-components, but custom
     * {@link #opacity()}.
     *
     * @see #opacity(double)
     */
    public Color opacity(final Percent opacity) {
        return new Color(r, g, b, opacity);
    }

    /**
     * Returns red value of the {@link Color}.
     */
    public int r() {
        return r;
    }

    /**
     * Returns green value of the {@link Color}.
     */
    public int g() {
        return g;
    }

    /**
     * Returns blue value of the {@link Color}.
     */
    public int b() {
        return b;
    }

    /**
     * Returns the colors opacity value.
     */
    public Percent opacity() {
        return opacity;
    }

    private Color(final int r, final int g, final int b) {
        this(r, g, b, Percent.max());
    }

    private Color(final int r, final int g, final int b, final Percent opacity) {
        Validate.range(r, 0, 255, "invalid color value (0-255): " + r);
        Validate.range(g, 0, 255, "invalid color value (0-255): " + g);
        Validate.range(b, 0, 255, "invalid color value (0-255): " + b);
        this.r = r;
        this.g = g;
        this.b = b;
        this.opacity = opacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b, g, opacity, r);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Color other = (Color) obj;
        return b == other.b && g == other.g && Objects.equals(opacity, other.opacity) && r == other.r;
    }

    @Override
    public String toString() {
        return "Color [r=" + r + ", g=" + g + ", b=" + b + ", opacity=" + opacity.value() + "]";
    }
}