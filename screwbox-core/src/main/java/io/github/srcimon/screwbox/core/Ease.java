package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Configures the direction and the progress of a value change.
 * To get an idea of what that means: <a href="https://easings.net/de">Easing Functions Cheat</a>
 */
public enum Ease implements UnaryOperator<Percent> {

    /**
     * Linear fade in: 0 to 1
     */
    LINEAR_IN(in -> in),

    /**
     * Linear fade out: 1 to 0
     */
    LINEAR_OUT(Percent::invert),

    /**
     * Linear fade in, plateau at 1, no fade out.
     */
    IN_PLATEAU(in -> Percent.of(10 * in.value())),

    /**
     * Linear fade in, plateau at 1, linear fade out.
     */
    IN_PLATEAU_OUT(in -> Percent.of(in.value() < 0.9 ? 10 * in.value() : 1 - 10 * (in.value() - 0.9))),

    /**
     * Plateau at 1, linear fade out.
     */
    PLATEAU_OUT(in -> Percent.of(in.value() < 0.9 ? 1 : 1 - 10 * (in.value() - 0.9))),

    /**
     * Plateau at 1, linear fade out slower.
     */
    PLATEAU_OUT_SLOW(in -> Percent.of(in.value() < 0.8 ? 1 : 1 - 5 * (in.value() - 0.8))),

    /**
     * Sinus fade in: 0 to 1
     */
    SINE_IN(in -> Percent.of(Math.sin((in.value() * Math.PI) / 2.0))),

    /**
     * Sinus fade out: 1 to 0
     */
    SINE_OUT(in -> Percent.of(Math.cos((in.value() * Math.PI) / 2.0))),

    /**
     * Square function in.
     */
    SQUARE_IN(in -> Percent.of(in.value() * in.value())),

    /**
     * Square function out.
     */
    SQUARE_OUT(in -> Percent.of(Math.sqrt(in.invert().value()))),

    /**
     * Sinus fade in and out again: 0 to 1 to 0
     */
    SINE_IN_OUT(in -> Percent.of(-(Math.cos(Math.PI * in.value() * 2.0) - 1.0) / 2.0)),

    /**
     * Sinus fade in and out again twice: 0 to 1 to 0 to 1 to 0
     */
    SIN_IN_OUT_TWICE(in -> Percent.of(-(Math.cos(Math.PI * in.value() * 4.0) - 1.0) / 2.0)),

    /**
     * Flickering effect. Mostly 1 but sometimes 0.
     *
     * @see #SPARKLE
     */
    FLICKER(FlickerSupport::sequenceValue),

    /**
     * Sparkling effect. Mostly 0 but sometimes 1.
     *
     * @see #FLICKER
     */
    SPARKLE(in -> FlickerSupport.sequenceValue(in).invert());

    private static class FlickerSupport {
        private static final String FLICKER = "##########__1#######_##########################################################################___2233##########2##################################################5########################################################235#############55443#####################################555##";
        private static final Map<Character, Percent> TRANSLATION = Map.of(
                '_', Percent.zero(),
                '#', Percent.max(),
                '1', Percent.of(0.1),
                '2', Percent.of(0.2),
                '3', Percent.of(0.3),
                '4', Percent.of(0.4),
                '5', Percent.of(0.5));

        private static Percent sequenceValue(Percent progress) {
            final int sequencePosition = (int) (FLICKER.length() * progress.value() % FLICKER.length());
            return TRANSLATION.get(FLICKER.charAt(sequencePosition));
        }
    }

    private final UnaryOperator<Percent> adjustment;

    Ease(final UnaryOperator<Percent> adjustment) {
        this.adjustment = adjustment;
    }

    /**
     * Applies the {@link Ease} on an input value.
     */
    public Percent apply(final Percent input) {
        return adjustment.apply(input);
    }

    /**
     * Creates a preview image that visualizes the {@link Ease}.
     *
     * @param color color to use for the value
     * @param size  width and height of the image
     * @since 2.15.0
     */
    public Frame createPreview(final Color color, final int size) {
        Validate.positive(size, "size must be positive");
        Objects.requireNonNull(color, "color must not be null");
        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        final Graphics2D graphics2D = (Graphics2D) image.getGraphics();

        for (double x = 0; x < image.getWidth(); x += 0.05) {
            int y = (int) (size - apply(Percent.of(x / size)).value() * size * 1.0);
            graphics2D.setColor(AwtMapper.toAwtColor(color));
            graphics2D.drawLine((int) x, y, (int) x, y);
        }
        graphics2D.dispose();
        return Frame.fromImage(image);
    }
}
