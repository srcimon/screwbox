package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

/**
 * A comic like rendering style for smoke effects.
 *
 * @since 3.33.0
 */
public class ComicSmokeStyle implements SmokeStyle {

    @Override
    public int apply(final float red, final float green, final float blue, final float alpha) {
        final float maxChannel = Math.max(red, Math.max(green, blue));
        float funR = (red == maxChannel) ? Math.clamp(red * 1.8f, 0.0f, 1.0f) : red * 0.3f;
        float funG = (green == maxChannel) ? Math.clamp(green * 1.8f, 0.0f, 1.0f) : green * 0.3f;
        float funB = (blue == maxChannel) ? Math.clamp(blue * 1.8f, 0.0f, 1.0f) : blue * 0.3f;

        if (maxChannel > 0.8f) {
            funR = Math.clamp(funR + 0.2f, 0.0f, 1.0f);
            funG = Math.clamp(funG + 0.2f, 0.0f, 1.0f);
            funB = Math.clamp(funB + 0.2f, 0.0f, 1.0f);
        }

        funR = Math.round(funR * 2.0f) / 2.0f;
        funG = Math.round(funG * 2.0f) / 2.0f;
        funB = Math.round(funB * 2.0f) / 2.0f;

        final int redInt = (int) (funR * alpha * 255.0f + 0.5f);
        final int greenInt = (int) (funG * alpha * 255.0f + 0.5f);
        final int blueInt = (int) (funB * alpha * 255.0f + 0.5f);
        final int alphaInt = (int) (alpha * 255.0f + 0.5f);
        return packRgb(redInt, greenInt, blueInt, alphaInt);
    }
}
