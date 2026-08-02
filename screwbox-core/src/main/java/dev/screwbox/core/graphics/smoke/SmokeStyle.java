package dev.screwbox.core.graphics.smoke;

/**
 * Smoke rendering can be customized by implementing this low level api interface. The style is applied on all
 * pixels of the resulting smoke image.
 */
@FunctionalInterface
public interface SmokeStyle {

    /**
     * Applies the smoke style to the specified color components.
     *
     * @param red   the red color component, normalized between 0.0 and 1.0
     * @param green the green color component, normalized between 0.0 and 1.0
     * @param blue  the blue color component, normalized between 0.0 and 1.0
     * @param alpha the alpha (opacity) component, ranging from 0.0 (fully transparent) to 1.0 (fully opaque)
     * @return a packed integer representing the combined output color or effect state
     */
    int apply(float red, float green, float blue, float alpha);

    /**
     * Packs integer values of red, green, blue and alpha into the resulting rgb value.
     */
    default int packRgb(int red, int green, int blue, final int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
