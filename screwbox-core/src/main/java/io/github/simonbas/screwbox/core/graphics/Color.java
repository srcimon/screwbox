package io.github.simonbas.screwbox.core.graphics;

import io.github.simonbas.screwbox.core.Percent;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Used to store color information used in {@link Graphics}.
 */
public final class Color implements Serializable {

    private static final Random RANDOM = new Random();

    private static final long serialVersionUID = 1L;

    private final int r;
    private final int g;
    private final int b;
    private final Percent opacity;

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
     * A transparent color.
     */
    public static final Color TRANSPARENT = Color.rgb(0, 0, 0, Percent.min());

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
     * Creates a color based on RGB-components and custom {@link #opacity()}.
     */
    public static Color rgb(final int r, final int g, final int b, final Percent opacity) {
        return new Color(r, g, b, opacity);
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
        validate(r);
        validate(g);
        validate(b);
        this.r = r;
        this.g = g;
        this.b = b;
        this.opacity = opacity;
    }

    private void validate(final int rgbValue) {
        if (rgbValue < 0 || rgbValue > 255) {
            throw new IllegalArgumentException("invalid color value (0-255): " + rgbValue);
        }
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
