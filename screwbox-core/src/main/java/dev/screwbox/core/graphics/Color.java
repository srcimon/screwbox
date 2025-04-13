package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.utils.Validate;

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
    private static final int MAX = 255;

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
    public static final Color WHITE = Color.rgb(MAX, MAX, MAX);

    /**
     * The color red.
     */
    public static final Color RED = Color.rgb(MAX, 0, 0);

    /**
     * The color green.
     */
    public static final Color GREEN = Color.rgb(0, MAX, 0);

    /**
     * The color dark green.
     */
    public static final Color DARK_GREEN = Color.rgb(0, 128, 0);

    /**
     * The color blue.
     */
    public static final Color BLUE = Color.rgb(0, 0, MAX);

    /**
     * The color dark blue.
     */
    public static final Color DARK_BLUE = Color.rgb(0, 0, 128);

    /**
     * The color yellow.
     */
    public static final Color YELLOW = Color.rgb(MAX, MAX, 0);

    /**
     * The color orange.
     */
    public static final Color ORANGE = Color.rgb(MAX, 165, 0);

    /**
     * A transparent color.
     */
    public static final Color TRANSPARENT = Color.rgb(0, 0, 0, Percent.zero());

    /**
     * Creates a random {@link Color} with full {@link #opacity()}.
     */
    public static Color random() {
        return rgb(RANDOM.nextInt(0, MAX), RANDOM.nextInt(0, MAX), RANDOM.nextInt(0, MAX));
    }

    /**
     * Returns {@link Color} from rgb value.
     *
     * @since 2.17.0
     */
    public static Color rgb(final int rgb) {
        final int a = (rgb >> 24) & MAX;
        final int r = (rgb >> 16) & MAX;
        final int g = (rgb >> 8) & MAX;
        final int b = rgb & MAX;
        return Color.rgb(r, g, b, Percent.of(a / 255.0));
    }

    /**
     * Returns the greyscale version of the color.
     *
     * @since 2.17.0
     */
    public Color greyscale() {
        final int average = brightness();
        return Color.rgb(average, average, average, opacity);
    }

    /**
     * Returns the rgb value of the color. Useful when operating with image filters.
     *
     * @since 2.17.0
     */
    public int rgb() {
        return alpha() << 24 | (r << 16) | (g << 8) | b;
    }

    /**
     * Returns the alpha value of the color. Useful when operating with image filters.
     *
     * @since 2.17.0
     */
    public int alpha() {
        return (int) (opacity.value() * MAX);
    }

    /**
     * Returns the inverted version of the color. Doesn't change the {@link #opacity()}.
     *
     * @since 2.17.0
     */
    public Color invert() {
        return Color.rgb(MAX - r, MAX - g, MAX - b, opacity);
    }

    /**
     * Clamps the value within valid rgb range from 0 to 255.
     *
     * @since 2.17.0
     */
    public static int clampRgbRange(final int value) {
        return Math.clamp(value, 0, MAX);
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
                    Percent.of(parseHex(hexValue.substring(1, 3)) * 1.0 / MAX)
            );
        }
        throw new IllegalArgumentException("unknown hex format: " + hexValue);
    }

    private static int parseHex(final String hex) {
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
        Validate.range(r, 0, MAX, "invalid red color value (0-255)");
        Validate.range(g, 0, MAX, "invalid green color value (0-255)");
        Validate.range(b, 0, MAX, "invalid blue color value (0-255)");
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

    /**
     * Returns the hexadecimal value of the color.
     *
     * @since 2.15.0
     */
    public String hex() {
        final String opacityValue = this.opacity.isMax() ? "" : toRgbHex((int) (this.opacity.value() * MAX));
        return "#" + opacityValue + toRgbHex(r) + toRgbHex(g) + toRgbHex(b);
    }

    private String toRgbHex(final int value) {
        String hex = Integer.toHexString(value);
        return hex.length() == 1 ? "0" + hex : hex;
    }

    /**
     * Returns the brightness of the color.
     *
     * @since 2.17.0
     */
    public int brightness() {
        return (r + g + b) / 3;
    }

    /**
     * Returns the Euclidean color difference between this and the other {@link Color}. In human terms: Calculates
     * how different the {@link Color color} looks.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Color_difference">Wikipedia Article on color difference</a>
     * @since 2.18.0
     */
    public double difference(final Color other) {
        requireNonNull(other, "other color must not be null");
        final double rDist = (double) other.r - r;
        final double gDist = (double) other.g - g;
        final double bDist = (double) other.b - b;
        return Math.sqrt(rDist * rDist + gDist * gDist + bDist * bDist);
    }
}