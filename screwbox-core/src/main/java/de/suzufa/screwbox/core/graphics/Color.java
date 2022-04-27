package de.suzufa.screwbox.core.graphics;

/**
 * Used to store color information used in {@link Graphics}.
 */
public final class Color {

    private final int r;
    private final int g;
    private final int b;

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
     * The color blue.
     */
    public static final Color BLUE = Color.rgb(0, 0, 255);

    /**
     * The color yellow.
     */
    public static final Color YELLOW = Color.rgb(255, 255, 0);

    /**
     * Creates a color based on RGB-components.
     */
    public static Color rgb(final int r, final int g, final int b) {
        return new Color(r, g, b);
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

    private Color(final int r, final int g, final int b) {
        validate(r);
        validate(g);
        validate(b);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private void validate(final int rgbValue) {
        if (rgbValue < 0 || rgbValue > 255) {
            throw new IllegalArgumentException("invalid color value (0-255): " + rgbValue);
        }
    }
}
